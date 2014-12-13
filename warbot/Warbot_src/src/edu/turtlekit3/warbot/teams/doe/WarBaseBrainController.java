package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	public WarBaseBrainController() {
		super();
		Environnement.clear();
	}
	
	private void broadcastPosition() {
		getBrain().broadcastMessageToAll(Constants.here, "");
	}
	
	@Override
	public String action() {
		
		
		if (Environnement.CHEAT) {
			WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarBase);
		}
		
		if (Environnement.CHEAT) {
			Environnement ev = Environnement.getInstance();
			ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
			if(percept.size() > 0) {
				ev.getTeamManager().setBaseAttacked(true);
			} else {
				ev.getTeamManager().setBaseAttacked(false);
			}
		}
		
		
		if (Environnement.CHEAT) {
			Environnement.getInstance().setMainBase(this.getBrain());
			if (Environnement.getInstance().isMainBase(this.getBrain()))
				this.broadcastPosition();
		} else {
			this.broadcastPosition();
		}
		
		this.getBrain().setDebugString("Bag "+this.getBrain().getNbElementsInBag()+"/"+this.getBrain().getBagSize()+" - life "+this.getBrain().getHealth());
		
		if (this.getBrain().getHealth() == 12000 && this.getBrain().isBagFull()) {
			this.getBrain().setNextAgentToCreate(WarAgentType.WarRocketLauncher);
			return WarBase.ACTION_CREATE;
		}
	
		if (this.getBrain().getHealth() < 12000)
			return WarBase.ACTION_EAT;
		else
			return WarBase.ACTION_IDLE;
	}
}
 