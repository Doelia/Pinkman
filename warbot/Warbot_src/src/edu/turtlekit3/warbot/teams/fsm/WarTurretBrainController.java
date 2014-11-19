package edu.turtlekit3.warbot.teams.fsm;

import edu.turtlekit3.warbot.agents.agents.WarTurret;
import edu.turtlekit3.warbot.brains.braincontrollers.WarTurretAbstractBrainController;

public class WarTurretBrainController extends WarTurretAbstractBrainController {
	
	public WarTurretBrainController() {
		super();
	}

	@Override
	public String action() {
		return WarTurret.ACTION_IDLE;
	}
}
