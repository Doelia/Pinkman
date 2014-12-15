package pinkman;

import pinkman.behavior.Behavior;
import pinkman.environement.Environnement;
import pinkman.messages.EnvironnementUpdaterInterface;
import pinkman.messages.ReceiverEnvironementInstruction;
import pinkman.messages.SenderEnvironnementInstruction;
import pinkman.tasks.DetectEnemyTask;
import pinkman.tasks.SendAlliesTask;
import pinkman.tasks.SetBaseAttackedTask;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	private Environnement e;
	private EnvironnementUpdaterInterface sender;
	private ReceiverEnvironementInstruction receiver;
	
	private static int MAX_BASES = 5;

	public WarBaseBrainController() {
		super();
	}

	private void broadcastPosition() {
		getBrain().broadcastMessageToAll("HERE", "");
	}

	private EnvironnementUpdaterInterface getSender() {
		if (sender == null) {
			if (Behavior.CHEAT) {
				sender = this.getEnvironnement();
			} else {
				sender = new SenderEnvironnementInstruction(this.getBrain());
			}
		}
		return sender;
	}
	
	private Environnement getEnvironnement() {
		if (Behavior.CHEAT) {
			e = Behavior.getGoodInstance(this.getBrain());
			receiver = new ReceiverEnvironementInstruction(e);
			return e;
		} else {
			if (e == null) {
				e = new Environnement();
				receiver = new ReceiverEnvironementInstruction(e);
			}
			return e;
		}
	}

	private boolean canCreate() {

		int nbrBases = getEnvironnement().getNumberOfType(WarAgentType.WarBase);

		if (nbrBases > MAX_BASES && this.getBrain().getID() == getEnvironnement().getBiggestBaseId()) {
			return true;
		}

		if (WarBase.MAX_HEALTH < 80000) {
			return (this.getBrain().getHealth() >= WarBase.MAX_HEALTH);
		} else {
			return (this.getBrain().getHealth() >= 80000);
		}

	}

	public WarAgentType getNextToCreate() {
		if (this.getEnvironnement().getNumberOfType(WarAgentType.WarExplorer) < 6 || this.getEnvironnement().getNumberOfType(WarAgentType.WarRocketLauncher) > 20) {
			return WarAgentType.WarExplorer;
		}
		return WarAgentType.WarRocketLauncher;
	}

	@Override
	public String action() {
		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarBase;

		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e, getBrain().getMessages()).exec();
		new SetBaseAttackedTask(this, t, e).exec();
		
		this.receiver.processMessages(this.getBrain());

		e.setMainBase(this.getBrain());
		if (e.isMainBase(this.getBrain())) {
			this.broadcastPosition();
			this.getSender().decrementTtlOfAll();
		}

		this.getBrain().setDebugString("Bag "+this.getBrain().getNbElementsInBag()+"/"+this.getBrain().getBagSize()+" - life "+this.getBrain().getHealth());

		if (canCreate()) {
			this.getBrain().setNextAgentToCreate(this.getNextToCreate());
			return WarBase.ACTION_CREATE;
		}

		if (this.getBrain().getHealth() < WarBase.MAX_HEALTH)
			return WarBase.ACTION_EAT;
		else
			return WarBase.ACTION_IDLE;
	}
}
