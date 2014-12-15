package protos.ownfms.behaviours;

import protos.communication.ProtosCommunication;
import protos.ownfms.AbstractBehaviour;
import protos.ownfms.conditions.LifeCondition;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.brains.WarBrainController;


/**
 * Réflexe de survie 
 * @author BEUGNON
 *
 */
public class SelfHealBehaviour extends AbstractBehaviour<WarBrainController>{

	
	private int healthThreshold;
	
	public SelfHealBehaviour(int healthThreshold)
	{
		super();
		this.healthThreshold=healthThreshold;
		this.buildConditions();
	}
	
	
	@Override
	public String act(WarBrainController controller) 
	{
		if (!controller.getBrain().isBagEmpty())
		{
			return ControllableWarAgent.ACTION_EAT;
		}
		controller.getBrain().broadcastMessageToAll(ProtosCommunication.ASK_FOR_FOOD);
		return null;
	}


	@Override
	protected void buildConditions() 
	{
		addCondition(new LifeCondition(healthThreshold,LifeCondition.INF_OR_EQUALS));
	}

}
