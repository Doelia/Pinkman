package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.environnement.Environnement;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	public WarBaseBrainController() {
		super();
	}
	
	private void broadcastPosition() {
		getBrain().broadcastMessageToAll(Constants.here, "");
	}
	
	@Override
	public String action() {
		
		Environnement.getInstance().setMainBase(this.getBrain());
		
		// Develop behaviour here
		
		if (Environnement.getInstance().isMainBase(this.getBrain()))
				this.broadcastPosition();
		
		return WarBase.ACTION_IDLE;
	}
}
 