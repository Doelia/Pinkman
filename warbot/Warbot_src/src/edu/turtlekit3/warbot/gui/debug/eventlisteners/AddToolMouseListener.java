package edu.turtlekit3.warbot.gui.debug.eventlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import madkit.action.SchedulingAction;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentCategory;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.gui.debug.DebugModeToolBar;
import edu.turtlekit3.warbot.gui.debug.DebugToolsPnl;
import edu.turtlekit3.warbot.tools.CoordCartesian;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class AddToolMouseListener implements MouseListener {

	private DebugModeToolBar _debugToolBar;
	private DebugToolsPnl _toolsPnl;

	private CoordCartesian _clickedPos;
	private String _lastSelectedTeam;

	public AddToolMouseListener(DebugModeToolBar debugToolBar, DebugToolsPnl toolsPnl) {
		_debugToolBar = debugToolBar;
		_toolsPnl = toolsPnl;

		_clickedPos = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		_debugToolBar.getViewer().setMapExplorationEventsEnabled(false);
		if (_toolsPnl.getSelectedWarAgentTypeToCreate() != null) {
			_clickedPos = new CoordCartesian(e.getX() / _debugToolBar.getViewer().getCellSize(),
					e.getY() / _debugToolBar.getViewer().getCellSize());
		} else {
			JOptionPane.showMessageDialog(_debugToolBar, "Veuillez s�lectionner un type d'agent.", "Cr�ation d'un agent impossible", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			if (_clickedPos != null) {
				if (_toolsPnl.getSelectedWarAgentTypeToCreate().getCategory() == WarAgentCategory.Resource) {
					WarAgent a = Game.instantiateNewWarResource(_toolsPnl.getSelectedWarAgentTypeToCreate().toString());
					_debugToolBar.getViewer().launchAgent(a);
					a.setPosition(_clickedPos);
				} else {
					CoordCartesian mouseClickPosition = new CoordCartesian(e.getX() / _debugToolBar.getViewer().getCellSize(),
							e.getY() / _debugToolBar.getViewer().getCellSize());
					CoordPolar movement = new CoordCartesian(mouseClickPosition.getX() - _clickedPos.getX(), mouseClickPosition.getY() - _clickedPos.getY()).toPolar();
					String[] choices = Game.getInstance().getPlayerTeamNames();
					if (_lastSelectedTeam == null)
						_lastSelectedTeam = choices[0];
					String teamName = (String) JOptionPane.showInputDialog(_debugToolBar, "A quelle �quipe appartient cet agent ?",
							"Choix d'�quipe", JOptionPane.QUESTION_MESSAGE, null, choices, _lastSelectedTeam);
					_lastSelectedTeam = teamName;
					WarAgent a = Game.instantiateNewControllableWarAgent(_toolsPnl.getSelectedWarAgentTypeToCreate().toString(),
							Game.getInstance().getPlayerTeam(teamName));
					_debugToolBar.getViewer().launchAgent(a);
					a.setHeading(movement.getAngle());
					a.setPosition(_clickedPos);
				}

				// TODO � remplacer par une simple actualisation de l'affichage
				_debugToolBar.getViewer().sendMessage(_debugToolBar.getViewer().getCommunity(), TKOrganization.ENGINE_GROUP,
						TKOrganization.SCHEDULER_ROLE,
						new SchedulingMessage(SchedulingAction.STEP));
			}
		} catch (Exception ex) {
			System.err.println("Erreur lors de l'instanciation de l'agent " + _toolsPnl.getSelectedWarAgentTypeToCreate().toString());
			ex.printStackTrace();
		}

		_clickedPos = null;
		_debugToolBar.getViewer().setMapExplorationEventsEnabled(true);
	}

}
