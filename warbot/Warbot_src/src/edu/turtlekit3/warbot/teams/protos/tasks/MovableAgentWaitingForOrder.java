package edu.turtlekit3.warbot.teams.protos.tasks;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class MovableAgentWaitingForOrder extends Task {

	private static MovableAgentWaitingForOrder instance;

	public static Task getInstance() {

		if (instance == null) {
			synchronized (MovableAgentWaitingForOrder.class) {
				instance = new MovableAgentWaitingForOrder();
			}
		}
		return instance;
	}
	
	

	@Override
	public ResultTask execute(WarBrainController wa) {
		// TODO LOOK FOR CONTRACT OR ROLE DEFINED
		return new ResultTask(this, ControllableWarAgent.ACTION_IDLE);
	}

}
