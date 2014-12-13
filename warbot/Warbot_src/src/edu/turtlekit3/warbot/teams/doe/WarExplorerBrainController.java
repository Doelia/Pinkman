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
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Vector2 targetFood = null;
	private Vector2 target = new Vector2(0, 0);
	private boolean isInGave = false;

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;
	
	private void broadcastFoodPosition() {
		if (!Environnement.CHEAT)
			getBrain().broadcastMessageToAll(Constants.foodHere, this.targetFood.toString());
	}

	private void findFood() {
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


	@Override
	public String action() {
		
		if (Environnement.CHEAT) {
			WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);
		}

		toReturn = WarExplorer.ACTION_MOVE;
		
		try {
			
			Vector2 curentPosition = this.getCurentPosition();
			
			this.findFood();
			
			if (this.getBrain().isBagEmpty()) {
				this.isInGave = false;
			}
			
			if ((getBrain().isBagFull() || this.isInGave)){
				this.isInGave = true;
				this.getBrain().setDebugString("return base");
				
				this.target = new Vector2(0,0);
				ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);

				if(basePercepts != null && basePercepts.size() > 0){
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

			if (getBrain().isBlocked()) {
				this.getBrain().setDebugString("blocked");
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
		}
		
		this.getBrain().setDebugString("bagSize="+this.getBrain().getNbElementsInBag()+"/"+this.getBrain().getBagSize());
		
		return toReturn;


	}
}

