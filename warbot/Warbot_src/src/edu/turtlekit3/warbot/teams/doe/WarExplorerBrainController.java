package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.BaseNotFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;
import edu.turtlekit3.warbot.teams.doe.tasks.DetectEnemyTask;
import edu.turtlekit3.warbot.teams.doe.tasks.DetectFoodTask;
import edu.turtlekit3.warbot.teams.doe.tasks.MoveTask;
import edu.turtlekit3.warbot.teams.doe.tasks.SendAlliesTask;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Environnement ev;

	private MoveTask activeTask = null;
	private String action;
	private boolean isInGave = false;
	private boolean haveTouchAproxTarget = false;

	public boolean isAWall() {
		return (this.getBrain().isBlocked() && this.getBrain().getPercepts().isEmpty());
	}

	public WarExplorerBrainController() {
		super();
	}

	private Environnement getEnvironnement() {
		if (Behavior.CHEAT)
			return Environnement.getInstance();
		else {
			if (ev == null) {
				ev = new Environnement();
			}
			return ev;
		}
	}

	int distance = 0;
	private void setDirectionToFindBasePosition(Vector2 position, Vector2 direction) {
		Tools.setHeadingOn(
				getBrain(), 
				position,
				direction);
	}

	private String findOurPositionBase() {

		System.out.println("uie");
		Environnement e = getEnvironnement();

		int index = e.getExplorerIndex(getBrain().getID());
		Vector2 v1 = new Vector2(20000, 0);
		Vector2 v2 = new Vector2(-20000, 0);
		Vector2 v3 = new Vector2(0, 0);
		
		if(index % 2 == 0) {
			setDirectionToFindBasePosition(v3, v2);
		} else if (index % 2 == 1) {
			setDirectionToFindBasePosition(v3, v1);
		}

		if(this.isAWall()) {
			if(index % 2 == 0) {
				e.setWeAreInTop(false);
			} else if (index % 2 == 1) {
				e.setWeAreInTop(true);
			}
		}

		if(getBrain().isBlocked()) {
			getBrain().setHeading(getBrain().getHeading() + 90);
		}


		//		if (this.isAWall()) {
		//			System.out.println("Base en haut à droite");
		//			e.setWeAreInTop(true);
		//			return MovableWarAgent.ACTION_IDLE;
		//		}
		//		
		//		if (this.distance > 200) {
		//			System.out.println("Base en bas à droite");
		//			getEnvironnement().setWeAreInTop(false);
		//			return MovableWarAgent.ACTION_IDLE;
		//		}
		//		
		//		this.getBrain().setHeading(0);
		//		if (!this.getBrain().isBlocked())
		//			distance++;
		//		
		//		this.getBrain().setDebugString("distance="+distance);
		return MovableWarAgent.ACTION_MOVE;
	}

	public Vector2 getTargetFood() {
		try {
			return getEnvironnement().getStructWarBrain(this.getBrain().getID()).getTargetFood();
		} catch (NotExistException e) {
			return null;
		}
	}

	private boolean imSearcher() {
		return true;
//		if (getEnvironnement().idSearcherBase == -1) {
//			getEnvironnement().idSearcherBase = this.getBrain().getID();
//		}
//
//		return (getEnvironnement().idSearcherBase == this.getBrain().getID());
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

	private boolean imInSearchEnemyBaseGroup() {
		return this.imSearcher();
	}

	@Override
	public String action() {

		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarExplorer;

		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e).exec();
		new DetectFoodTask(this, t, e).exec();

		if (activeTask == null)
			activeTask = new MoveTask(this, t, e);

		this.action = WarExplorer.ACTION_MOVE;
		
		e.registerExplorer(getBrain().getID());

		try {

			Vector2 curentPosition = activeTask.getCurentPosition();

			if (!this.baseEnemyIsFound() && (Environnement.RUSH_MODE || this.imInSearchEnemyBaseGroup())) {

				if (this.getEnvironnement().ourBaseIsFound()) {

					this.getBrain().setDebugString("going to aprox enemy base: "+this.getPositionAprox());

					this.activeTask.setTarget(this.getPositionAprox());

					if (Tools.isNextTo(curentPosition, activeTask.getTarget(), 5)) {
						this.haveTouchAproxTarget = true;
					}

					if (this.haveTouchAproxTarget) {
						this.activeTask.setTarget(null);
					}

				} else {
					if (this.imSearcher()) {
						this.getBrain().setDebugString("searching our base position");
						return this.findOurPositionBase();
					}
				}

			} else {

				if (this.getBrain().isBagEmpty()) {
					this.isInGave = false;
				}

				if ((getBrain().isBagFull() || this.isInGave)){
					this.isInGave = true;
					this.getBrain().setDebugString("return base");

					this.activeTask.setTarget(getEnvironnement().getPositionAllieBaseWithLowLife());

					ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);

					if(basePercepts != null && basePercepts.size() > 0) {
						WarPercept base = basePercepts.get(0);
						if (base.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
							getBrain().setIdNextAgentToGive(base.getID());
							action = MovableWarAgent.ACTION_GIVE;
						}
					}

				} else {

					if (this.getTargetFood() != null) {
						this.getBrain().setDebugString("target food");
						if (Tools.isNextTo(curentPosition, this.getTargetFood(), MovableWarAgent.MAX_DISTANCE_GIVE)) {
							this.getBrain().setDebugString("taking food");
							action = MovableWarAgent.ACTION_TAKE;
							getEnvironnement().getStructWarBrain(this.getBrain().getID()).setFirstTargetFound();
						}
					}
					this.activeTask.setTarget(this.getTargetFood());
				}
			}

			this.activeTask.exec();


		}  catch (NotExistException ex) {
		} catch (BaseNotFoundException ex) {
		}

		return action;


	}
}