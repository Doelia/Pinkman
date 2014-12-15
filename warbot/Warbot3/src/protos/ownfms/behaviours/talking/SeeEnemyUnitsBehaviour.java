package protos.ownfms.behaviours.talking;

import java.util.List;

import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import protos.ownfms.behaviours.TalkingBehaviour;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class SeeEnemyUnitsBehaviour extends TalkingBehaviour<WarBrainController> 
{
	private static SeeEnemyUnitsBehaviour instance;

	public static SeeEnemyUnitsBehaviour getInstance() {
		if(instance==null)
		{
			synchronized (SeeEnemyUnitsBehaviour.class) {
				if(instance==null)
					instance = new SeeEnemyUnitsBehaviour();
			}
		}
		return instance;
	}
	
	@Override
	public String act(WarBrainController controller) {
		List<WarPercept> l = controller.getBrain().getPerceptsEnemies();
		if (l != null && l.size()>0) {
			for (WarPercept wp : l) {
				//Ceci envoie un message au Nexus pour maintenir 
				//L'existence de la nourriture à cet position
				TargetMessageWrapper.wrapAndSendMessageToAgentType(
						ProtosCommunication.INFORM_FOUND_ENEMY,
						controller.getBrain(), wp,WarAgentType.WarBase,WarAgentType.WarTurret);
				;
			}
		}
		return super.act(controller);
	}
}
