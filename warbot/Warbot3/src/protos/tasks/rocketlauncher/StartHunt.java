package protos.tasks.rocketlauncher;

import protos.WarRocketLauncherBrainController;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;

public class StartHunt extends Task<WarRocketLauncherBrainController> {

	private static StartHunt instance;

	@Override
	public ResultTask execute(WarRocketLauncherBrainController wa) {
		//LOOK FOR TARGET IN WARPERCEPT
		
		/*
		 
		 if(foundTarget())
		 {
		 //SWITCH TO TASK
		 }
		 else
		 {
		 //LOOK FOR TARGET IN MESSAGE
		 }
		 
		 */
		
		return new ResultTask(this, WarRocketLauncher.ACTION_MOVE);
	}
	
	public static Task getInstance()
	{
		if(instance==null)
			synchronized (StartHunt.class) {
				if(instance==null)
					instance = new StartHunt();
				
			}
		return instance;
	}

}
