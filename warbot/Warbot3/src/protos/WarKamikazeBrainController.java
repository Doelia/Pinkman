package protos;

import static protos.Reflexes.allReflexes;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import protos.tasks.kamikaze.GoKillYourselfOnEnemyBase;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarKamikazeAbstractBrainController;

public class WarKamikazeBrainController extends WarKamikazeAbstractBrainController {
	
	private String toReturn;
	private Task currentTask;
	private int chronoExplosion = -1;

	public WarKamikazeBrainController() {
		super();
		currentTask = GoKillYourselfOnEnemyBase.getInstance();
		toReturn = null;
		chronoExplosion = 10;
	}

	@Override
	public String action() {

		 // le temp avec d'exploser 
		
		resetAction();
		// REFLEXES
		setAction(allReflexes(this, WarAgentType.WarKamikaze));
		
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
	
	public int getChrono () {
		return chronoExplosion;
	}
	
	public void deCreChrono(){
		chronoExplosion--;
	}
	
	public void initChrono () {
		chronoExplosion = 3;
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