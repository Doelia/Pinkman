package protos.tasks.base;

import protos.Nexus;
import protos.communication.ProtosCommunication;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public class FactoryRoutine extends Task<Nexus> {

	private static FactoryRoutine instance;

	public static Task getInstance() {

		if (instance == null) {
			synchronized (FactoryRoutine.class) {
				instance = new FactoryRoutine();
			}
		}
		return instance;
	}
	
	

	@Override
	public ResultTask execute(Nexus wa) {
		
		if(wa.getBrain().getHealth() > wa.MIN_HEATH_TO_CREATE)
		{
			wa.getBrain().setNextAgentToCreate(WarAgentType.WarRocketLauncher);
		}
		else
		{
			wa.getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, ProtosCommunication.ASK_FOR_FOOD);
		}
		
		return new ResultTask(this, WarBase.ACTION_IDLE);
	}

}
