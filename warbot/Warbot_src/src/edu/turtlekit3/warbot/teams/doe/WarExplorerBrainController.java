package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private boolean foodTargetFound = false;

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;

	private void randomMove(){

		if(getBrain().isBlocked())
			getBrain().setRandomHeading();

		toReturn = WarExplorer.ACTION_MOVE;
	}

	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);

		toReturn = WarExplorer.ACTION_IDLE;

		// Cherche la base
		//if (!Environnement.getInstance().oneBaseIsFound()) {
		if (false) {
			this.randomMove();
			this.getBrain().setDebugString("search enemy base");
			return toReturn;
		}

		else {

			ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();

			if (foodPercepts != null && foodPercepts.size() > 0){
				
				this.getBrain().setDebugString("taking food found");
				
				foodTargetFound = true;
				WarPercept foodP = foodPercepts.get(0);
				
				try {
					Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
					Vector2 posFood = Tools.getPositionOfEntityFromMine(curentPosition, (float) foodP.getAngle(), (float) foodP.getDistance());
					Environnement.getInstance().setLastFood(posFood);
				} catch (NotExistException e) {
				}

				if (foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE){
					getBrain().setHeading(foodP.getAngle());
					toReturn = MovableWarAgent.ACTION_MOVE;
				} else {
					this.foodTargetFound = false;
					toReturn = MovableWarAgent.ACTION_TAKE;
				}
			
			// Il faut trouver de la bouffe
			} else {

				// On s'aide de la dernière position, sauf si j'y suis déjà allé
				if (Environnement.getInstance().haveLastFood() && !foodTargetFound) {
					
					Vector2 bouffe = Environnement.getInstance().getLastFood();
					
					try {
						Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
						if (!Tools.isNextTo(curentPosition, bouffe)) {
							Tools.setHeadingOn(this.getBrain(), curentPosition, bouffe);
							this.getBrain().setDebugString("going to last food");
							return WarExplorer.ACTION_MOVE;
						} else {
							this.foodTargetFound = true;
						}
					} catch (NotExistException e) {
					}
				}
					
				this.randomMove();
				this.getBrain().setDebugString("searching food");
				return toReturn;
			}
		}

		return toReturn;


	}
}

