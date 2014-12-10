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
	int x = new Random().nextInt() * 500;
	int y = new Random().nextInt() * 500;
	String toReturn = "";
	int maxDistanceToTarget = 50;
	
	public WarRocketLauncherBrainController() {
		super();
		angleModifier = new Random().nextInt() * 180 - 90;
	}
	@Override
	public String action() {
		
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarRocketLauncher);
		
		toReturn = move();
		toReturn = attack();
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
						WarBrainUtils.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getTarget());
						return WarRocketLauncher.ACTION_FIRE;
					}
				} else {
					//on set la target de la team
					try {
						//WarPercept target = getBestTarget(e);
						int targetId = ev.getClosestEnemy(ev.getStructWarBrain(getBrain().getID()).getPosition());
						t.setAttacking(true);
						System.out.println("enemy found, starting to attack");
						t.setTarget(ev.getEnemy(targetId).getPosition());
						return WarRocketLauncher.ACTION_FIRE;
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
					if(getBrain().isBlocked()) {
						getBrain().setHeading(90 + getBrain().getHeading());
					}
				} else {
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