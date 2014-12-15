package protos.tasks.explorator;

import protos.WarEngineerBrainController.EngineerRole;
import protos.WarExplorerBrainController;
import protos.communication.ProtosCommunication;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import protos.tasks.engineer.DoStar;
import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
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
				if(wm.getContent()[0].equals(EngineerRole.TURRET_PRESSION.toString()))
				{
					//mettre des tourelles dans le vide de la map pour faire pression
					//ou autour d'une base enemie
					//TODO A CHANGER
					return new ResultTask(DoStar.getInstance(), WarExplorer.ACTION_IDLE);
				}
				else if(wm.getContent()[0].equals(EngineerRole.GUARDIAN.toString()))
				{
					//mettre des tourelles près des bases
					return new ResultTask(DoStar.getInstance(), WarExplorer.ACTION_IDLE);
				}
			}
		}
		
		webc.getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, 
				ProtosCommunication.INFORM_WITHOUT_ROLE);
		
		return new ResultTask(this,WarExplorer.ACTION_IDLE);
	}

}
