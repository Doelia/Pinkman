package edu.turtlekit3.warbot.agents.projectiles;

import java.util.HashMap;

import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.agents.WarProjectile;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarDeathRocket extends WarProjectile {
	
	public static final double HITBOX_RADIUS;
	public static final double SPEED;
	public static final double EXPLOSION_RADIUS;
	public static final int AUTONOMY;
	public static final int DAMAGE;
	public static final double RANGE;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfWarProjectile("WarDeathRocket");
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		SPEED = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_SPEED));
		EXPLOSION_RADIUS = Double.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_EXPLOSION_RADIUS));
		AUTONOMY = Integer.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_AUTONOMY));
		DAMAGE = Integer.valueOf(data.get(WarConfig.PROJECTILE_CONFIG_DAMAGE));		
		RANGE = SPEED * AUTONOMY;
	}

	
	public WarDeathRocket(Team team, WarAgent sender) {
		super(ACTION_MOVE, team, HITBOX_RADIUS, sender, SPEED, EXPLOSION_RADIUS, DAMAGE, AUTONOMY);
	}
}
