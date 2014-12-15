package protos.ownfms.behaviours;

import protos.ownfms.AbstractBehaviour;
import edu.turtlekit3.warbot.brains.WarBrainController;


public abstract class TalkingBehaviour<T extends WarBrainController> extends AbstractBehaviour<T> {

	@Override
	protected void buildConditions() 
	{
		//No need of condition for theses 
		//We can still add Conditions in subclasses
	}

	@Override
	public String act(T controller) {
		//Always return null
		return null;
	}

}
