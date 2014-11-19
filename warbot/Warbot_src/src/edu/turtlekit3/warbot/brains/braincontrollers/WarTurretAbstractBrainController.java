package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarTurretBrain;

public abstract class WarTurretAbstractBrainController extends WarBrainController {
	
	public WarTurretAbstractBrainController() {
		super();
	}

	@Override
	public WarTurretBrain getBrain() {
		return (WarTurretBrain) _brain;
	}

}
