package edu.turtlekit3.warbot.agents.agents;

import java.util.HashMap;
import java.util.logging.Level;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.capacities.Agressive;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.brains.braincontrollers.WarTurretAbstractBrainController;
import edu.turtlekit3.warbot.brains.brains.WarTurretBrain;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public class WarTurret extends ControllableWarAgent implements Agressive {

	public static final double ANGLE_OF_VIEW;
	public static final double HITBOX_RADIUS;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final int	TICKS_TO_RELOAD;
	
	private boolean _reloaded;
	private boolean _reloading;
	private int _tickLeftBeforeReloaded;
	
	static {
		HashMap<String, String> data = WarConfig.getConfigOfControllableWarAgent("WarTurret");
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		HITBOX_RADIUS = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_HITBOX_RADIUS));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_BAG_SIZE));
		TICKS_TO_RELOAD = Integer.valueOf(data.get(WarConfig.AGENT_CONFIG_TICKS_TO_RELOAD));
	}
	
	public WarTurret(Team team, WarTurretAbstractBrainController brainController) {
		super(ACTION_IDLE, team, HITBOX_RADIUS, brainController, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE);
		
		getBrainController().setBrain(new WarTurretBrain(this));
		_tickLeftBeforeReloaded = TICKS_TO_RELOAD;
		_reloaded = false;
		_reloading = true;
	}
	
	@Override
	protected void doOnEachTick() {
		_tickLeftBeforeReloaded--;
		if (_tickLeftBeforeReloaded <= 0 && _reloading) {
			_reloaded = true;
			_reloading = false;
		}
		super.doOnEachTick();
	}
	
	@Override
	public String fire() {
		logger.log(Level.FINEST, this.toString() + " firing...");
		if (isReloaded()) {
			logger.log(Level.FINER, this.toString() + " fired.");
			launchAgent(new WarRocket(getTeam(), this));
			_reloaded = false;
		}
		return getBrainController().action();
	}

	@Override
	public String beginReloadWeapon() {
		logger.log(Level.FINEST, this.toString() + " begin reload weapon.");
		if (! _reloading) {
			_tickLeftBeforeReloaded = TICKS_TO_RELOAD;
			_reloading = true;
		}
		return getBrainController().action();
	}

	@Override
	public boolean isReloaded() {
		return _reloaded;
	}

	@Override
	public boolean isReloading() {
		return _reloading;
	}

}
