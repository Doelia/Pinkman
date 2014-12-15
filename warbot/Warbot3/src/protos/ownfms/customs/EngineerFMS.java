package protos.ownfms.customs;

import protos.WarEngineerBrainController;
import protos.ownfms.FMS;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;

public class EngineerFMS extends FMS<WarEngineerBrainController>  {
	
	private Task task;

	public EngineerFMS(WarEngineerBrainController controller) {
		super(controller);
		this.task = getController().getWarRole().getFirstTask();
	}

	@Override
	protected String applyCustomAgentReasoning() {
		if(haveCurrentTask())
		{
			ResultTask rt = getCurrentTask().execute(getController());
			setCurrentTask(rt.getNextTask());
			return rt.getAction();
		}
		else
			return null;
	}

	protected void setCurrentTask(Task nextTask) {
		this.task=nextTask;
	}

	protected boolean haveCurrentTask() {
		return getCurrentTask()!=null;
	}

	@Override
	protected String defaultAction() {
		if(!getController().getBrain().isBlocked())
			getController().getBrain().setRandomHeading();
		return WarRocketLauncher.ACTION_MOVE;
	}
	
	protected Task getCurrentTask()
	{
		return this.task;
	}

}