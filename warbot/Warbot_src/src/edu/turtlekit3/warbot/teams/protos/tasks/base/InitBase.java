package edu.turtlekit3.warbot.teams.protos.tasks.base;

import java.util.List;

import edu.turtlekit3.warbot.teams.protos.WarBaseBrainController;
import edu.turtlekit3.warbot.teams.protos.WarBaseBrainController.BaseRole;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class InitBase extends Task {

	private static InitBase instance;

	@Override
	public ResultTask execute(WarBrainController wa) {
		WarBaseBrainController cont = (WarBaseBrainController) wa;
		ResultTask rt = null;
		// look for message
		if (!cont.isHQ() && !cont.haveHQDefined()) {
			//cherche le HQ
			List<WarMessage> l = cont.getBrain().getMessages();
			for (WarMessage wm : l) {
				if (wm.getMessage().equals(
						ProtosCommunication.INFORM_HQ_DECLARED)) {
					//TROUVER HQ
					cont.setHQ(wm.getSenderID());
					cont.setRole(BaseRole.FACTORY);
					cont.getBrain().reply(wm,
							ProtosCommunication.INFORM_MY_TYPE);
					break;
				}
			}
		}

		//PAS TROUVE HQ
		if (!cont.haveHQDefined()) {
			//BECOME HQ
			cont.getBrain().broadcastMessageToAll(
					ProtosCommunication.INFORM_HQ_DECLARED);
			cont.setRole(BaseRole.HQ);
//			cont.getBrain().requestRole("HQ", "HQ");
			// EXECUTE LA TACHE COMMAND AU PROCHAIN TICK
			rt = new ResultTask(Command.getInstance(),
					WarBase.ACTION_IDLE);
			cont.setHQ(cont.getBrain().getID());
		} else {
			//CHERCHE UNE SAFE BRANCH
			List<WarMessage> l = cont.getBrain().getMessages();
			boolean found = false;
			for (WarMessage wm : l) {
				if (wm.getMessage().equals(
						ProtosCommunication.INFORM_SAFE_BRANCH)) {
					found = true;
					break;
				}
			}
			//PAS TROUVEE UNE SAFE BRANCH
			if (!found) {
				//DEVIENT UNE SAFE BRANCH
				cont.setRole(BaseRole.SAFE_BRANCH);
				cont.getBrain().broadcastMessageToAgentType(
						WarAgentType.WarBase,
						ProtosCommunication.INFORM_SAFE_BRANCH);
			}

		}
		if(rt==null)
			rt = new ResultTask(BaseWaitingForOrder.getInstance(),
					WarBase.ACTION_IDLE);

		return rt;
	}

	public static Task getInstance() {
		if (instance == null) {
			synchronized (InitBase.class) {
				if (instance == null) {
					instance = new InitBase();
				}
			}
		}
		return instance;
	}

}
