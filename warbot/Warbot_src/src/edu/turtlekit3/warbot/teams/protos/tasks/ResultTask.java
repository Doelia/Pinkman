package edu.turtlekit3.warbot.teams.protos.tasks;

public class ResultTask {

	private Task nextTask;
	private String action;
	
	
	public ResultTask(Task nextTask, String action)
	{
		this.nextTask = nextTask;
		this.action = action;
	}
	
	public Task getNextTask()
	{
		return this.nextTask;
	}
	
	public String getAction()
	{
		return this.action;
	}
}
