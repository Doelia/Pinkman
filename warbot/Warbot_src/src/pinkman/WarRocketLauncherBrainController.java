package pinkman;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import pinkman.environement.Environnement;
import pinkman.exceptions.BaseNotFoundException;
import pinkman.exceptions.NoTeamFoundException;
import pinkman.exceptions.NotExistException;
import pinkman.messages.ReceiverEnvironementInstruction;
import pinkman.messages.SenderEnvironnementInstruction;
import pinkman.tasks.DetectEnemyTask;
import pinkman.tasks.SendAlliesTask;
import pinkman.tasks.SetBaseAttackedTask;
import pinkman.teams.Group;
import pinkman.tools.Tools;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x;
	int y;
	String toReturn = "";
	int isOnTop;
	private int angleModifier = new Random().nextInt(180);
	private ArrayList<WarMessage> messages;
	private ArrayList<WarPercept> enemies;
	private int ticksCount;

	private Environnement e;
	private ReceiverEnvironementInstruction receiver;

	public WarRocketLauncherBrainController() {
		super();
		newPosition();
		isOnTop = 0;
		toReturn = "";
		messages = new ArrayList<WarMessage>();
		ticksCount = 0;
	}

	private Environnement getEnvironnement() {
		if (!isDefined()) {
			if (e == null) {
				e = new Environnement();
				receiver = new ReceiverEnvironementInstruction(e);
			}
		} else {
			e = SenderEnvironnementInstruction.createNewSender(this.getBrain());
			receiver = new ReceiverEnvironementInstruction(e);
		}
		return e;
	}
	
	private boolean isDefined() {
		return SetBaseAttackedTask.isdefine;
	}

	@Override
	public String action() {
		ticksCount++;
		
		if(!getBrain().isReloaded() && !getBrain().isReloading()) {
			return WarRocketLauncher.ACTION_RELOAD;
		}
		this.messages = getBrain().getMessages();

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
		getBrain().setHeading(getBrain().getHeading() + 25);
		return WarRocketLauncher.ACTION_MOVE;
	}

	public String attack() {
		Environnement ev = this.getEnvironnement();
		Group t;
		try {
			Vector2 myPosition = ev.getStructWarBrain(getBrain().getID()).getPosition();
			t = ev.getTeamManager().getTeamOf(this.getBrain().getID());

			if (!t.hasTarget()) {
				return WarRocketLauncher.ACTION_MOVE;
			} else {
				Vector2 targetPosition = t.getTargetPosition(getBrain().getID(), isOnTop);

				if(targetPosition.dst(myPosition) < 3) {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							ev.getEnemy(t.getTarget().brainID).getPosition());
					getBrain().setDebugString("i'im in position");
					WarPercept p = doIHaveYouInMyPercepts(t.getTarget().brainID);
					if(p != null && getBrain().isReloaded()) {
						getBrain().setDebugString("i see an enemy : " + p.getID());
						getBrain().setHeading(p.getAngle());
						return WarRocketLauncher.ACTION_FIRE;
					}
					return WarRocketLauncher.ACTION_IDLE;
				} else {
					Tools.setHeadingOn(
							getBrain(), 
							myPosition,
							targetPosition);
					getBrain().setDebugString("going to target");
					return WarRocketLauncher.ACTION_MOVE;
				}


			}
		} catch (Exception e) {};
		return toReturn;
	}

	public String move() {
		Environnement ev = this.getEnvironnement();
		if(ticksCount < 100) {
			wiggle();
			return toReturn;
		}
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
					Tools.setHeadingOn(getBrain(), ev.getStructWarBrain(getBrain().getID()).getPosition(), e.getPositionFirstEclaireur());
				}
			} catch (NotExistException e) {
			}

		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}

		return WarRocketLauncher.ACTION_MOVE;
	}

	private WarPercept doIHaveYouInMyPercepts(int id) {
		WarPercept w = null;
		for (WarPercept wp : enemies) {
			if(wp.getID() == id) {
				return wp;
			}
		}
		return w;
	}
	
	private boolean isDistanceOk() {
		ArrayList<WarPercept> percept = getBrain().getPerceptsAlliesByType(WarAgentType.WarRocketLauncher);

		if(percept.size() > 0 && percept.get(0).getDistance() < 20) {
			getBrain().setHeading(getBrain().getHeading() - angleModifier);
			toReturn = MovableWarAgent.ACTION_MOVE;
			return true;
		}
		return false;
	}

	private void wiggle() {
		if(getBrain().isBlocked())
			getBrain().setRandomHeading();

		getBrain().setDebugStringColor(Color.black);
		getBrain().setDebugString("Looking for ennemies");


		if(!isDistanceOk()) {
			double angle = getBrain().getHeading() + new Random().nextInt(10) - new Random().nextInt(10);
			getBrain().setHeading(angle);
			toReturn = MovableWarAgent.ACTION_MOVE;		
		}

	}
}
