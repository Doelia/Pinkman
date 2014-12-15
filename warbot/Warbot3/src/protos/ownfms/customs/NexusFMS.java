package protos.ownfms.customs;

import protos.Nexus;
import protos.ownfms.FMS;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarBase;

public class NexusFMS extends FMS<Nexus>  {
	
	

	private Task task;

	public NexusFMS(Nexus controller) {
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
		return WarBase.ACTION_IDLE;
	}
	
	protected Task getCurrentTask()
	{
		return this.task;
	}

}
