package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;

public abstract class WarBaseAbstractBrainController extends WarBrainController {

	public WarBaseAbstractBrainController() {
		super();
	}

	@Override
	public WarBaseBrain getBrain() {
		return (WarBaseBrain) _brain;
	}

}