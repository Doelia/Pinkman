package protos.tasks;

import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class Task<T extends WarBrainController>
{	
	
	public boolean canExecute(T wa)
	{
		return true;
	}
	
	
	public abstract ResultTask execute(T wa);
	
}
