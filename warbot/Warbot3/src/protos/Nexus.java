package protos;

import java.util.ArrayList;
import java.util.List;

import protos.headquarters.WarCharts;
import protos.ownfms.AbstractBehaviour;
import protos.ownfms.FMS;
import protos.ownfms.behaviours.SelfHealBehaviour;
import protos.ownfms.behaviours.UrgentHealBehaviour;
import protos.ownfms.behaviours.ping.NexusPingBehaviour;
import protos.ownfms.behaviours.talking.SeeEnemyUnitsBehaviour;
import protos.ownfms.behaviours.talking.SeeFoodBehaviour;
import protos.ownfms.customs.NexusFMS;
import protos.role.BaseRole;
import protos.role.WarRole;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;

public class Nexus extends WarBaseAbstractBrainController
{
	
	private WarRole role;

	private FMS fms;

	private WarCharts charts;
	
	public static final int MIN_HEATH_TO_CREATE = (int) (WarBase.MAX_HEALTH * 0.8);
	
	private static List<AbstractBehaviour<WarBrainController>> nexusBehaviours;
	
	static
	{//Produce a static ordered list of behaviour for all Nexus instances
		nexusBehaviours = new ArrayList<>();
		nexusBehaviours.add(new UrgentHealBehaviour( (int) ((double)WarBase.MAX_HEALTH * 0.5)));
		nexusBehaviours.add(new SelfHealBehaviour(MIN_HEATH_TO_CREATE));
	}
	
	
	public Nexus()
	{
		super();
		setWarRole(BaseRole.iUNKNOWN);
		fms = new NexusFMS(this);
		//Talking behaviours
		fms.addWarBehaviour(NexusPingBehaviour.getInstance());
		fms.addWarBehaviour(SeeFoodBehaviour.getInstance());
		fms.addWarBehaviour(SeeEnemyUnitsBehaviour.getInstance());
		//Action behaviours
		fms.addWarBehaviours(nexusBehaviours);
		
	}
	
	public FMS getFMS()
	{
		return this.fms;
	}
	
	@Override
	public String action()
	{
		return getFMS().execute();
		/* OLD
		getBrain().setDebugString("start:"+getCurrentTask().getClass().getName());
		resetAction();
		// REFLEXES
		setAction(allReflexes(this, WarAgentType.WarBase));

		if (haveAction())
		{	getBrain().setDebugString("have Reflex"+brole+" "+getAction());
			return getAction();
		}
		getBrain().setDebugString("have no Reflex"+brole+" "+getAction());
		// HQ SECURITY
		if (getRole().equals(BaseRole.SAFE_BRANCH))
			securiseHQ();

		//PING WITH ROLE
		getBrain().broadcastMessageToAll(ProtosCommunication.PING, getRole().toString());
		
		// FMS
		
		if (getCurrentTask() != null && getCurrentTask().canExecute(this))
		{
			getBrain().setDebugString("have task"+brole+" "+getCurrentTask().getClass().getName());
			ResultTask rt = getCurrentTask().execute(this);
			setCurrentTask(rt.getNextTask());
			setAction(rt.getAction());
		}
		
		// FACTORY
		if (!haveAction())
			defaultAttitude();
		
		return getAction();*/
	}
	
	
	public WarRole getWarRole()
	{
		return role;	
	}
	
	public void setWarRole(BaseRole baseRole)
	{
		role= baseRole;
	}

	public WarCharts getCharts() {
		return this.charts;
	}

	public boolean haveCharts() {
		
		return this.charts==null;
	}

	public void startCharts() {
		this.charts = new WarCharts();
	}
	
}
