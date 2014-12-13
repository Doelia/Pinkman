package edu.turtlekit3.warbot.teams.protos.tasks.explorator;

import java.util.List;

import edu.turtlekit3.warbot.teams.protos.WarExplorerBrainController;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class Explore extends Task
{
	
	private static Task instance;
	
	private Explore()
	{
		super();
	}

	@Override
	public ResultTask execute(WarBrainController wa)
	{
		WarExplorerBrainController w = (WarExplorerBrainController) wa;
		w.getBrain().setDebugString("Task:explore");
		List<WarPercept> lwp = w.getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		
		if(lwp!=null && !lwp.isEmpty())
		{
			//TODO TO CHANGE
			w.getBrain().setHeading(lwp.get(0).getAngle()-90);
			
				
			return new ResultTask(this, WarExplorer.ACTION_MOVE);
		}
		else
		{
			if(w.haveTarget())
			{
				w.getBrain().setHeading(w.getTarget().getAngle());
			}
			else
			{
				//TODO CHERCHE TARGET DANS MESSAGE
			}
		}
		
		if(w.getBrain().isBlocked())
		{
			w.getBrain().setHeading(w.getBrain().getHeading()+45);
		}
		return new ResultTask(this,WarExplorer.ACTION_MOVE);
	}

	public static Task getInstance()
	{
		if (instance == null) {
			synchronized (Explore.class) {
				if (instance == null) {
					instance = new Explore();
				}
			}
		}
		return instance;
	}
	
}
