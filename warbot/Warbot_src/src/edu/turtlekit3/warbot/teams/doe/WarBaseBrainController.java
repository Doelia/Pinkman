package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.clean.Tools;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	public WarBaseBrainController() {
		super();
	}
	
	private void broadcastPosition() {
		getBrain().broadcastMessageToAll(Constants.here, "");
	}
	
	@Override
	public String action() {
		
		if (Tools.CHEAT) {
			Environnement.getInstance().setMainBase(this.getBrain());
			if (Environnement.getInstance().isMainBase(this.getBrain()))
				this.broadcastPosition();
		} else {
			this.broadcastPosition();
		}
		
		if (this.getBrain().isBagFull()) {
			this.getBrain().setNextAgentToCreate(WarAgentType.WarRocketLauncher);
			return WarBase.ACTION_CREATE;
		}
		
		return WarBase.ACTION_IDLE;
	}
}
 