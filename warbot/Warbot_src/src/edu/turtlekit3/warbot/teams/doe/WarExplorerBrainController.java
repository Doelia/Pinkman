package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.clean.Tools;
import edu.turtlekit3.warbot.teams.doe.exceptions.BaseNotFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Vector2 targetFood = null;
	private Vector2 target = null;
	private boolean isInGave = false;
	private boolean baseIsFound = false;
	private int pos = 0;
	private boolean haveTouchAproxTarget = false;
	
	public boolean isAWall() {
		return (this.getBrain().isBlocked() && this.getBrain().getPercepts().isEmpty());
	}

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;
	
	private void broadcastFoodPosition() {
		if (!Environnement.CHEAT)
			getBrain().broadcastMessageToAll(Constants.foodHere, this.targetFood.toString());
	}
	
	private void broadcastOurBasePosition() {
		getBrain().broadcastMessageToAll(Constants.enemyBaseHere, this.pos+"");
	}
	
	
	int distance = 0;
	private String findOurPositionBase() {
		
		if (Environnement.CHEAT) {
			if (this.isAWall()) {
				System.out.println("Base en haut à droite");
				Environnement.getInstance().setWeAreInTop(true);
				return MovableWarAgent.ACTION_IDLE;
			}
			
			if (this.distance > 200) {
				System.out.println("Base en bas à droite");
				Environnement.getInstance().setWeAreInTop(false);
				return MovableWarAgent.ACTION_IDLE;
			}
		} else {
			if (this.isAWall()) {
				this.baseIsFound = true;
				this.pos = 1;
			}
			
			if (this.distance > 200) {
				this.baseIsFound = false;
				this.pos = 2;
			}
			
			this.broadcastOurBasePosition();
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
					if (Environnement.CHEAT) {
						Vector2 pos = Tools.getPositionOfEntityFromMine(curentPosition, p.getAngle(), p.getDistance());
						Environnement.getInstance().addFreeFood(pos, p.getID());
					} else {
						targetFood = new Vector2((float) foodPercepts.get(0).getAngle(), (float) p.getDistance());
						this.broadcastFoodPosition();
					}
				}
			}
		}  catch (NotExistException e) {
		}
	}
	
	public Vector2 getTargetFood() {
		if (Environnement.CHEAT) {
			try {
				return Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getTargetFood();
			} catch (NotExistException e) {
				return null;
			}
		} else {
			return this.targetFood;
		}
	}
	
	public Vector2 getCurentPosition() throws NotExistException {
		try {
			if (Environnement.CHEAT)
				return Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
			else {
				return new Vector2((float) this.getBrain().getHeading(), 0);
			}
		} catch (Exception e) {
			throw new NotExistException();
		}
	}
	
	public void disableFoodTarget() {
		if (Environnement.CHEAT) {
			try {
				Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).setFirstTargetFound();
			} catch (NotExistException e) {
				e.printStackTrace();
			}
		} else {
			this.targetFood = null;
		}
	}
	
	private boolean imSearcher() {
		if (Environnement.CHEAT) {
			if (Environnement.idSearcherBase == -1) {
				Environnement.idSearcherBase = this.getBrain().getID();
			}
			
			return (Environnement.idSearcherBase == this.getBrain().getID());
		}
		
		return (this.getBrain().getID() == 1);
	}
	
	private boolean ourBaseIsFound() {
		if (Environnement.CHEAT) {
			try {
				Environnement.getInstance().getWeAreInTop();
				return true;
			} catch (BaseNotFoundException e) {
				return false;
			}
		}
		
		return this.baseIsFound;
	}


	private boolean baseEnemyIsFound() {
		if (Environnement.CHEAT) {
			try {
				Environnement.getInstance().getPositionFirstEnemyBase();
				return true;
			} catch (NotExistException e) {
				return false;
			}
		}
		
		return (this.target == null && baseIsFound);
	}
	
	private Vector2 getPositionAprox() throws BaseNotFoundException {
		if (Environnement.CHEAT)
			return Environnement.getInstance().getApproxEnemyBasePosition();
		
		return this.target;
	}
	
	private boolean imInSearchEnemyBaseGroup() {
		return this.imSearcher();
	}
	
	@Override
	public String action() {
		
		if (Environnement.CHEAT) {
			WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);
		}
		
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
					
					if (Environnement.CHEAT) {
						this.target = Environnement.getInstance().getPositionAllieBaseWithLowLife();
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

		}  catch (NotExistException e) {
		} catch (BaseNotFoundException e) {
		}
		
		return toReturn;


	}
}

