package protos.ownfms;

import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class AbstractCondition<T extends WarBrainController> 
{
	
	
	public AbstractCondition()
	{
	}


	public abstract boolean isAvailable(WarBrainController controller);
	
}
