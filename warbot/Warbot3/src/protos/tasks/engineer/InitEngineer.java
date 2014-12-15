package protos.tasks.engineer;

import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.brains.WarBrainController;
import protos.tasks.ResultTask;
import protos.tasks.Task;

public class InitEngineer extends Task {

	@Override
	public ResultTask execute(WarBrainController wa) {

		return new ResultTask(DoStar.getInstance(),WarEngineer.ACTION_IDLE);
	}

}
