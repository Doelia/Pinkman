package edu.turtlekit3.warbot.teams.fsm;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;

public class WarEngineerBrainController extends WarEngineerAbstractBrainController {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarExplorer.ACTION_MOVE;
	}
}
