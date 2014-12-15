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
 * Lorsque l'agent perçoit une ou des bases enemies
 * @author BEUGNON
 *
 */
public class SeeEnemyBaseBehaviour extends TalkingBehaviour {

	
	private static SeeEnemyBaseBehaviour instance;

	@Override
	public String act(WarBrainController controller) {
		
		List<WarPercept> list = controller.getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		
		
		//Autant renvoyer une liste vide 
		//S'il vous plait je trouve cela sale 
		//de retourner des null
		//Même les agents sont d'accords avec moi !
		if(list!=null && list.size()>0)
		{
			for(WarPercept wp : list)
			{
				//Ceci envoie un message à tous les agents (dont le Nexus HeadQuarters) pour maintenir 
				//L'existence de la nourriture à cet position
				TargetMessageWrapper.wrapAndSendMessageToAll
				(ProtosCommunication.INFORM_ENEMY_BASE,controller.getBrain(), wp);
			}
		}
		
		return super.act(controller);
	}
	
	public static final SeeEnemyBaseBehaviour getInstance()
	{
		if(instance==null)
		{
			synchronized (SeeEnemyBaseBehaviour.class) {
				if(instance==null)
					instance = new SeeEnemyBaseBehaviour();
				
			}
		}
		return instance;
	}
}
