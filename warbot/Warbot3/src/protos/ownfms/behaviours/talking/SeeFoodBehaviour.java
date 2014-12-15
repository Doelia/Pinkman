package protos.ownfms.behaviours.talking;

import java.util.List;

import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import protos.ownfms.behaviours.TalkingBehaviour;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

/**
 * Réflexe de discussion 
 * Lorsque l'agent perçoit de la nourriture
 * @author BEUGNON
 *
 */
public class SeeFoodBehaviour extends TalkingBehaviour {

	
	private static SeeFoodBehaviour instance;

	@Override
	public String act(WarBrainController controller) {
		List<WarPercept> l = controller.getBrain().getPerceptsResources();
		if (l != null && l.size()>0) {
			for (WarPercept wp : l) {
				//Ceci envoie un message au Nexus pour maintenir 
				//L'existence de la nourriture à cet position
				TargetMessageWrapper.wrapAndSendMessageToAgentType(
						ProtosCommunication.INFORM_FOUND_FOOD,
						controller.getBrain(), wp, WarAgentType.WarBase);
				;
			}
		}
		return super.act(controller);
	}

	public static SeeFoodBehaviour getInstance() {
		if(instance==null)
		{
			synchronized (SeeFoodBehaviour.class) {
				if(instance==null)
					instance = new SeeFoodBehaviour();
			}
		}
		return instance;
	}
}
