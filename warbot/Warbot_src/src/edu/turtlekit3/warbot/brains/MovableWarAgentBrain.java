package edu.turtlekit3.warbot.brains;

import edu.turtlekit3.warbot.agents.MovableWarAgent;

public abstract class MovableWarAgentBrain extends WarBrain {
		
	public MovableWarAgentBrain(MovableWarAgent agent) {
		super(agent);
	}

	public boolean isBlocked() {
		return ((MovableWarAgent) _agent).isBlocked();
	}
	
}
