package edu.turtlekit3.warbot.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import turtlekit.viewer.AbstractViewer;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.agents.WarProjectile;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.agents.WarKamikaze;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.agents.WarTurret;
import edu.turtlekit3.warbot.agents.capacities.Movable;
import edu.turtlekit3.warbot.agents.percepts.InConePerceptsGetter;
import edu.turtlekit3.warbot.agents.percepts.InRadiusPerceptsGetter;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.agents.resources.WarFood;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.gui.debug.DebugModeToolBar;
import edu.turtlekit3.warbot.gui.toolbar.WarToolBar;
import edu.turtlekit3.warbot.launcher.Simulation;
import edu.turtlekit3.warbot.tools.CoordCartesian;
import edu.turtlekit3.warbot.tools.CoordPolar;
import edu.turtlekit3.warbot.tools.WarMathTools;

@SuppressWarnings("serial")
public class WarViewer extends AbstractViewer {

	// D�finit le niveau de zoom initial
	public static final int CELL_SIZE = 1;
	
	private static final int _healthBarDefaultSize = 10;
	private static final int _spaceBetweenAgentAndHealthBar = 2;
	
	private WarToolBar wtb;
	private DebugModeToolBar _autorModeToolBar;

	private MouseListener _mapExploremMouseListener;
	
	//private Graphics _mapDisplay;
	private ArrayList<Shape> _explosions;
	
	private ArrayList<Integer> _agentsIDsSeenBySelectedAgent;
	
	private JTabbedPane tabs;

	private SwingView swingView;
	private JScrollPane scrollPane;
	
	private JPanel gdxContainer;
	
	private int width, height;
	
	private boolean loadGdx;
	

	public WarViewer() {
		super();
		wtb = new WarToolBar(this);
		_autorModeToolBar = new DebugModeToolBar(this);
		_explosions = new ArrayList<>();
		_agentsIDsSeenBySelectedAgent = new ArrayList<>();
		
		loadGdx = Simulation.getInstance().isEnabledEnhancedGraphism();
	}

