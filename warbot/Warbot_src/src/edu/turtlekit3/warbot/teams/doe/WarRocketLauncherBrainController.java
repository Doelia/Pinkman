package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.clean.Group;
import edu.turtlekit3.warbot.teams.doe.clean.Tools;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTeamFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x = new Random().nextInt(1600);
	int y = new Random().nextInt(1000);
	String toReturn = "";
	int maxDistanceToTarget = 50;

	public WarRocketLauncherBrainController() {
		super();
	}
	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarRocketLauncher);

		toReturn = move();
		toReturn = attack();
		return toReturn;
	}

	public String attack() {
		//si la team rencontre quelqu'un
		//elle le met en target et l'entour
		//s'ils n'ont pas rechargé
		//ils wiggle

		if(!getBrain().isReloaded() && !getBrain().isReloading()) {
			return WarRocketLauncher.ACTION_RELOAD;
		}

		Environnement ev = Environnement.getInstance();
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		percept.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase));
		// Je un agentType dans le percept

		Group t;
		try {
			t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			int leader = t.getLeader();
			if(t.isBaseAttacked()) {
				if(!getBrain().isReloaded()) {
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getDefensePosition(getBrain().getID()));
					return WarRocketLauncher.ACTION_MOVE;
				} else {
					if(percept.size() > 0) {
						getBrain().setHeading(percept.get(0).getAngle());
						return WarRocketLauncher.ACTION_FIRE;
					}
				}
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
		Environnement ev = Environnement.getInstance();
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
					if(n > 95) {
						x = new Random().nextInt(200);
						y = new Random().nextInt(200);
					}
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(-x, -y));
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
