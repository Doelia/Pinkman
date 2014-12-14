package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.tasks.DetectEnemyTask;
import edu.turtlekit3.warbot.teams.doe.tasks.SendAlliesTask;
import edu.turtlekit3.warbot.teams.doe.tasks.SetBaseAttackedTask;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

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
	
	private boolean healhIsSufisant() {
		
		if (WarBase.MAX_HEALTH < 80000) {
			return (this.getBrain().getHealth() >= WarBase.MAX_HEALTH && this.getBrain().isBagFull());
		} else {
			return (this.getBrain().getHealth() >= 80000);
		}
		
	}
	
	public WarAgentType getNextToCreate() {
		if (e.getNumberOfType(WarAgentType.WarExplorer) < 6) {
			return WarAgentType.WarExplorer;
		}
		return WarAgentType.WarRocketLauncher;
	}
	
	@Override
	public String action() {
		
		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarBase;
		
		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e).exec();
		new SetBaseAttackedTask(this, t, e).exec();
		
		e.setMainBase(this.getBrain());
		if (e.isMainBase(this.getBrain()))
			this.broadcastPosition();
		
		this.getBrain().setDebugString("Bag "+this.getBrain().getNbElementsInBag()+"/"+this.getBrain().getBagSize()+" - life "+this.getBrain().getHealth());
		
		if (healhIsSufisant()) {
			this.getBrain().setNextAgentToCreate(this.getNextToCreate());
			return WarBase.ACTION_CREATE;
		}
	
		if (this.getBrain().getHealth() < WarBase.MAX_HEALTH)
			return WarBase.ACTION_EAT;
		else
			return WarBase.ACTION_IDLE;
	}
}
 