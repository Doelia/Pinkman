package edu.turtlekit3.warbot.teams.protos;

import static edu.turtlekit3.warbot.teams.protos.Reflexes.allReflexes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.teams.protos.tasks.base.InitBase;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class WarBaseBrainController extends WarBaseAbstractBrainController
{
	
	public enum BaseRole
	{
		UNKNOWN, FACTORY, SAFE_BRANCH, HQ;
		
		public boolean isUnknown()
		{
			return equals(UNKNOWN);
		}
	}
	
	public class Stats
	{
		private Map<WarAgentType, Integer> stats;
		
		private List<CoordPolar> enemyBases;
		
		private List<CoordPolar> destroyedBases;
		
		private List<CoordPolar> foodLocalisations;
		
		public Stats()
		{
			this.stats = new HashMap<>();
			for (WarAgentType wat : WarAgentType.values())
				this.stats.put(wat, 0);
			
			this.enemyBases = new ArrayList<>();
			this.destroyedBases = new ArrayList<>();
			this.foodLocalisations = new ArrayList<>();
		}
		
		public void addTypeToStats(WarAgentType senderType)
		{
			int i = this.stats.get(senderType).intValue();
			i++;
			this.stats.put(senderType, i);
		}
		
		public void addEnemyBaseCoord(CoordPolar cp)
		{
			if (!enemyBases.contains(cp) || !destroyedBases.contains(cp))
			{
				enemyBases.add(cp);
			}
		}
		
		public void addDestroyedBaseCoord(CoordPolar cp)
		{
			enemyBases.remove(cp);
			destroyedBases.add(cp);
		}
		
		public void resetStats()
		{
			this.stats = new HashMap<>();
			for (WarAgentType wat : WarAgentType.values())
				this.stats.put(wat, 0);
		}
		
		public void resetAll()
		{
			this.stats = new HashMap<>();
			for (WarAgentType wat : WarAgentType.values())
				this.stats.put(wat, 0);
		}
		
		public void addPossibleFoodCoord(CoordPolar cp)
		{
			if (!this.foodLocalisations.contains(cp))
				this.foodLocalisations.add(cp);
		}
		
		public void removePossibleFoodCoord(CoordPolar cp)
		{
			this.foodLocalisations.remove(cp);
		}
		
		public CoordPolar getNearEnemyBase()
		{
			int id = 0;
			for (int i = 1; i < this.enemyBases.size(); i++)
			{
				if (this.enemyBases.get(id).getDistance() > enemyBases.get(i)
						.getDistance())
					id = i;
			}
			return enemyBases.get(id);
		}
		
		public boolean haveEnemyBaseTarget()
		{
			return !this.enemyBases.isEmpty();
		}
		
	}
	
	public class ContractManager
	{
		
	}
	
	private WarAgentType lastCreateUnit = null;
	
	private String toReturn;
	
	private Task currentTask;
	
	private Stats stats;
	
	private BaseRole brole;
	
	private int HQ = -1;
	
	private static final int MIN_HEATH_TO_CREATE = (int) (WarBase.MAX_HEALTH * 0.8);
	
	public WarBaseBrainController()
	{
		super();
		stats = null;
		brole = BaseRole.UNKNOWN;
		currentTask = InitBase.getInstance();
		boolean firstTick = true;
		
	}
	
	public Task getCurrentTask()
	{
		return this.currentTask;
	}
	
	@Override
	public String action()
	{
		resetAction();
		// REFLEXES
		setAction(allReflexes(this, WarAgentType.WarBase));
		
		if (haveAction())
			return getAction();
		
		// HQ SECURITY
		if (getRole().equals(BaseRole.SAFE_BRANCH))
			securiseHQ();
		
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
	
	private void securiseHQ()
	{
		// TODO SI LE HQ NE REPOND PAS PENDANT X TICKS IL DEVIENT LE HQ
		
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
	
	public BaseRole getRole()
	{
		return this.brole;
	}
	
	private void handlerHQCommunication()
	{
		getBrain().getMessages();
	}
	
	public void defaultAttitude()
	{
		setAction(WarBase.ACTION_IDLE);
	}
	
	public boolean haveHQDefined()
	{
		return HQ != -1;
	}
	
	public void setHQ(int senderID)
	{
		HQ = senderID;
	}
	
	public void setRole(BaseRole role)
	{
		this.brole = role;
	}
	
	public void setCurrentTask(Task t)
	{
		this.currentTask = t;
		
	}
	
	public boolean isHQ()
	{
		return this.HQ == getBrain().getID();
	}
	
	public Stats getStats()
	{
		return this.stats;
	}
	
	public boolean haveStats()
	{
		return this.stats != null;
	}
	
	public void startStats()
	{
		this.stats = new Stats();
	}
	
	public Object getContractManager()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
