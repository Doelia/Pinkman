package edu.turtlekit3.warbot.FSM.Reflexe;

import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;

public class WarReflexeWarnWithCondition extends WarReflexe{

	WarCondition condition;
	String message;
	WarAgentType agentType;
	
	public WarReflexeWarnWithCondition(WarBrain b, WarCondition condition, WarAgentType agentType, String message) {
		super(b, "Reflexe warn wit condition");
		this.condition = condition;
		this.message = message;
		this.agentType = agentType;
	}

	@Override
	public String executeReflexe() {
		if(this.condition.isValide()){
			getBrain().broadcastMessageToAgentType(this.agentType, this.message, "");
		}
		return null;
	}

}
