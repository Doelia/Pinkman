package protos;

import static protos.Reflexes.allReflexes;
import protos.role.RocketLauncherRole;
import protos.role.WarRole;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import protos.tasks.rocketlauncher.RocketLauncherRoutine;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;


public class WarRocketLauncherBrainController extends
		WarRocketLauncherAbstractBrainController {

	private String toReturn = null;
	
	private Task currentTask;

	private String brole;

	private WarRole role;
	
	public WarRole getWarRole()
	{
		return role;	
	}
	
	public void setWarRole(RocketLauncherRole baseRole)
	{
		role= baseRole;
	}
	public WarRocketLauncherBrainController() {
		currentTask= RocketLauncherRoutine.getInstance();
	}
	
	@Override
	public void activate() 
	{
		
	};

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

	public String getRole()
	{
		return brole;
	}

	
	
	

}