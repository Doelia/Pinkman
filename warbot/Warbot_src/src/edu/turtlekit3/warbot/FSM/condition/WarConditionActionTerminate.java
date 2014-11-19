package edu.turtlekit3.warbot.FSM.condition;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.brains.WarBrain;

public class WarConditionActionTerminate extends WarCondition{
	
	WarAction action;
	
	public WarConditionActionTerminate(WarBrain brain, WarAction action){
		super(brain);
		this.action = action;
	}

	@Override
	public boolean isValide() {
		if(this.action.isTerminate())
			return true;
		else
			return false;
			
	}

}
