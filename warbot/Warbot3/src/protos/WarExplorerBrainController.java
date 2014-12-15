package protos;

import static protos.Reflexes.allReflexes;
import protos.role.ExplorerRole;
import protos.role.WarRole;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import protos.tasks.explorator.Collector;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class WarExplorerBrainController extends
		WarExplorerAbstractBrainController {

	private String toReturn;
	private String role;
	private Task currentTask;
	private CoordPolar target;
	

	

	public WarExplorerBrainController() {
		super();
		this.role =ExplorerRole.UNKNOWN;
		this.currentTask=Collector.getInstance();
	}

	@Override
	public String action() {
		resetAction();
		// REFLEXES
		setAction(allReflexes(this, WarAgentType.WarExplorer));
		
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
	
	public String getRole()
	{
		return this.role;
	}

	
	public void defaultAttitude()
	{
		setAction(WarExplorer.ACTION_IDLE);
	}
	
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public void setCurrentTask(Task t)
	{
		this.currentTask = t;	
	}
	
	private Task getCurrentTask() {
		return this.currentTask;
	}

	public boolean haveTarget() {
		return this.target!=null;
	}
	
	public void setTarget(CoordPolar cp)
	{
		this.target=cp;
	}
	
	public CoordPolar getTarget()
	{
		return this.target;
	}

	public WarRole getWarRole() {
		// TODO Auto-generated method stub
		return null;
	}

}