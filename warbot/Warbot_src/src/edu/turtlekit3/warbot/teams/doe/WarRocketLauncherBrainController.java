package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

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

	int x, y;
	int angleModifier;
	boolean justTurned = false;
	String toReturn = "";
	int maxDistanceToTarget = 50;
	
	public WarRocketLauncherBrainController() {
		super();
		x = - new Random().nextInt() * 1500;
		y = - new Random().nextInt() * 1500;
		angleModifier = new Random().nextInt() * 180 - 90;
	}
	@Override
	public String action() {
		if(justTurned) {
			getBrain().setHeading(getBrain().getHeading() - 30);
		}
		justTurned = false;
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarRocketLauncher);
		
		toReturn = move();
		
		return toReturn;
	}
	
	public String attack() {
		ArrayList<WarPercept> e = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		Environnement ev = Environnement.getInstance();
		try {
			Team t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != getBrain().getID()) {
					//on tire sur la target du leader
					if(t.isAttacking()) {
						WarBrainUtils.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getTarget());
						toReturn = WarRocketLauncher.ACTION_FIRE;
						return toReturn;
					}
				} else {
					//on set la target de la team
					try {
						//WarPercept target = getBestTarget(e);
						int targetId = ev.getClosestEnemy(ev.getStructWarBrain(getBrain().getID()).getPosition());
						t.setAttacking(true);
						t.setTarget(ev.getEnemy(targetId).getPosition());
						toReturn = WarRocketLauncher.ACTION_FIRE;
						return toReturn;
					} catch (NoTargetFoundException e1) {
						t.setAttacking(false);
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
					if(ev.getEntitiesInRadiusOfWithAngle(this.getBrain().getID(), 20, 60, (int) getBrain().getHeading()).size() > 0) {
						justTurned = true;
						getBrain().setHeading(30 + getBrain().getHeading());
					}
				} else {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(-200, -200));
				}
			} catch (NotExistException e) {
			}

		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}

		return WarRocketLauncher.ACTION_MOVE;
	}
	
//	public WarPercept getBestTarget(ArrayList<WarPercept> e) throws NoTargetFoundException {
//		WarPercept target = null;
//		double maxDistance = maxDistanceToTarget;
//		int minHealth = 999999;
//		
//		for (WarPercept warPercept : e) {
//			if(warPercept.getDistance() < maxDistanceToTarget) {
//				if(warPercept.getDistance() < maxDistance) {
//					if(warPercept.getHealth() < minHealth) {
//						maxDistance = warPercept.getDistance();
//						minHealth = warPercept.getHealth();
//						target = warPercept;
//					}
//				}
//			}
//		}
//		if(target == null) {
//			throw new NoTargetFoundException();
//		}
//		return target;
//	}
}