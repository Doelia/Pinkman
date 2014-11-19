package edu.turtlekit3.warbot.brains.capacities;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public interface CreatorBrain {

	public void setNextAgentToCreate(WarAgentType agent);
	public WarAgentType getNextAgentToCreate();
	public boolean isAbleToCreate(WarAgentType agent);	
	
}
