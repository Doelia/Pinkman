package protos.ownfms.behaviours;

import protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class UrgentHealBehaviour extends SelfHealBehaviour {

	public UrgentHealBehaviour(int healthThreshold) {
		super(healthThreshold);
	}


	@Override
	public String act(WarBrainController controller) 
	{
		if (!controller.getBrain().isBagEmpty())
		{
			return ControllableWarAgent.ACTION_EAT;
		}
		controller.getBrain().broadcastMessageToAll(ProtosCommunication.REQUEST_FOOD_IMMEDIATLY);
		return null;
	}


}
