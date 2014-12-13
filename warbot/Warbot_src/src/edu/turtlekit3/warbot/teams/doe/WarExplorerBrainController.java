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
import edu.turtlekit3.warbot.teams.doe.tasks.SendAlliesTask;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Environnement ev;
	
	private Vector2 target = null;
	private boolean isInGave = false;
	private boolean haveTouchAproxTarget = false;
	
	public boolean isAWall() {
		return (this.getBrain().isBlocked() && this.getBrain().getPercepts().isEmpty());
	}

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;
	
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
	private String findOurPositionBase() {
		
		if (this.isAWall()) {
			System.out.println("Base en haut à droite");
			getEnvironnement().setWeAreInTop(true);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if (this.distance > 200) {
			System.out.println("Base en bas à droite");
			getEnvironnement().setWeAreInTop(false);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		this.getBrain().setHeading(0);
		if (!this.getBrain().isBlocked())
			distance++;
		
		this.getBrain().setDebugString("distance="+distance);
		return MovableWarAgent.ACTION_MOVE;
	}

	private void recordFood() {
		try {
			Vector2 curentPosition = this.getCurentPosition();
			ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();

			if (foodPercepts != null && foodPercepts.size() > 0){
				for (WarPercept p : foodPercepts) {
						Vector2 pos = Tools.getPositionOfEntityFromMine(curentPosition, p.getAngle(), p.getDistance());
						getEnvironnement().addFreeFood(pos, p.getID());
				}
			}
		}  catch (NotExistException e) {
		}
	}
	
	public Vector2 getTargetFood() {
			try {
				return getEnvironnement().getStructWarBrain(this.getBrain().getID()).getTargetFood();
			} catch (NotExistException e) {
				return null;
			}
	}
	
	public Vector2 getCurentPosition() throws NotExistException {
		try {
			return getEnvironnement().getStructWarBrain(this.getBrain().getID()).getPosition();
		} catch (Exception e) {
			throw new NotExistException();
		}
	}
	
	public void disableFoodTarget() {
		try {
			getEnvironnement().getStructWarBrain(this.getBrain().getID()).setFirstTargetFound();
		} catch (NotExistException e) {
			e.printStackTrace();
		}
	}
	
	private boolean imSearcher() {
		if (getEnvironnement().idSearcherBase == -1) {
			getEnvironnement().idSearcherBase = this.getBrain().getID();
		}
		
		return (getEnvironnement().idSearcherBase == this.getBrain().getID());
	}
	
	private boolean ourBaseIsFound() {
		try {
			getEnvironnement().getWeAreInTop();
			return true;
		} catch (BaseNotFoundException e) {
			return false;
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
	
	private boolean imInSearchEnemyBaseGroup() {
		return this.imSearcher();
	}
	
	@Override
	public String action() {
		
		Environnement e = this.getEnvironnement();
		WarAgentType t = WarAgentType.WarExplorer;
		
		new DetectEnemyTask(this, t, e).exec();
		new SendAlliesTask(this, t, e).exec();
		
		this.toReturn = WarExplorer.ACTION_MOVE;
		
		try {
			
			Vector2 curentPosition = this.getCurentPosition();
			this.recordFood();
			
			if (!this.baseEnemyIsFound() && (Environnement.RUSH_MODE || this.imInSearchEnemyBaseGroup())) {
				
				if (this.ourBaseIsFound()) {
					
					this.getBrain().setDebugString("going to aprox enemy base: "+this.getPositionAprox());
					
					this.target = this.getPositionAprox();
					
					if (Tools.isNextTo(curentPosition, target, 5)) {
						this.haveTouchAproxTarget = true;
					}
					
					if (this.haveTouchAproxTarget) {
						this.target = null;
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
					
					if (Behavior.CHEAT) {
						this.target = getEnvironnement().getPositionAllieBaseWithLowLife();
					} else {
						this.target = new Vector2(0,0);
					}
					
					ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);

					if(basePercepts != null && basePercepts.size() > 0) {
						WarPercept base = basePercepts.get(0);
						if (base.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
							getBrain().setIdNextAgentToGive(base.getID());
							toReturn = MovableWarAgent.ACTION_GIVE;
						}
					}
					
				} else {
					
					if (this.getTargetFood() != null) {
						this.getBrain().setDebugString("target food");
						if (Tools.isNextTo(curentPosition, this.getTargetFood(), MovableWarAgent.MAX_DISTANCE_GIVE)) {
							this.getBrain().setDebugString("taking food");
							toReturn = MovableWarAgent.ACTION_TAKE;
							this.disableFoodTarget();
						}
					}
					this.target = this.getTargetFood();
				}
			}
			
			if (getBrain().isBlocked()) {
				getBrain().setHeading(getBrain().getHeading()+20);
			} else {
				if (target == null) {
					this.getBrain().setDebugString("randomised");
					getBrain().setRandomHeading(20);
				} else {
					Tools.setHeadingOn(this.getBrain(), curentPosition, target);
				}
			}

		}  catch (NotExistException ex) {
		} catch (BaseNotFoundException ex) {
		}
		
		return toReturn;


	}
}

