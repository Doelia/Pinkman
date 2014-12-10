package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.Team;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTargetFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTeamFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int angleModifier;
	int x = new Random().nextInt(800);
	int y = new Random().nextInt(400);
	String toReturn = "";
	int maxDistanceToTarget = 50;

	public WarRocketLauncherBrainController() {
		super();
		angleModifier = new Random().nextInt() * 180 - 90;
		System.out.println("x : " + x);
		System.out.println("y : " + y);
	}
	@Override
	public String action() {

		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarRocketLauncher);

		toReturn = move();
		toReturn = attack();
		if(toReturn.equals(WarRocketLauncher.ACTION_FIRE)) {
			System.out.println("attacking");
		}
		return toReturn;
	}

	public String attack() {
		if(!getBrain().isReloaded()) {
			return WarRocketLauncher.ACTION_RELOAD;
		}
		Environnement ev = Environnement.getInstance();
		try {
			Team t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != getBrain().getID()) {
					//on tire sur la target du leader
					if(t.isAttacking()) {
						System.out.println("angle before : " + this.getBrain().getHeading());
						WarBrainUtils.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getTarget());
						//						System.out.println(t.getTarget());
						//						System.out.println("angle after : " + this.getBrain().getHeading());
						ArrayList<WarPercept> e = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
						if(e.size() > 0) {
							getBrain().setHeading(e.get(0).getAngle());
							return WarRocketLauncher.ACTION_FIRE;
						}
//						if(t.getTarget().dst(ev.getStructWarBrain(getBrain().getID()).getPosition()) > 50) {
//							return WarRocketLauncher.ACTION_MOVE;
//						} else {
//							return WarRocketLauncher.ACTION_FIRE;
//						}
					}
				} else {
					ArrayList<WarPercept> e = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
					if(e.size() > 0) {
						WarPercept target = e.get(0);
						t.setAttacking(true);
						t.setTarget(WarBrainUtils.getPositionOfEntityFromMine(ev.getStructWarBrain(getBrain().getID()).getPosition(), (float) target.getAngle(), (float) target.getDistance()));
						getBrain().setHeading(target.getAngle());
						return WarRocketLauncher.ACTION_FIRE;
					} else {
						t.setAttacking(false);
					}
					//on set la target de la team
					//					try {
					//						//WarPercept target = getBestTarget(e);
					//						int targetId = ev.getClosestEnemy(ev.getStructWarBrain(getBrain().getID()).getPosition());
					//						if(targetId > 0) {
					//							t.setAttacking(true);
					//							t.setTarget(ev.getEnemy(targetId).getPosition());
					//							System.out.println(ev.getEnemy(targetId).getPosition());
					//							WarBrainUtils.setHeadingOn(
					//									getBrain(), 
					//									ev.getStructWarBrain(getBrain().getID()).getPosition(),
					//									t.getTarget());
					//							if(t.getTarget().dst(ev.getStructWarBrain(getBrain().getID()).getPosition()) > 30) {
					//								return WarRocketLauncher.ACTION_MOVE;
					//							}
					//							return WarRocketLauncher.ACTION_FIRE;
					//						} else {
					//							t.setAttacking(false);
					//						}
					//					} catch (NoTargetFoundException e1) {
					//						t.setAttacking(false);
					//					}
				}
			} catch (NotExistException e1) {
			}

		} catch (NoTeamFoundException e1) {
		}

		return toReturn;
	}

	public String move() {
		Environnement ev = Environnement.getInstance();
		try {
			Team t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != this.getBrain().getID()) {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getMovementPosition(getBrain().getID()));
					if(getBrain().isBlocked()) {
						getBrain().setHeading(90 + getBrain().getHeading());
					}
				} else {
					int n = new Random().nextInt(100);
					if(n > 95) {
						x = new Random().nextInt(1000);
						y = new Random().nextInt(600);
					}
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(-x, -y));
					if(getBrain().isBlocked()) {
						getBrain().setHeading(90 + getBrain().getHeading());
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