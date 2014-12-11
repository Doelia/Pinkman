package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.game.Game;
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
		if(!getBrain().isReloaded() || !getBrain().isReloading()) {
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
						//						WarBrainUtils.setHeadingOn(
						//								getBrain(), 
						//								ev.getStructWarBrain(getBrain().getID()).getPosition(),
						//								t.getTarget());
						//						System.out.println(t.getTarget());
						//						System.out.println("angle after : " + this.getBrain().getHeading());
						if(!getBrain().isReloaded()) {
							WarBrainUtils.setHeadingOn(
									getBrain(), 
									ev.getStructWarBrain(getBrain().getID()).getPosition(),
									t.getBattlePosition(getBrain().getID()));
							//							if(getBrain().isBlocked()) {
							//								int n = (new Random().nextBoolean())?1:-1;
							//								getBrain().setHeading(90 * n + getBrain().getHeading());
							//							}
							return WarRocketLauncher.ACTION_MOVE;
						}
						ArrayList<WarPercept> e = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
						if(e.size() > 0) {
							t.setAttacking(true);
							getBrain().setHeading(e.get(0).getAngle());
							return WarRocketLauncher.ACTION_FIRE;
						} else {
							t.setAttacking(false);
						}
					}
				} else {
					if(!t.isAttacking()) {
						try {
							Integer target = Environnement.getInstance().getClosestEnemy(ev.getStructWarBrain(getBrain().getID()).getPosition());
							WarBrainUtils.setHeadingOn(
									getBrain(), 
									ev.getStructWarBrain(getBrain().getID()).getPosition(),
									ev.getEnemy(target).getPosition());
						} catch (Exception e1) {
							
						}
					}
					ArrayList<WarPercept> e = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
					if(e.size() > 0) {
						WarPercept target = e.get(0);
						t.setAttacking(true);
						t.setTarget(WarBrainUtils.getPositionOfEntityFromMine(ev.getStructWarBrain(getBrain().getID()).getPosition(), (float) target.getAngle(), (float) target.getDistance()));
						getBrain().setHeading(target.getAngle());
						return WarRocketLauncher.ACTION_FIRE;
					} else {
						t.setAttacking(false);
//						WarBrainUtils.setHeadingOn(
//								getBrain(), 
//								ev.getStructWarBrain(getBrain().getID()).getPosition(),
//								t.getBattlePosition(getBrain().getID()));
					}
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