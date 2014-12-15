package doe;

import java.util.ArrayList;
import java.util.Random;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.badlogic.gdx.math.Vector2;

import doe.behavior.Behavior;
import doe.environement.Environnement;
import doe.exceptions.BaseNotFoundException;
import doe.exceptions.NoTeamFoundException;
import doe.exceptions.NotExistException;
import doe.messages.ReceiverEnvironementInstruction;
import doe.tasks.DetectEnemyTask;
import doe.tasks.SendAlliesTask;
import doe.teams.Group;
import doe.tools.Tools;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x;
	int y;
	String toReturn = "";
	int maxDistanceToTarget = 50;
	int isOnTop;
	int angleToUnstuck;
	private int angleModifier = new Random().nextInt(90);
	private int ticksSinceLastEncounter;
	private ArrayList<WarMessage> messages;
	private ArrayList<WarPercept> enemyBases;
	private ArrayList<WarPercept> enemies;

	private Environnement e;
	private ReceiverEnvironementInstruction receiver;

	public WarRocketLauncherBrainController() {
		super();
		newPosition();
		isOnTop = 0;
		angleToUnstuck = new Random().nextInt(360);
		if(new Random().nextBoolean()) {
			angleModifier = -angleModifier;
		}
		toReturn = "";
		ticksSinceLastEncounter = 0;
		messages = new ArrayList<WarMessage>();
	}

	private Environnement getEnvironnement() {
		if (Behavior.CHEAT) {
			e = Behavior.getGoodInstance(this.getBrain());
			receiver = new ReceiverEnvironementInstruction(e);
		}
		else {
			if (e == null) {
				e = new Environnement();
				receiver = new ReceiverEnvironementInstruction(e);
			}
		}
		return e;
	}

	@Override
	public String action() {
		if(!getBrain().isReloaded() && !getBrain().isReloading()) {
			return WarRocketLauncher.ACTION_RELOAD;
		}
		ticksSinceLastEncounter++;
		this.messages = getBrain().getMessages();
		
		enemyBases = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		
		enemies = getBrain().getPerceptsEnemiesByType(WarAgentType.WarTurret);
		enemies.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher));
		enemies.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarKamikaze));
		enemies.addAll(getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase));


		Environnement e = this.getEnvironnement();

		WarAgentType t = WarAgentType.WarRocketLauncher;

		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e, this.messages).exec();
		
		this.receiver.processMessages(this.getBrain());

		try {
			boolean top = getEnvironnement().getWeAreInTop();
			isOnTop = ((top)?-1:1);
		} catch (BaseNotFoundException ex) {}

		if(getBrain().isBlocked()) {
			return unstuck();
		}
		
		toReturn = move();
		toReturn = attack();
