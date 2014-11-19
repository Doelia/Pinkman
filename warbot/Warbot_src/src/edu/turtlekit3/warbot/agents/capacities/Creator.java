package edu.turtlekit3.warbot.agents.capacities;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public interface Creator {

	public static String ACTION_CREATE = "create";
	
	public String create();

	public boolean isAbleToCreate(WarAgentType agent);
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate);
	public WarAgentType getNextAgentToCreate();
}
