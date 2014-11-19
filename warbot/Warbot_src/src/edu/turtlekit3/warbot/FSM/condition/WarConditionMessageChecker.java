package edu.turtlekit3.warbot.FSM.condition;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.brains.WarRocketLauncherBrain;
import edu.turtlekit3.warbot.communications.WarMessage;

public class WarConditionMessageChecker extends WarCondition{
	
	WarAgentType agentType;
	String message;

	public WarConditionMessageChecker(WarRocketLauncherBrain brain,
			WarAgentType agentType, String message) {
		super(brain);
		
		this.message = message;
		this.agentType = agentType;
	}

	@Override
	public boolean isValide() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getMessage().equals(this.message) && m.getSenderType().equals(agentType)){
				return true;
			}
		}
		return false;
	}

}
