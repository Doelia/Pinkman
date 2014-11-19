package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarEngineerBrain;

public abstract class WarEngineerAbstractBrainController extends WarBrainController {
	
	public WarEngineerAbstractBrainController() {
		super();
	}

	@Override
	public WarEngineerBrain getBrain() {
		return (WarEngineerBrain) _brain;
	}
}
