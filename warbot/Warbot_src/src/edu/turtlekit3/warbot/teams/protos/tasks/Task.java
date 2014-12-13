package edu.turtlekit3.warbot.teams.protos.tasks;

import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class Task
{	
	
	public boolean canExecute(WarBrainController wa)
	{
		return true;
	}
	
	
	public abstract ResultTask execute(WarBrainController wa);
	
}
