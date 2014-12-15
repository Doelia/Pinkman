package edu.turtlekit3.warbot.teams.protos;

import static edu.turtlekit3.warbot.teams.protos.Reflexes.allReflexes;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.teams.protos.tasks.rocketlauncher.AttackBase;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;


public class WarRocketLauncherBrainController extends
		WarRocketLauncherAbstractBrainController {

	private String toReturn = null;
	
	private Task currentTask;

	public WarRocketLauncherBrainController() {
		currentTask= AttackBase.getInstance();
	}

	@Override
	public String action() {
		resetAction();
		// REFLEXES
		setAction(allReflexes(this, WarAgentType.WarRocketLauncher));
		
		if (haveAction())
			return getAction();
		
		
		// FMS
		
		if (getCurrentTask() != null && getCurrentTask().canExecute(this))
		{
			ResultTask rt = getCurrentTask().execute(this);
			setCurrentTask(rt.getNextTask());
			setAction(rt.getAction());
		}
		
		// FACTORY
		if (!haveAction())
			defaultAttitude();
		
		return getAction();
	}
	
	private boolean haveAction()
	{
		return this.toReturn != null;
	}
	
	private void resetAction()
	{
		this.toReturn = null;
	}
	
	private String getAction()
	{
		return this.toReturn;
	}
	
	private void setAction(String action)
	{
		this.toReturn = action;
	}
	

	
	public void defaultAttitude()
	{
		setAction(WarBase.ACTION_IDLE);
	}
	
	
	public void setCurrentTask(Task t)
	{
		this.currentTask = t;	
	}
	
	private Task getCurrentTask() {
		return this.currentTask;
	}

	
	
	

}