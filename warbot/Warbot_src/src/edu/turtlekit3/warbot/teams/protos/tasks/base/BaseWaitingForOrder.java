package edu.turtlekit3.warbot.teams.protos.tasks.base;

import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class BaseWaitingForOrder extends Task {

	private static BaseWaitingForOrder instance;

	public static Task getInstance() {

		if (instance == null) {
			synchronized (BaseWaitingForOrder.class) {
				instance = new BaseWaitingForOrder();
			}
		}
		return instance;
	}
	
	

	@Override
	public ResultTask execute(WarBrainController wa) {
		return new ResultTask(this, WarBase.ACTION_IDLE);
	}

}
