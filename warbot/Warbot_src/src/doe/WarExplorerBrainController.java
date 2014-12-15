package doe;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import doe.behavior.Behavior;
import doe.environement.Environnement;
import doe.exceptions.BaseNotFoundException;
import doe.exceptions.NotExistException;
import doe.messages.ReceiverEnvironementInstruction;
import doe.tasks.DetectEnemyTask;
import doe.tasks.DetectFoodTask;
import doe.tasks.MoveTask;
import doe.tasks.SendAlliesTask;
import doe.tools.Tools;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Environnement ev;
	private ReceiverEnvironementInstruction receiver;

	private MoveTask activeTask = null;
	private String action;
	private boolean isInGave = false;
	private boolean haveTouchAproxTarget = false;
	private boolean detectFood;
	private int ttl_touchAproxTarget = 1000;

	public boolean isAWall() {
		return (this.getBrain().isBlocked() && this.getBrain().getPercepts().isEmpty());
	}

	public WarExplorerBrainController() {
		super();
	}

	private Environnement getEnvironnement() {
		if (Behavior.CHEAT) {
			ev = Behavior.getGoodInstance(this.getBrain());
			receiver = new ReceiverEnvironementInstruction(ev);
		}
		else {
			if (ev == null) {
				ev = new Environnement();
				receiver = new ReceiverEnvironementInstruction(ev);
			}
		}
		return ev;
	}

	public boolean jeDoisPartirADroite() {
		try {
			return (getEnvironnement().getExplorerIndex(getBrain().getID()) % 2 == 0);
		} catch (Exception e) {
			return true;
		}
	}

	private String findOurPositionBase() {
		this.getBrain().setHeading(this.jeDoisPartirADroite()?0:180);
		if (this.isAWall()) {
			getEnvironnement().setWeAreInTop(this.jeDoisPartirADroite());
		}
		return MovableWarAgent.ACTION_MOVE;
	}

	public Vector2 getTargetFood() {
		try {
			return getEnvironnement().getStructWarBrain(this.getBrain().getID()).getTargetFood();
		} catch (NotExistException e) {
			return null;
		}
	}

	private boolean baseEnemyIsFound() {
		try {
			getEnvironnement().getPositionFirstEnemyBase();
			return true;
		} catch (NotExistException e) {
			return false;
		}
	}

	private Vector2 getPositionAprox() throws BaseNotFoundException {
		return getEnvironnement().getApproxEnemyBasePosition();
	}
	
	private void returnBase() throws NotExistException {
		this.isInGave = true;
		Vector2 base = getEnvironnement().getPositionAllieBaseWithLowLife();
		this.getBrain().setDebugString("return base "+base);
		this.activeTask.setTarget(base);
		ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);
		if (basePercepts != null && basePercepts.size() > 0) {
			WarPercept x = basePercepts.get(0);
			if (x.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
				this.getBrain().setDebugString("giving to base");
				getBrain().setIdNextAgentToGive(x.getID());
				action = MovableWarAgent.ACTION_GIVE;
			} else {
				Vector2 target = Tools.cartFromPolaire(x.getAngle(), x.getDistance());
				target.sub(this.activeTask.getCurentPosition());
				this.activeTask.setTarget(target);
			}
		}
	}
	
	private void targetFood() throws NotExistException {
		if (this.getTargetFood() != null) {
			this.getBrain().setDebugString("target food "+this.getTargetFood());
			if (Tools.isNextTo(this.activeTask.getCurentPosition(), this.getTargetFood(), MovableWarAgent.MAX_DISTANCE_GIVE)) {
				this.getBrain().setDebugString("taking food");
				action = MovableWarAgent.ACTION_TAKE;
				getEnvironnement().getStructWarBrain(this.getBrain().getID()).setFirstTargetFound();
			}
		}
		this.activeTask.setTarget(this.getTargetFood());
	}
	
	private boolean getFood() throws NotExistException {
		ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();
		if (foodPercepts != null && foodPercepts.size() > 0 && !this.getBrain().isBagFull()) {
			WarPercept food = foodPercepts.get(0);
			if (food.getDistance() <= MovableWarAgent.MAX_DISTANCE_GIVE) {
				action = MovableWarAgent.ACTION_TAKE;
			} else {
				Vector2 pos = Tools.getPositionOfEntityFromMine(this.activeTask.getCurentPosition(), food.getAngle(), food.getDistance());
				this.activeTask.setTarget(pos);
			}
			this.detectFood = false;
			return true;
		}
		return false;
	}

	@Override
	public String action() {

		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarExplorer;

		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e, getBrain().getMessages()).exec();

		this.action = WarExplorer.ACTION_MOVE;
		if (activeTask == null)
			activeTask = new MoveTask(this, t, e);
		this.detectFood = true;
		
		this.receiver.processMessages(this.getBrain());

		try {

			// 1. On cherche la base de l'enemie
			if (!this.baseEnemyIsFound()) {
				
				// 1.1. On cherche la position de notre base
				if (!this.getEnvironnement().ourBaseIsFound()) {
					this.getBrain().setDebugString("searching our base position");
					return this.findOurPositionBase();
				}
				 // 1.2 On s'approche de la base enemie
				else {
					if (!this.getFood())  { // 1.2.1 On cherche de la nourriture sur le passage, sinon on continue
						this.getBrain().setDebugString("aprox "+this.getPositionAprox()+", ttl "+ttl_touchAproxTarget);
						this.activeTask.setTarget(this.getPositionAprox());
						if (Tools.isNextTo(activeTask.getCurentPosition(), activeTask.getTarget(), 5) || ttl_touchAproxTarget <= 0) {
							this.haveTouchAproxTarget = true;
						}

						// On a atteint la position approximative, on wiggile jusqu'a ce qu'on trouve
						if (this.haveTouchAproxTarget) {
							this.activeTask.setTarget(null);
						}
						
						ttl_touchAproxTarget--;
					}
				}

			// 2. Comportement par défaut de l'éclaireur
			} else {
				if (this.getBrain().isBagEmpty()) {
					this.isInGave = false;
				}
				if ((getBrain().isBagFull() || this.isInGave)) {
					this.returnBase();
				} else {
					this.targetFood();
				}
			}
			
			if (this.detectFood) {
				new DetectFoodTask(this, t, e).exec();
			}

			this.activeTask.exec();

		} catch (NotExistException ex) {
		} catch (BaseNotFoundException ex) {
		}

		return action;
	}
}
