package edu.turtlekit3.warbot.teams.doe;

import java.awt.Color;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Vector2 foodTargetFound = null;
	private boolean haveTouchIt = false;

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;

	private void randomMove(){

		if(getBrain().isBlocked())
			getBrain().setRandomHeading(90);
		
		getBrain().setDebugString("random");

		toReturn = WarExplorer.ACTION_MOVE;
	}

	private boolean getFood() {
		if(getBrain().isBagFull()){
			return false;
		}

		try {
			Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();

			ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();

			if (foodPercepts != null && foodPercepts.size() > 0){

				this.getBrain().setDebugString("taking food found");

				WarPercept foodP = foodPercepts.get(0);

				Vector2 posFood = Tools.getPositionOfEntityFromMine(curentPosition, (float) foodP.getAngle(), (float) foodP.getDistance());
				Environnement.getInstance().setLastFood(posFood);

				if (foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE){
					getBrain().setHeading(foodP.getAngle());
					toReturn = MovableWarAgent.ACTION_MOVE;
					return true;
				} else {
					toReturn = MovableWarAgent.ACTION_TAKE;
					return true;
				}

				// Il faut trouver de la bouffe
			} else {

				// On s'aide de la dernière position, sauf si j'y suis déjà allé
				if (Environnement.getInstance().haveLastFood()) {

					Vector2 bouffe = Environnement.getInstance().getLastFood();

					if (foodTargetFound == null || !foodTargetFound.equals(bouffe) || !haveTouchIt) {
						if (!Tools.isNextTo(curentPosition, bouffe)) {
							Tools.setHeadingOn(this.getBrain(), curentPosition, bouffe);
							this.getBrain().setDebugString("going to last food");
							this.toReturn = WarExplorer.ACTION_MOVE;
							return true;
						} else {
							this.getBrain().setDebugString("is nextTo");
							haveTouchIt = true;
							this.foodTargetFound = bouffe;
							this.randomMove();
							return true;
						}
					} else {
						this.randomMove();
						this.getBrain().setDebugString("have already found last food");
						return true;
					}
				} 

				this.randomMove();
				this.getBrain().setDebugString("searching food");
				return true;
			}
		}  catch (NotExistException e) {
			return false;
		}

	}

	private boolean returnFood() {

		Vector2 curentPosition = null;
		try {
			curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
		}  catch (NotExistException e) {
			return false;
		}

		getBrain().setDebugStringColor(Color.green.darker());
		getBrain().setDebugString("Returning Food");

		if(getBrain().isBlocked()) {
			getBrain().setRandomHeading();
			return true;
		}

		ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);

		//Si je ne voit pas de base
		if(basePercepts == null | basePercepts.size() == 0){

			Tools.setHeadingOn(this.getBrain(), curentPosition, new Vector2(0,0));
			toReturn = MovableWarAgent.ACTION_MOVE;
			return true;

		} else {//si je vois une base
			WarPercept base = basePercepts.get(0);

			if (base.getDistance() > MovableWarAgent.MAX_DISTANCE_GIVE){
				getBrain().setHeading(base.getAngle());
				toReturn = MovableWarAgent.ACTION_MOVE;
				return true;
			} else {
				getBrain().setIdNextAgentToGive(base.getID());
				toReturn = MovableWarAgent.ACTION_GIVE;
				return true;
			}

		}


	}

	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);

		toReturn = WarExplorer.ACTION_IDLE;

		if(getBrain().isBlocked()) {
			getBrain().setRandomHeading(90);
			return WarExplorer.ACTION_MOVE;
		}
		
		// Cherche la base
		//if (!Environnement.getInstance().oneBaseIsFound()) {
		if (false) {
			this.randomMove();
			this.getBrain().setDebugString("search enemy base");
			return toReturn;
		}

		else {
			if (!this.getFood()) {
				if (!this.returnFood()) {
					this.getBrain().setDebugString("Rien à faire");
					return WarExplorer.ACTION_IDLE;
				}
			}

		}

		return toReturn;


	}
}