//		
//		Group g;
//		try {
//			g = getEnvironnement().getTeamManager().getTeamOf(this.getBrain().getID());
//			int leader = g.getLeader();
//			if(getBrain().getID() == leader) {
//				getBrain().setDebugString("leader : " + getBrain().isReloading());
//			} else {
//				getBrain().setDebugString(getBrain().isReloading() + "");
//			}
//		} catch (Exception ex) {};
//		
//		if(getBrain().isBlocked()) {
//			return unstuck();
//		}
//		
		return toReturn;
	}

	public void newPosition() {
		if(isOnTop == 0) {
			x = new Random().nextInt(3200) - 1600;
			y = new Random().nextInt(2000) - 1000;
		}
		else {
			x = (new Random().nextInt(200) + 700) * isOnTop;
			y = (new Random().nextInt(300) + 250) * isOnTop;
		}
	}

	public String unstuck() {
		if(new Random().nextInt(100) > 98) {
			angleToUnstuck = new Random().nextInt(360);
		}
		getBrain().setHeading(getBrain().getHeading() + 90);
		return WarRocketLauncher.ACTION_MOVE;
	}

	public String attack() {
		Environnement ev = this.getEnvironnement();
		Group t;
		try {
			t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			String s = rush(t, ev);
			if(!s.equals("")) {
				return s;
			}

			if(enemies != null && enemies.size() > 0){
				t.setTargetID(enemies.get(0).getID());
				t.setTarget(Tools.getPositionOfEntityFromMine(ev.getStructWarBrain(getBrain().getID()).getPosition(), enemies.get(0).getAngle(), enemies.get(0).getDistance()), false);
				t.setAttacking(true);
			} else {
				if(t.isBaseAttacked() && !t.isBaseAttackTeam()) {
					getBrain().setDebugString("base is under attack !");
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getDefensePosition(getBrain().getID()));
					return WarRocketLauncher.ACTION_MOVE;
				}
				try {
					Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
					Vector2 enemyPosition = ev.getEnemy(ev.getClosestEnemy(myPosition)).getPosition();

					if(myPosition.dst(enemyPosition) < 200) {
						t.setTarget(enemyPosition, false);
						Vector2 enemyBase = t.getTargetPosition(getBrain().getID(), isOnTop);
						Tools.setHeadingOn(
								getBrain(), 
								myPosition,
								enemyBase);

						if(myPosition.dst(enemyBase) < 3) {
							Tools.setHeadingOn(
									getBrain(), 
									myPosition,
									t.getTarget());
							getBrain().setDebugString("in position to attack");
							return  WarRocketLauncher.ACTION_IDLE;
						}
						getBrain().setDebugString("positioning to attack");
						toReturn = WarRocketLauncher.ACTION_MOVE;
					} else {
						t.setAttacking(false);
						s = attackBaseAfterFirstBaseDead(t, ev);
						if(!s.equals("")) {
							getBrain().setDebugString("going to attack enemy base");
							return s;
						}
					}
				} catch (Exception e) {
					t.setAttacking(false);
					s = attackBaseAfterFirstBaseDead(t, ev);
					if(!s.equals("")) {
						getBrain().setDebugString("going to attack enemy base");
						return s;
					}
				};

			}
			if(t.isAttacking()) {
				if(enemies.size() > 0) {
					if(!getBrain().isReloaded()) {
						Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
						Vector2 enemyPosition = t.getTargetPosition(getBrain().getID(), isOnTop);

						Tools.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getTargetPosition(getBrain().getID(), isOnTop));

						if(myPosition.dst(enemyPosition) < 3) {
							Tools.setHeadingOn(
									getBrain(), 
									myPosition,
									t.getTarget());
							getBrain().setDebugString("in position to attack");

							return  WarRocketLauncher.ACTION_IDLE;
						}
					} else {
						getBrain().setHeading(enemies.get(0).getAngle());
						getBrain().setDebugString("shooting target");
						return WarRocketLauncher.ACTION_FIRE;
					}
				} else {
					Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
					Vector2 enemyBase = t.getTargetPosition(getBrain().getID(), isOnTop);
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							enemyBase);

					if(myPosition.dst(enemyBase) < 3) {
						Tools.setHeadingOn(
								getBrain(), 
								myPosition,
								t.getTarget());
						getBrain().setDebugString("in position to shoot");
						return  WarRocketLauncher.ACTION_IDLE;
					}
					toReturn = WarRocketLauncher.ACTION_MOVE;
				}
			}
		} catch (Exception e){getBrain().setDebugString(getBrain().getDebugString() + "in catch1");};

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
				} else {
					if(!t.isReady() && ev.inRush()) {
						Tools.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getLeaderPositionForWaiting(getBrain().getID(), ev, isOnTop));
					} else {
						int n = new Random().nextInt(100);
						if(n > 98 || getBrain().isBlocked()) {
							newPosition();
						}
						Tools.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								new Vector2(x, y));
					}

				}
			} catch (NotExistException e) {
			}

		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}

		return WarRocketLauncher.ACTION_MOVE;
	}

	private String attackBaseAfterFirstBaseDead(Group t, Environnement ev) throws NotExistException {
		if(ev.oneBaseIsFound()) {
			Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
			Vector2 enemyBase = t.getBaseAttackPosition(getBrain().getID());
			ArrayList<WarPercept> p = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
			if(p.size() > 0) {
				t.setTarget(getEnvironnement().getPositionFirstEnemyBase(), true);
				if(!getBrain().isReloaded()) {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							enemyBase);
					if(myPosition.dst(enemyBase) < 3) {
						Tools.setHeadingOn(
								getBrain(), 
								ev.getStructWarBrain(getBrain().getID()).getPosition(),
								t.getTarget());
						return  WarRocketLauncher.ACTION_IDLE;
					}
					return WarRocketLauncher.ACTION_MOVE;
					
				} else {
					getBrain().setHeading(p.get(0).getAngle());
					return WarRocketLauncher.ACTION_FIRE;
				}
			} else {
				if(myPosition.dst(enemyBase) < 3) {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							t.getTarget());
					return  WarRocketLauncher.ACTION_IDLE;
				} else {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							enemyBase);
				}
				return WarRocketLauncher.ACTION_MOVE;
			}
		}
		return "";
	}

	private String attackBase(Group t, Environnement ev) throws NotExistException {
		if(ev.oneBaseIsFound()) {
			Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
			Vector2 enemyBase = t.getBaseAttackPosition(getBrain().getID());
			if(enemyBases.size() > 0) {
				
				t.setTarget(getEnvironnement().getPositionFirstEnemyBase(), true);
				if(!getBrain().isReloaded()) {
					Tools.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							enemyBase);

					if(myPosition.dst(enemyBase) < 3) {
						Tools.setHeadingOn(
								getBrain(), 
								myPosition,
								getEnvironnement().getPositionFirstEnemyBase());
						getBrain().setDebugString("in position and reloading");
						return  WarRocketLauncher.ACTION_IDLE;
					}

					return WarRocketLauncher.ACTION_MOVE;
				} else {
					getBrain().setDebugString("fire!");
					getBrain().setHeading(enemyBases.get(0).getAngle());
					return WarRocketLauncher.ACTION_FIRE;
				}
			} else {
				Tools.setHeadingOn(
						getBrain(), 
						ev.getStructWarBrain(getBrain().getID()).getPosition(),
						enemyBase);
				if(myPosition.dst(enemyBase) < 3) {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							getEnvironnement().getPositionFirstEnemyBase());
					getBrain().setDebugString("i'm in position and i dont see anyone");
					return  WarRocketLauncher.ACTION_IDLE;
				}
				return WarRocketLauncher.ACTION_MOVE;
			}
		} else {
			return goToApproxEnemyBase(t, ev);
		}
	}

	private String goToApproxEnemyBase(Group t, Environnement ev) throws NotExistException {
		try {
			Vector2 enemyBasePosition = ev.getApproxEnemyBasePosition();
			Tools.setHeadingOn(
					getBrain(), 
					ev.getStructWarBrain(getBrain().getID()).getPosition(),
					enemyBasePosition);

			return WarRocketLauncher.ACTION_MOVE;

		} catch (BaseNotFoundException e) {};
		return "";
	}

	private String rush(Group t, Environnement ev) throws NotExistException {
		if (ev.inRush()) {
			return attackBase(t, ev);
		} else if (ticksSinceLastEncounter > 100 && !ev.oneBaseIsFound()){
			t.setReady(true);
		}
		return "";
	}
}
