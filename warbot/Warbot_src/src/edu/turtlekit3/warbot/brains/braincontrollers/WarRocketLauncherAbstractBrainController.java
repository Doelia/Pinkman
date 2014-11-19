package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarRocketLauncherBrain;

public abstract class WarRocketLauncherAbstractBrainController extends WarBrainController {

	public WarRocketLauncherAbstractBrainController() {
		super();
	}

	@Override
	public WarRocketLauncherBrain getBrain() {
		return (WarRocketLauncherBrain) _brain;
	}
	
}