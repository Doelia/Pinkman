package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;

public abstract class WarExplorerAbstractBrainController extends WarBrainController {

	public WarExplorerAbstractBrainController() {
		super();
	}

	@Override
	public WarExplorerBrain getBrain() {
		return (WarExplorerBrain) _brain;
	}

}