	@Override
	public void setupFrame(final JFrame frame) {
		super.setupFrame(frame);
		width = getWidth();
		height = getHeight();
		wtb.init(frame);
		_autorModeToolBar.init(frame);
		setCellSize(CELL_SIZE);
		((JScrollPane) getDisplayPane()).getHorizontalScrollBar().setUnitIncrement(10);
		((JScrollPane) getDisplayPane()).getVerticalScrollBar().setUnitIncrement(10);
		swingView = new SwingView();
		swingView.setSize(new Dimension(width, height));
		
		gdxContainer = new JPanel();
		
		frame.remove(getDisplayPane());
		
		scrollPane = new JScrollPane(swingView);
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				updateSize(e.getPoint(),e.getWheelRotation());
			}
		});

		tabs = new JTabbedPane();
		tabs.addTab("Vue standard", scrollPane);	
		tabs.addTab("2D Isométrique", gdxContainer);
		gdxContainer.setSize(1024, 768);
		
		frame.add(tabs, BorderLayout.CENTER);
				
		_mapExploremMouseListener = new MapExplorationListener(this);
		setMapExplorationEventsEnabled(true);

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		if (loadGdx) {
			EventQueue.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					new WarViewerEvolvedLauncher(gdxContainer, gdxContainer.getSize().width, gdxContainer.getSize().height);
				}
			});
		} else {
			showNoGdxLoaded();
		}
	}
	
	private void showNoGdxLoaded() {
		JLabel text = new JLabel("Vue isométrique non chargée");
		gdxContainer.add(text);
	}

	private void updateSize(Point point, int wheelRotation) {
		final int i = point.x / cellSize;
		int offX = (int) i * wheelRotation;
		int offY = (int) (point.y / cellSize) * wheelRotation;
		cellSize -= wheelRotation;
		if (cellSize < 1)
			cellSize = 1;
		if (cellSize > 1) {
			swingView.setLocation(swingView.getLocation().x + offX,
					scrollPane.getLocation().y + offY);
		}
		else{
			swingView.setLocation(0, 0);
		}
		swingView.setPreferredSize(new Dimension(getWidth() * cellSize, getHeight() * cellSize));
		swingView.getParent().doLayout();
	}
	
	@Override
	protected void observe() {
		if (isSynchronousPainting()) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						swingView.repaint();
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			swingView.repaint();
		}
	}

	@Override
	protected void activate() {
		super.activate();
		setSynchronousPainting(false);
	}

	@Override
	protected void render(Graphics g) {
		// Avec observe redefini, render n est plus appelee. L'affichage est
		// effectue dans SwingView.paintComponent
	}
	
	private void paintTeam(Graphics g, Team team) {
		Color backgroundColor = team.getColor();
		Color borderColor = backgroundColor.darker();
		Color perceptsColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(),
				backgroundColor.getBlue(), 100);
		boolean isCurrentAgentTheSelectedOne = false;
		boolean haveOneColorChanged = false;
		
		for(WarAgent agent : team.getAllAgents()) {
			// Si les couleurs ont �t� modifi�es, on restaure les couleurs
			if (haveOneColorChanged) {
				backgroundColor = team.getColor();
				borderColor = backgroundColor.darker();
				isCurrentAgentTheSelectedOne = false;
				haveOneColorChanged = false;
			}
			
			if (_autorModeToolBar.isVisible()) {
				if (_autorModeToolBar.getSelectedAgent() != null) {
					if (agent.getID() == _autorModeToolBar.getSelectedAgent().getID()) {
						borderColor = Color.GRAY;
						backgroundColor = Color.WHITE;
						isCurrentAgentTheSelectedOne = true;
						haveOneColorChanged = true;
					}
				}
			}
			
			// Si l'agent courant est vu par l'agent s�lectionn�
			if (_agentsIDsSeenBySelectedAgent.contains(agent.getID())) {
				borderColor = Color.YELLOW;
				haveOneColorChanged = true;
			}

			if (agent instanceof ControllableWarAgent) {
				if (wtb.isShowHealthBars())
					paintHealthBar(g, (ControllableWarAgent) agent);
				if (wtb.isShowInfos())
					paintInfos(g, (ControllableWarAgent) agent, backgroundColor);
				if(wtb.isShowPercepts())
					paintPerceptionArea(g, (ControllableWarAgent) agent, perceptsColor);
				if (wtb.isShowDebugMessages() && !isCurrentAgentTheSelectedOne)
					paintDebugMessage(g, (ControllableWarAgent) agent);
			}
			
			if (agent instanceof WarBase)
				paintWarBase(g, (WarBase) agent, borderColor, backgroundColor);
			else if (agent instanceof WarExplorer)
				paintWarExplorer(g, (WarExplorer) agent, borderColor, backgroundColor);
			else if (agent instanceof WarRocket)
				paintWarRocket(g, (WarRocket) agent, borderColor, backgroundColor);
			else if (agent instanceof WarRocketLauncher)
				paintWarRocketLauncher(g, (WarRocketLauncher) agent, borderColor, backgroundColor);
			else if (agent instanceof WarFood)
				paintWarFood(g, (WarFood) agent, borderColor, backgroundColor);
			else if (agent instanceof WarKamikaze)
				paintWarKamikaze(g, (WarKamikaze) agent, borderColor, backgroundColor);
			else if (agent instanceof WarEngineer)
				paintWarEngineer(g, (WarEngineer) agent, borderColor, backgroundColor);
			else if (agent instanceof WarTurret)
				paintWarTurret(g, (WarTurret) agent, borderColor, backgroundColor);

			if (agent instanceof Movable) {
				paintHeading(g, agent, borderColor);
			}
		}
		
		if (_autorModeToolBar.getSelectedAgent() != null) {
			if (_autorModeToolBar.getSelectedAgent() instanceof ControllableWarAgent)
				paintDebugMessage(g, (ControllableWarAgent) _autorModeToolBar.getSelectedAgent());
		}
		
		// Affichage des agents mourants
		for (WarAgent a : team.getDyingAgents()) {
			if (a instanceof WarProjectile)
				_explosions.add(createExplosionShape(a.getPosition(), (int) (((WarProjectile)a).getExplosionRadius() - Team.MAX_DYING_STEP + a.getDyingStep())));				
			else
				_explosions.add(createExplosionShape(a.getPosition(), (int) ((a.getDyingStep() + a.getHitboxRadius()) * 2)));
		}
	}

	private void paintWarBase(Graphics g, WarBase agent, Color borderColor, Color backgroundColor) {
		paintSquareOfAgent(g, agent, borderColor, backgroundColor);
	}

	private void paintWarRocketLauncher(Graphics g, WarRocketLauncher agent, Color borderColor, Color backgroundColor) {
		paintOrientedSquareOfAgent(g, agent, borderColor, backgroundColor);
	}

	private void paintWarExplorer(Graphics g, WarExplorer agent, Color borderColor, Color backgroundColor) {
		paintCircleOfAgent(g, agent, borderColor, backgroundColor);
	}

	private void paintWarFood(Graphics g, WarFood agent, Color borderColor, Color backgroundColor) {
		paintSquareOfAgent(g, agent, borderColor, backgroundColor);
	}

	private void paintWarRocket(Graphics g, WarRocket agent, Color borderColor, Color backgroundColor) {
		paintSquareOfAgent(g, agent, borderColor, backgroundColor);
	}
	
	private void paintWarKamikaze(Graphics g, WarKamikaze agent, Color borderColor, Color backgroundColor) {
		paintOrientedDiamondOfAgent(g, agent, borderColor, backgroundColor);
	}
	
	private void paintWarEngineer(Graphics g, WarEngineer agent, Color borderColor, Color backgroundColor) {
		paintOrientedTriangleOfAgent(g, agent, borderColor, backgroundColor);
	}
	
	private void paintWarTurret(Graphics g, WarTurret agent, Color borderColor, Color backgroundColor) {
		paintOrientedCustomTriangleOfAgent(g, agent, borderColor, backgroundColor);
	}

	private void paintHeading(Graphics g, WarAgent agent, Color color) {
		g.setColor(color);
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		g.drawLine((int) xPos, (int) yPos,
				(int) (xPos + hitboxRadius * Math.cos(Math.toRadians(agent.getHeading()))),
				(int) (yPos + hitboxRadius * Math.sin(Math.toRadians(agent.getHeading()))));
	}

	private void paintHealthBar(Graphics g, ControllableWarAgent agent) {
		Color previousColor = g.getColor();
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		int healthBarHeight = 3 * cellSize;
		double healthBarWidth = _healthBarDefaultSize * cellSize;
		int healthWidth = (int) (healthBarWidth * (Double.valueOf(agent.getHealth()) / Double.valueOf(agent.getMaxHealth())));
		double xBarPos = xPos - (healthBarWidth / 2);
		double yBarPos = yPos - hitboxRadius - healthBarHeight - (_spaceBetweenAgentAndHealthBar * cellSize);

		if (agent.getHealth() <= (agent.getMaxHealth() * 0.25))
			g.setColor(Color.RED);
		else
			g.setColor(Color.ORANGE);
		g.fillRect((int) xBarPos, (int) yBarPos, (int) healthBarWidth, healthBarHeight);
		
		g.setColor(Color.GREEN);
		g.fillRect((int) xBarPos, (int) yBarPos, healthWidth, healthBarHeight);

		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) xBarPos, (int) yBarPos, (int) healthBarWidth, healthBarHeight);

		g.setColor(previousColor);
	}

	private void paintDebugMessage(Graphics g, ControllableWarAgent agent) {
		if(agent.getDebugString() != ""){
			String msg = agent.getDebugString();
			Color fontColor = agent.getDebugStringColor();
			
			int distanceBubbleFromAgent = 20;
			int padding = 2;
			
			Font font = new Font("Arial", Font.PLAIN, 10);
			FontMetrics metrics = g.getFontMetrics(font);
			Dimension speechBubbleSize = new Dimension(metrics.stringWidth(msg) + (2 * padding), metrics.getHeight() + (2 * padding));
			
			Color backgroundColor;
			boolean fontIsDark = ((fontColor.getRed() + fontColor.getGreen() + fontColor.getBlue()) / 3) < 127;
			if (fontIsDark)
				backgroundColor = Color.WHITE;
			else
				backgroundColor = Color.BLACK;
			
			int posX = (int) ((agent.getX()) * cellSize - (5 / cellSize) - speechBubbleSize.width - distanceBubbleFromAgent);
			int posY = (int) ((agent.getY()) * cellSize - (5 / cellSize) - speechBubbleSize.height - distanceBubbleFromAgent);
			g.setColor(Color.BLACK);
			g.drawLine(posX, posY, ((int) agent.getX() * cellSize), ((int) agent.getY() * cellSize));
			g.setColor(backgroundColor);
			g.fillRect(posX, posY, speechBubbleSize.width, speechBubbleSize.height);
			g.setColor(Color.BLACK);
			g.drawRect(posX, posY, speechBubbleSize.width, speechBubbleSize.height);
			g.setColor(fontColor);
			g.setFont(font);
			g.drawString(msg, posX + padding, posY + speechBubbleSize.height - padding);
		}
	}

	public DebugModeToolBar getAutorModeToolBar() {
		return _autorModeToolBar;
	}

	private void paintOrientedSquareOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		CoordCartesian agentPos = new CoordCartesian(agent.getX() * cellSize, agent.getY() * cellSize);
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		Polygon square = new Polygon();
		
		double squaredHitboxRadius = Math.pow(hitboxRadius, 2);
		double halfDiagonaleSize = Math.sqrt(squaredHitboxRadius * 2);
		
		CoordCartesian frontLeftPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(halfDiagonaleSize, agent.getHeading() + 45));
		square.addPoint((int) frontLeftPos.getX(), (int) frontLeftPos.getY());
		
		CoordCartesian frontRightPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(halfDiagonaleSize, agent.getHeading() - 45));
		square.addPoint((int) frontRightPos.getX(), (int) frontRightPos.getY());
		
		CoordCartesian backRightPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(halfDiagonaleSize, agent.getHeading() - 135));
		square.addPoint((int) backRightPos.getX(), (int) backRightPos.getY());
		
		CoordCartesian backtLeftPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(halfDiagonaleSize, agent.getHeading() + 135));
		square.addPoint((int) backtLeftPos.getX(), (int) backtLeftPos.getY());
		
		g.setColor(backgroundColor);
		g.fillPolygon(square);
		g.setColor(borderColor);
		g.drawPolygon(square);
	}

	private void paintSquareOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		Polygon square = new Polygon();
		square.addPoint((int) (xPos - hitboxRadius),
				(int) (yPos - hitboxRadius));
		square.addPoint((int) (xPos - hitboxRadius),
				(int) (yPos + hitboxRadius));
		square.addPoint((int) (xPos + hitboxRadius),
				(int) (yPos + hitboxRadius));
		square.addPoint((int) (xPos + hitboxRadius),
				(int) (yPos - hitboxRadius));
		
		g.setColor(backgroundColor);
		g.fillPolygon(square);
		g.setColor(borderColor);
		g.drawPolygon(square);
	}

	private void paintCircleOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		
		int xCenter = (int) (xPos - hitboxRadius);
		int yCenter = (int) (yPos - hitboxRadius);
		int hitboxDiameter = (int) (hitboxRadius * 2);
		
		g.setColor(backgroundColor);
		g.fillOval(xCenter, yCenter, hitboxDiameter, hitboxDiameter);
		g.setColor(borderColor);
		g.drawOval(xCenter, yCenter, hitboxDiameter, hitboxDiameter);
	}

	private void paintOrientedDiamondOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		CoordCartesian agentPos = new CoordCartesian(agent.getX() * cellSize, agent.getY() * cellSize);
		double hitboxRadius = agent.getHitboxRadius() * cellSize;

		Polygon diamond = new Polygon();
		
		CoordCartesian frontPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading()));
		diamond.addPoint((int) frontPos.getX(), (int) frontPos.getY());
		
		CoordCartesian leftPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() + 90));
		diamond.addPoint((int) leftPos.getX(), (int) leftPos.getY());

		CoordCartesian backPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() + 180));
		diamond.addPoint((int) backPos.getX(), (int) backPos.getY());

		CoordCartesian rightPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() - 90));
		diamond.addPoint((int) rightPos.getX(), (int) rightPos.getY());

		g.setColor(backgroundColor);
		g.fillPolygon(diamond);
		g.setColor(borderColor);
		g.drawPolygon(diamond);
	}

	private void paintOrientedTriangleOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		CoordCartesian agentPos = new CoordCartesian(agent.getX() * cellSize, agent.getY() * cellSize);
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		Polygon triangle = new Polygon();

		CoordCartesian frontPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading()));
		triangle.addPoint((int) frontPos.getX(), (int) frontPos.getY());
		
		CoordCartesian backLeftPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() + 130));
		triangle.addPoint((int) backLeftPos.getX(), (int) backLeftPos.getY());
		
		CoordCartesian backRightPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() - 130));
		triangle.addPoint((int) backRightPos.getX(), (int) backRightPos.getY());

		g.setColor(backgroundColor);
		g.fillPolygon(triangle);
		g.setColor(borderColor);
		g.drawPolygon(triangle);
	}
	
	private void paintOrientedCustomTriangleOfAgent(Graphics g, WarAgent agent, Color borderColor, Color backgroundColor) {
		CoordCartesian agentPos = new CoordCartesian(agent.getX() * cellSize, agent.getY() * cellSize);
		double hitboxRadius = agent.getHitboxRadius() * cellSize;
		Polygon triangle = new Polygon();

		CoordCartesian frontPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading()));
		triangle.addPoint((int) frontPos.getX(), (int) frontPos.getY());
		
		CoordCartesian backLeftPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() + 130));
		triangle.addPoint((int) backLeftPos.getX(), (int) backLeftPos.getY());
		
		triangle.addPoint((int) agentPos.getX(), (int) agentPos.getY());
		
		CoordCartesian backRightPos = WarMathTools.addTwoPoints(agentPos, new CoordPolar(hitboxRadius, agent.getHeading() - 130));
		triangle.addPoint((int) backRightPos.getX(), (int) backRightPos.getY());

		g.setColor(backgroundColor);
		g.fillPolygon(triangle);
		g.setColor(borderColor);
		g.drawPolygon(triangle);
	}	

	private void paintInfos(Graphics g, ControllableWarAgent agent, Color color) {
		g.setColor(color);
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		g.drawString(agent.getClass().getSimpleName() + " " + agent.getID()
				+ ": " + agent.getTeam().getName() + ", " + agent.getHealth()
				+ " HP, heading: " + (int) agent.getHeading(),
				(int) (xPos + (agent.getHitboxRadius() * cellSize)),
				(int) yPos);
	}

	private void paintPerceptionArea(Graphics g, ControllableWarAgent agent, Color color) {
		g.setColor(color);
		double radius = agent.getDistanceOfView() * cellSize;
		double xPos = agent.getX() * cellSize;
		double yPos = agent.getY() * cellSize;
		if (Simulation.getInstance().getPerceptsGetterClass().equals(InRadiusPerceptsGetter.class)) {
			g.drawOval((int) (xPos - radius),
					(int) (yPos - radius),
					(int) (2 * radius),
					(int) (2 * radius));
		} else if (Simulation.getInstance().getPerceptsGetterClass().equals(InConePerceptsGetter.class)) {
			((Graphics2D) g).draw(new Arc2D.Double(
					xPos - radius, yPos - radius,
					2. * radius, 2. * radius,
					360. - agent.getHeading() - (agent.getAngleOfView() / 2.),
					agent.getAngleOfView(),
					Arc2D.PIE));
		}
	}

	private Shape createExplosionShape(CoordCartesian pos, int radius) {
		int newRadius = radius * cellSize;
	    return createStar(10, new CoordCartesian(pos.getX() * cellSize, pos.getY() * cellSize).toPoint(), newRadius, newRadius / 2);
	}
	
	private void paintExplosionShape(Graphics2D g2d, Shape s) {
		RadialGradientPaint color = new RadialGradientPaint(new CoordCartesian(s.getBounds2D().getCenterX(), s.getBounds2D().getCenterY()),
				(float) s.getBounds2D().getWidth(),
				new float[] {0.0f, 0.5f},
				new Color[] {Color.RED, Color.YELLOW});
	    g2d.setPaint(color);
	    g2d.fill(s);
	}
	
	private Shape createStar(int nbArms, Point center, double radiusOuterCircle, double radiusInnerCircle) {
	    double angle = Math.PI / nbArms;
	    GeneralPath path = new GeneralPath();
	    for (int i = 0; i < 2 * nbArms; i++) {
	        double r = (i & 1) == 0 ? radiusOuterCircle : radiusInnerCircle;
	        Point2D.Double p = new Point2D.Double(center.x + Math.cos(i * angle) * r, center.y + Math.sin(i * angle) * r);
	        if (i == 0) path.moveTo(p.getX(), p.getY());
	        else path.lineTo(p.getX(), p.getY());
	    }
	    path.closePath();
	    return path;
	}
	
	public void setMapExplorationEventsEnabled(boolean bool) {
		if (bool) {
			Toolkit.getDefaultToolkit().addAWTEventListener((AWTEventListener) _mapExploremMouseListener, AWTEvent.KEY_EVENT_MASK);
			swingView.addMouseListener(_mapExploremMouseListener);
			swingView.addMouseMotionListener((MouseMotionListener) _mapExploremMouseListener);
		} else {
			Toolkit.getDefaultToolkit().removeAWTEventListener((AWTEventListener) _mapExploremMouseListener);
			swingView.removeMouseListener(_mapExploremMouseListener);
			swingView.removeMouseMotionListener((MouseMotionListener) _mapExploremMouseListener);
		}
	}
	
	public JPanel getSwingView() {
		return swingView;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	class SwingView extends JPanel {
		
		@Override
		 public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			 ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                     RenderingHints.VALUE_ANTIALIAS_ON);
			
			//affichage du nombre de FPS
			g.drawString("TPS : " + Game.getInstance().getFPS().toString(), 1, 11);
			
			if (_autorModeToolBar.getSelectedAgent() != null) {
				// Update de l'affichage des infos sur l'unit� s�lectionn�e
				_autorModeToolBar.getAgentInformationsPanel().update();
				// On r�cup�re la liste des agents vus par l'agent s�lectionn�
				WarAgent selectedAgent = _autorModeToolBar.getSelectedAgent();
				if (selectedAgent instanceof ControllableWarAgent) {
					for(WarPercept p : ((ControllableWarAgent) selectedAgent).getPercepts())
						_agentsIDsSeenBySelectedAgent.add(p.getID());
				}
			}
			
			// Affichage de M�re Nature (resources)
			paintTeam(g, Game.getInstance().getMotherNatureTeam());

			// Affichage des �quipes
			for (Team t : Game.getInstance().getPlayerTeams()) {
				paintTeam(g, t);
			}

			// Affichage des explosions
			Graphics2D g2d = (Graphics2D) g;
			for (Shape s : _explosions)
				paintExplosionShape(g2d, s);
			_explosions.clear();
			
			g.setColor(Color.RED);
			g.drawRect(0, 0, width * cellSize, height * cellSize);
			
			_agentsIDsSeenBySelectedAgent.clear();
		 }
		
	}
}
