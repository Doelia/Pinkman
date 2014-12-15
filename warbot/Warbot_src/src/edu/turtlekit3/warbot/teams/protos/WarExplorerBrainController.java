package edu.turtlekit3.warbot.teams.protos;

import static edu.turtlekit3.warbot.teams.protos.Reflexes.allReflexes;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.teams.protos.tasks.explorator.Collector;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class WarExplorerBrainController extends
		WarExplorerAbstractBrainController {

	private String toReturn;
	private ExplorerRole brole;
	private Task currentTask;
	private CoordPolar target;
	
	public enum ExplorerRole
	{
		UNKNOWN,EXPLORER,LEADER,COLLECTOR,DELIVROR;
	}
	
	

	public WarExplorerBrainController() {
		super();
		this.brole =ExplorerRole.UNKNOWN;
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
	
	public ExplorerRole getRole()
	{
		return this.brole;
	}

	
	public void defaultAttitude()
	{
		setAction(WarExplorer.ACTION_IDLE);
	}
	
	public void setRole(ExplorerRole role)
	{
		this.brole = role;
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

}