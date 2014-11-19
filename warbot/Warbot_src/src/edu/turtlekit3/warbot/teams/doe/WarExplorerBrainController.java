package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {
	
	public WarExplorerBrainController() {
		super();
	}

	@Override
	public String action() {
		// Develop behaviour here
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		
		this.getBrain().setHeading(this.getBrain().getHeading()+1);
		
		return WarExplorer.ACTION_MOVE;
	}
}