package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.environnement.WarBrainUtils;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	public WarExplorerBrainController() {
		super();
	}


	@Override
	public String action() {
		WarBrainUtils.updatePositionInEnvironnement(this.getBrain());

		return WarExplorer.ACTION_MOVE;
	}
}

