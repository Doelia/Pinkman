package edu.turtlekit3.warbot.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.agents.WarProjectile;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarKernelMessage;
import edu.turtlekit3.warbot.gui.launcher.WarLauncherInterface;
import edu.turtlekit3.warbot.tools.WarMathTools;

public class Team extends Observable {
	
	public static int MAX_DYING_STEP = 5;
	public static final String DEFAULT_GROUP_NAME = "defaultGroup-Warbot";
	
	private String _name;
	private ImageIcon _teamLogo;
	private Color _color;
	private String _description;
	private HashMap<String, Class<? extends WarBrainController>> _brainControllers; 
	private ArrayList<ControllableWarAgent> _controllableAgents;
	private ArrayList<WarProjectile> _projectiles;
	private HashMap<WarAgentType, Integer> _nbUnitsLeft;
	private ArrayList<WarAgent> _dyingAgents;
	
	public Team(String nom) {
		_name = nom;
		_color = Color.WHITE;
		_teamLogo = null;
		_description = "";
		_controllableAgents = new ArrayList<>();
		_projectiles = new ArrayList<>();
		_brainControllers = new HashMap<>();
		_nbUnitsLeft = new HashMap<>();
		for(WarAgentType type : WarAgentType.values())
			_nbUnitsLeft.put(type, 0);
		_dyingAgents = new ArrayList<>();
	}
	
	public Team(String nom, Color color, ImageIcon logo, String description, ArrayList<ControllableWarAgent> controllableAgents, ArrayList<WarProjectile> projectiles,
			HashMap<String, Class<? extends WarBrainController>> brainControllers, HashMap<WarAgentType, Integer> nbUnitsLeft, ArrayList<WarAgent> dyingAgents) {
		_name = nom;
		_color = color;
		_teamLogo = logo;
		_description = description;
		_controllableAgents = controllableAgents;
		_projectiles = projectiles;
		_brainControllers = brainControllers;
		_nbUnitsLeft = nbUnitsLeft;
		_dyingAgents = dyingAgents;
	}
	
    public void setLogo(ImageIcon logo) {
        _teamLogo = logo;
    }
	
    public void addBrainControllerClassForAgent(String agent, Class<? extends WarBrainController> brainController) {
    	_brainControllers.put(agent, brainController);
    }
    
    public Class<? extends WarBrainController> getBrainControllerOfAgent(String agentName) {
    	return _brainControllers.get(agentName);
    }
    
    public HashMap<String, Class<? extends WarBrainController>> getAllBrainControllers() {
    	return _brainControllers;
    }
    
    public ImageIcon getImage() {
        return _teamLogo;
    }
    
	public String getName() {
		return _name;
	}
	
