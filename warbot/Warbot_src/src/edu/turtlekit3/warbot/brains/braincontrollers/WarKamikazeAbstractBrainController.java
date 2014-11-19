package edu.turtlekit3.warbot.brains.braincontrollers;

import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarKamikazeBrain;

public abstract class WarKamikazeAbstractBrainController extends WarBrainController {
	
	public WarKamikazeAbstractBrainController() {
		super();
	}

	@Override
	public WarKamikazeBrain getBrain() {
		return (WarKamikazeBrain) _brain;
	}

}
