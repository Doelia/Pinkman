package edu.turtlekit3.warbot.teams.protos.tasks.explorator;

import edu.turtlekit3.warbot.teams.protos.WarExplorerBrainController;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class InitExplorer extends Task {

	private static Task instance;

	public static Task getInstance()
	{
		if (instance == null) {
			synchronized (Explore.class) {
				if (instance == null) {
					instance = new InitExplorer();
				}
				
				
			}
		}
		return instance;
	}
	
	@Override
	public ResultTask execute(WarBrainController wa) {
		
		WarExplorerBrainController webc = (WarExplorerBrainController) wa;
		
		
		for(WarMessage wm : webc.getBrain().getMessages())
		{
			if(wm.getMessage().equals(ProtosCommunication.INFORM_GIVE_ROLE))
			{
				
			}
		}
		
		
		return new ResultTask(this,WarExplorer.ACTION_IDLE);
	}

}
