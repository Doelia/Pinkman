package edu.turtlekit3.warbot.teams.protos;

import static edu.turtlekit3.warbot.teams.protos.Reflexes.allReflexes;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.teams.protos.tasks.engineer.DoStar;

public class WarEngineerBrainController extends
		WarEngineerAbstractBrainController {

	public enum EngineerRole {
		UNKNOWN, DEFENSER, ATTACKER;

		public boolean isUnknown() {
			return equals(UNKNOWN);
		}
	}
	
	private String toReturn;

	/**
	 * La vie minimum qu'il faut à un engénieur pour créer des turrets.
	 * Avec 50 % permet de créer 2 tourrets et de lui lesser 33% de ca vie
	 * car la création d'un turret lui coute 33%
	 */
	private static final int MIN_HEATH_TO_CREATE = (int) (WarEngineer.MAX_HEALTH * 0.5); 
	
	/**
	 * Permet de ne créer qu'une seul turret à la fois
	 */
	private boolean lockTurret = false; 

	/**
	 * Permet de bloquer dans l'état : retour base
	 */
	private boolean lockReturnBase = false;

	//private int tick = 0;
	
	/**
	 * au bout de combien de temp il doit revenir
	 */
	private int returnTick = 10; // modif ce temps par la base ?
	
	/**
	 * quel angle pour l'étoile 
	 */
	
	private double nextAngleForContinue = 90;
	
	/**
	 * le tick courant
	 */
	private int currentTick = 0;

	private int HQ;
	
	/**
	 * id de la base ou le contrat est en cour
	 */
	private int baseId;
	
	private Task currentTask;
	
	private EngineerRole brole;

	public WarEngineerBrainController() {
		super();
		currentTask=DoStar.getInstance();
		brole= EngineerRole.DEFENSER;
	}

	@Override
	public String action() {
		
		resetAction();
		//REFLEXES
		setAction(allReflexes(this, WarAgentType.WarEngineer));
		
		if (haveAction())
			return getAction();
		
		//FMS
		if (getCurrentTask() != null && 
				getCurrentTask().canExecute(this)) {
			ResultTask rt = getCurrentTask().execute(this);
			setCurrentTask(rt.getNextTask());
			setAction(rt.getAction());
		}

		//FACTORY
		if (!haveAction())
			defaultAttitude();

		return getAction();

	}

	private Task getCurrentTask() {
		return this.currentTask;
	}

	/**
	 * Permet à un engénieur de ce soigner
	 */
	
	private void healMySelf() {
		getBrain().getHealth();
		toReturn = MovableWarAgent.ACTION_TAKE;
	}

	private boolean haveAction() {
		return this.toReturn != null;
	}

	private void resetAction() {
		this.toReturn = null;
	}

	private String getAction() {
		return this.toReturn;
	}

	private void setAction(String action) {
		this.toReturn = action;
	}

	public EngineerRole getRole() {
		return this.brole;
	}

	public void defaultAttitude() {
		setAction(MovableWarAgent.ACTION_IDLE);
	}

	public boolean haveHQDefined() {
		return HQ != -1;
	}

	public void setHQ(int senderID) {
		HQ = senderID;
	}

	public void setRole(EngineerRole role) {
		this.brole = role;
	}

	public void setCurrentTask(Task t) {
		this.currentTask = t;

	}

	public boolean isLockTurret() {
		return lockTurret;
	}

	public void setLockTurret(boolean lockTurret) {
		this.lockTurret = lockTurret;
	}

	public boolean isLockReturnBase() {
		return lockReturnBase;
	}

	public void setLockReturnBase(boolean lockReturnBase) {
		this.lockReturnBase = lockReturnBase;
	}
	
	public int getReturnTick() {
		return returnTick;
	}
	
	public void addTick() {
		this.currentTick++;
	}

	public void setReturnTick(int returnTick) {
		this.returnTick = returnTick;
	}

	public int getCurrentTick() {
		return currentTick;
	}

	public void setCurrentTick(int currentTick) {
		this.currentTick = currentTick;
	}
	public void resetCurrentTick() {
		this.currentTick = 0;
	}

	public int getMinHeathToCreate() {
		return MIN_HEATH_TO_CREATE;
	}

	public double getNextAngleForContinue() {
		return nextAngleForContinue;
	}

	public void setNextAngleForContinue(double nextAngleForContinue) {
		this.nextAngleForContinue = nextAngleForContinue;
	}

	public int getBaseId() {
		return baseId;
	}

	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}
	
	
	
	
	
	

}
