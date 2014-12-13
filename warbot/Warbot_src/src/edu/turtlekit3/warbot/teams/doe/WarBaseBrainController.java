package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.environement.EnvironnementUpdater;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	private EnvironnementUpdater eu = null;
	private Environnement e;
	
	public WarBaseBrainController() {
		super();
		Environnement.clear();
	}
	
	private void broadcastPosition() {
		getBrain().broadcastMessageToAll(Constants.here, "");
	}
	
	private Environnement getEnvironnement() {
		if (Behavior.CHEAT) {
			return Environnement.getInstance();
		} else {
			if (e == null) {
				e = new Environnement();
			}
			return e;
		}
	}
	
	@Override
	public String action() {
		
		if (eu == null) {
			eu = new EnvironnementUpdater(getEnvironnement());
		}
		
		eu.updateEnvironement(this.getBrain(), WarAgentType.WarBase);
		
		Environnement ev = this.getEnvironnement();
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if(percept.size() > 0) {
			ev.getTeamManager().setBaseAttacked(true);
		} else {
			ev.getTeamManager().setBaseAttacked(false);
		}
		
		ev.setMainBase(this.getBrain());
		if (ev.isMainBase(this.getBrain()))
			this.broadcastPosition();
		
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
 