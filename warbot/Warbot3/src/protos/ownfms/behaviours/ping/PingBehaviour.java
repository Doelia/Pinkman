package protos.ownfms.behaviours.ping;

import protos.communication.ProtosCommunication;
import protos.ownfms.AbstractBehaviour;
import protos.role.WarRole;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class PingBehaviour<T extends WarBrainController> extends AbstractBehaviour<T> 
{

	@Override
	protected void buildConditions() {
		//NONE EXCEPT BE ALIVE
	}
	
	@Override
	public String act(T controller) {
		
		//TODO OPTIMIZE IT BY USING GROUP Bases ROLE HEADQUARTERS
		controller.getBrain().broadcastMessageToAgentType
		(WarAgentType.WarBase, ProtosCommunication.PING, getRole(controller).getRoleName());
		
		return null;
	}
	
	public abstract WarRole getRole(T controller);
	
	
}
