package protos.ownfms.behaviours;

import protos.WarExplorerBrainController;
import protos.communication.ProtosCommunication;
import protos.ownfms.AbstractBehaviour;
import protos.ownfms.conditions.MessageCondition;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;

public class RequestFoodBehaviour extends AbstractBehaviour<WarExplorerBrainController> {

	
	//Personal Message condition
	private static MessageCondition foodRequestCondtion = 
			new MessageCondition(ProtosCommunication.REQUEST_FOOD_IMMEDIATLY);
	
	private static RequestFoodBehaviour instance;
	
	
	public static RequestFoodBehaviour getInstance()
	{
		if(instance==null)
			synchronized (RequestFoodBehaviour.class) {
				if(instance==null)
					instance = new RequestFoodBehaviour();
			}
		return instance;
	}
	
	
	
	@Override
	protected void buildConditions() {
		addCondition(NotEmptyBagCondition.getInstance());//Static condition
		addCondition(foodRequestCondtion);
	}

	@Override
	public String act(WarExplorerBrainController controller) {

		MessageCondition mc = (MessageCondition) getConditions().get(0);
		mc.getTarget();
		if(mc.getTarget().getDistance() < WarExplorer.MAX_DISTANCE_GIVE)
		{
			controller.getBrain().setIdNextAgentToGive(mc.getIDTarget());
			return ControllableWarAgent.ACTION_GIVE;
		}
		else
		{
			//Empêche les agents de faire de longues distances pour venir donner de la nourriture
			//à une requête
			if(mc.getTarget().getDistance() <= 3 * WarExplorer.MAX_DISTANCE_GIVE)
			{
				controller.getBrain().setHeading(mc.getTarget().getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}
		}
		
		return null;
	}

}
