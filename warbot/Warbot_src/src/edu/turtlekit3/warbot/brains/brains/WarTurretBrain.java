package edu.turtlekit3.warbot.brains.brains;

import edu.turtlekit3.warbot.agents.agents.WarTurret;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.capacities.AgressiveBrain;

public class WarTurretBrain extends WarBrain implements AgressiveBrain {
	
	public WarTurretBrain(WarTurret agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarTurret) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarTurret) _agent).isReloading();
	}
}
