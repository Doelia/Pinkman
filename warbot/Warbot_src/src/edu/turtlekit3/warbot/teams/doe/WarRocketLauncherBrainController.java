package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.BaseNotFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTeamFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;
import edu.turtlekit3.warbot.teams.doe.tasks.DetectEnemyTask;
import edu.turtlekit3.warbot.teams.doe.tasks.SendAlliesTask;
import edu.turtlekit3.warbot.teams.doe.teams.Group;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x;
	int y;
	String toReturn = "";
	int maxDistanceToTarget = 50;
	int isOnTop;
	int lastBaseFoundId = -1;
	
	private Environnement e;

	public WarRocketLauncherBrainController() {
		super();
		newPosition();
		isOnTop = 0;
	}
	
	private Environnement getEnvironnement() {
		if (Behavior.CHEAT) {
			return Environnement.getInstance();
		} else {
			if (e == null) {
				e = new Environnement();
			}
			return e;
		}
	}
	
	@Override
	public String action() {
		
		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarRocketLauncher;
		
		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e).exec();

		try {
			boolean top = getEnvironnement().getWeAreInTop();
			isOnTop = ((top)?-1:1);
		} catch (BaseNotFoundException ex) {}

		toReturn = move();
		toReturn = attack();
		return toReturn;
	}

	public void newPosition() {
		if(isOnTop == 0) {
			x = new Random().nextInt(3200) - 1600;
			y = new Random().nextInt(2000) - 1000;
		}
		else {
			x = new Random().nextInt(1600) * isOnTop;
			y = new Random().nextInt(1000) * isOnTop;
		}
	}

	public String attack() {
		//si la team rencontre quelqu'un
		//elle le met en target et l'entour
		//s'ils n'ont pas rechargé
		//ils wiggle

		if(!getBrain().isReloaded() && !getBrain().isReloading()) {
			return WarRocketLauncher.ACTION_RELOAD;
		}

		Environnement ev = this.getEnvironnement();
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		percept.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase));
		// Je un agentType dans le percept

		Group t;
		try {
			t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			int leader = t.getLeader();

			if(!ev.killedFirstBase()) {
				if(ev.oneBaseIsFound()) {
					ArrayList<WarPercept> p = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
					if(p.size() > 0) {
						lastBaseFoundId = p.get(0).getID();
						if(!getBrain().isReloaded()) {
							Tools.setHeadingOn(
									getBrain(), 
									ev.getStructWarBrain(getBrain().getID()).getPosition(),
									t.getBaseAttackPosition(getBrain().getID()));
							if(getBrain().isBlocked()) {
								getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
							}
							return WarRocketLauncher.ACTION_MOVE;
						} else {
							getBrain().setHeading(p.get(0).getAngle());
							return WarRocketLauncher.ACTION_FIRE;
						}
					} else {
						if(lastBaseFoundId >= 0) {
							ev.voteToKillBase(lastBaseFoundId);
							if(ev.getFirstEnemyBase() != lastBaseFoundId) {
								lastBaseFoundId = -1;
							}
						}
						Tools.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getBaseAttackPosition(getBrain().getID()));
						if(getBrain().isBlocked()) {
							getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
						}
						return WarRocketLauncher.ACTION_MOVE;
					}
				} else {

					try {
						if(getBrain().getID() == leader) {
							Vector2 enemyBasePosition = ev.getApproxEnemyBasePosition();
							System.out.println(enemyBasePosition);
							System.out.println("my position : " + ev.getStructWarBrain(getBrain().getID()).getPosition());
							Tools.setHeadingOn(
									getBrain(), 
									ev.getStructWarBrain(getBrain().getID()).getPosition(),
									enemyBasePosition);
							if(getBrain().isBlocked()) {
								getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
							}
							return WarRocketLauncher.ACTION_MOVE;
						}

					} catch (BaseNotFoundException e) {};
				}
			}

			if(t.isBaseAttacked() && getBrain().getID() == leader) {
				Tools.setHeadingOn(
						getBrain(), 
						ev.getStructWarBrain(getBrain().getID()).getPosition(),
						t.getDefensePosition(leader));
			}
			if(percept != null && percept.size() > 0){
				t.setAttacking(true);
				if(getBrain().getID() == leader) {
					t.setTarget(ev.getEnemy(ev.getClosestEnemy(ev.getStructWarBrain(getBrain().getID()).getPosition())).getPosition(), 0);
					//					t.setTarget(Tools.getPositionOfEntityFromMine(ev.getStructWarBrain(getBrain().getID()).getPosition(), percept.get(0).getAngle(), percept.get(0).getDistance()), (int) percept.get(0).getAngle());
				}
			} else {
				t.setAttacking(false);
			}
			if(t.isAttacking() ) {
				if(!getBrain().isReloaded()) {
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getTargetPosition(getBrain().getID()));
				} else {
					if(percept.size() > 0) {
						getBrain().setHeading(percept.get(0).getAngle());
						return WarRocketLauncher.ACTION_FIRE;
					}
				}
			}
			if(getBrain().isBlocked()) {
				getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
			}
		} catch (Exception e){};

		return toReturn;
	}

	public String move() {
		Environnement ev = this.getEnvironnement();
		try {
			Group t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != this.getBrain().getID()) {
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getMovementPosition(getBrain().getID()));
					if(getBrain().isBlocked()) {
						getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
					}
				} else {
					int n = new Random().nextInt(100);
					if(n > 95 || getBrain().isBlocked()) {
						newPosition();
					}
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(x, y));
					if(getBrain().isBlocked()) {
						getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
					}
				}
			} catch (NotExistException e) {
			}

		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}

		return WarRocketLauncher.ACTION_MOVE;
	}
}
