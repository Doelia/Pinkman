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

	int x = new Random().nextInt(800);
	int y = new Random().nextInt(400);
	String toReturn = "";
	int maxDistanceToTarget = 50;
	private double lastAngle = 0;
	private int angleModifier = new Random().nextInt(90);
	private boolean justShot = false;

	public WarRocketLauncherBrainController() {
		super();
		System.out.println("x : " + x);
		System.out.println("y : " + y);
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

		//		getBrain().setHeading(getBrain().getHeading() - lastAngle);
		//		lastAngle = 0;

		Environnement ev = Environnement.getInstance();
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		percept.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase));
		// Je un agentType dans le percept

		Group t;
		try {
			t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			if(percept != null && percept.size() > 0){
				t.setAttacking(true);
				int leader = t.getLeader();
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
				if(new Random().nextBoolean()) {
					//					return WarRocketLauncher.ACTION_IDLE;
				}
			}
			if(getBrain().isBlocked()) {
				getBrain().setHeading((new Random().nextBoolean())?1:1 * 90 + getBrain().getHeading());
			}
		} catch (Exception e){};
		//				if(leader != getBrain().getID()) {
		//					if(getBrain().isReloaded()) {
		//						ArrayList<WarPercept> allies = getBrain().getPerceptsAlliesByType(WarAgentType.WarRocketLauncher);
		//						if(percept.size() > 0 && allies.size() > 0) {
		//							
		//							getBrain().setHeading(percept.get(0).getAngle());
		//						} else {
		////							Tools.setHeadingOn(
		////									getBrain(), 
		////									ev.getStructWarBrain(getBrain().getID()).getPosition(),
		////									ev.getStructWarBrain(t.getLeader()).getPosition());
		//							getBrain().setHeading(getBrain().getHeading() - 90);
		////							return WarRocketLauncher.ACTION_IDLE;
		//						}
		//						return WarRocketLauncher.ACTION_FIRE;
		//					}
		//					Tools.setHeadingOn(
		//							getBrain(), 
		//							ev.getStructWarBrain(getBrain().getID()).getPosition(),
		//							t.getBattlePosition(getBrain().getID()));
		//					if(getBrain().isBlocked()) {
		//						getBrain().setHeading((new Random().nextBoolean())?1:-1 * 90 + getBrain().getHeading());
		//					}
		//					return WarRocketLauncher.ACTION_MOVE;
		//				} else {
		//					t.setLeaderCanShoot(false);
		//					return WarRocketLauncher.ACTION_IDLE;
		////					if(getBrain().isReloaded()) {
		////						System.out.println("in");
		////						if(justShot) {
		////							t.setLeaderCanShoot(false);
		////							justShot = !justShot;
		////							return WarRocketLauncher.ACTION_IDLE;
		////						} else {
		////							t.setLeaderCanShoot(true);
		////							justShot = !justShot;
		////							return WarRocketLauncher.ACTION_FIRE;
		////						}
		////					} else {
		////						System.out.println("out");
		////					}
		//				}
		//			}
		//
		//		} catch (NoTeamFoundException e) {} catch (NotExistException e) {
		//		}


		//
		//			//je le dit aux autres
		//			Group t;
		//			try {
		//				t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
		//				t.setAttacking(true);
		//				t.setTarget(Tools.getPositionOfEntityFromMine(ev.getStructWarBrain(getBrain().getID()).getPosition(), (float) percept.get(0).getAngle(), (float) percept.get(0).getDistance()));
		//			} catch (NoTeamFoundException | NotExistException e) {}
		//
		//			if(getBrain().isReloaded()){
		//				getBrain().setHeading(percept.get(0).getAngle());
		//				toReturn = WarRocketLauncher.ACTION_FIRE;
		//			}else{
		//				getBrain().setHeading(percept.get(0).getAngle() + angleModifier);
		//				lastAngle = angleModifier;
		//				toReturn = WarRocketLauncher.ACTION_MOVE;
		//			}
		//		}else{
		//			//si j'ai un message me disant qu'il y a  un autre tank a tuer
		//			Group t;
		//			try {
		//				t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
		//				t.setAttacking(false);
		//				if(t.isAttacking()) {
		//					Tools.setHeadingOn(
		//							getBrain(), 
		//							ev.getStructWarBrain(getBrain().getID()).getPosition(),
		//							t.getTarget());
		//						}
		//					} catch (NoTeamFoundException | NotExistException e) {}

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
						x = new Random().nextInt(1000);
						y = new Random().nextInt(600);
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
