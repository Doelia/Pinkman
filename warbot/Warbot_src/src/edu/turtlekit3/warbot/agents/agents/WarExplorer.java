package edu.turtlekit3.warbot.agents.agents;

import java.util.HashMap;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.launcher.WarConfig;

public class WarExplorer extends MovableWarAgent {

	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarExplorer");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
	}
	
	public WarExplorer(Team team, WarExplorerAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		getBrainController().setBrain(new WarExplorerBrain(this));
	}

}