	public void setName(String newName) {
		_name = newName;
	}
	
	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String description) {
		_description = description;
	}
	
	public void addWarAgent(WarAgent agent) {
		WarAgentType type = WarAgentType.valueOf(agent.getClass().getSimpleName());
		_nbUnitsLeft.put(type, _nbUnitsLeft.get(type) + 1);
		if (agent instanceof WarProjectile)
			_projectiles.add((WarProjectile) agent);
		else if (agent instanceof ControllableWarAgent)
			_controllableAgents.add((ControllableWarAgent) agent);
		agent.getLogger().log(Level.FINEST, agent.toString() + " added to team " + this.getName());
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<ControllableWarAgent> getControllableAgents() {
		return _controllableAgents;
	}
	
	public ArrayList<WarProjectile> getProjectiles() {
		return _projectiles;
	}
	
	public void removeWarAgent(WarAgent agent) {
		WarAgentType type = WarAgentType.valueOf(agent.getClass().getSimpleName());
		_nbUnitsLeft.put(type, _nbUnitsLeft.get(type) - 1);
		if (agent instanceof WarProjectile)
			_projectiles.remove(agent);
		else
			_controllableAgents.remove(agent);
		
		setChanged();
		notifyObservers();
	}
	
	public void setWarAgentAsDying(WarAgent agent) {
		removeWarAgent(agent);
		_dyingAgents.add(agent);
	}
	
	public ArrayList<WarAgent> getAllAgents() {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		toReturn.addAll(_controllableAgents);
		toReturn.addAll(_projectiles);
		return toReturn;
	}

	/**
	 * Retourne l'agent dont l'id est celui pass� en param�tre
	 * @param id - id de l'agent � r�cup�rer
	 * @return l'agent dont l'id est pass� en param�tre
	 * @return null si aucun agent n'a �t� trouv�
	 */
	public WarAgent getAgentWithID(int id) {
		for(WarAgent a : getAllAgents()) {
			if (a.getID() == id)
				return a;
		}
		return null;
	}
	
	public void sendMessageToAllMembers(ControllableWarAgent sender, String message, String[] content) {
		// A savoir que Madkit exclut la possibilit� qu'un agent s'envoie un message � lui-m�me, nous ne faisons donc pas le test ici
		for (WarAgent a : _controllableAgents) {
			sender.sendMessage(a.getAgentAddressIn(getName(), DEFAULT_GROUP_NAME, a.getClass().getSimpleName()),
					new WarKernelMessage(sender, message, content));
		}
	}
	
	@Override
	public boolean equals(Object team) {
		if (team instanceof Team)
			return this.getName().equals(((Team) team).getName());
		else if (team instanceof String)
			return this.getName().equals((String) team);
		else
			return false;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadiusOf(WarAgent referenceAgent, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (WarAgent a : getAllAgents()) {
			if (referenceAgent.getDistanceFrom(a) <= radius) {
				toReturn.add(a);
			}
		}
		return toReturn;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadius(double posX, double posY, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (WarAgent a : getAllAgents()) {
			if ((WarMathTools.getDistanceBetweenTwoPoints(posX, posY, a.getX(), a.getY()) - a.getHitboxRadius()) <= radius) {
				toReturn.add(a);
			}
		}
		return toReturn;
	}

	public void setColor(Color color) {
		this._color = color;
	}
	
	public Color getColor() {
		return _color;
	}
	
	public int getNbUnitsLeftOfType(WarAgentType type) {
		return _nbUnitsLeft.get(type);
	}
	
	public HashMap<WarAgentType, Integer> getAllNbUnitsLeft() {
		return _nbUnitsLeft;
	}
	
	public void destroy() {
		for (WarAgent a : getAllAgents())
			a.killAgent(a);
	}
	
	public static Team duplicate(Team t, String newName) {
		return new Team(newName,
				new Color(t.getColor().getRGB()),
				t.getImage(),
				t.getDescription(),
				new ArrayList<>(t.getControllableAgents()),
				new ArrayList<>(t.getProjectiles()),
				new HashMap<>(t.getAllBrainControllers()),
				new HashMap<>(t.getAllNbUnitsLeft()),
				new ArrayList<>(t.getDyingAgents())
				);
	}
	
	public static String getRealNameFromTeamName(String teamName) {
		if (teamName.endsWith(WarLauncherInterface.TEXT_ADDED_TO_DUPLICATE_TEAM_NAME))
			return teamName.substring(0, teamName.lastIndexOf(WarLauncherInterface.TEXT_ADDED_TO_DUPLICATE_TEAM_NAME));
		else
			return teamName;
	}

	public void doOnEachTick() {
		for (int i = 0; i < _dyingAgents.size(); i++) {
			WarAgent a = _dyingAgents.get(i);
			a.incrementDyingStep();
			if (a.getDyingStep() > MAX_DYING_STEP) {
				_dyingAgents.remove(i);
			}
		}
	}
	
	public ArrayList<WarAgent> getDyingAgents() {
		return new ArrayList<>(_dyingAgents);
	}
}
