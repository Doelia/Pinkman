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
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	private Vector2 targetFood = null;

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;

	private void randomMove(){

		getBrain().setRandomHeading(20);
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
				System.out.println( foodPercepts.size()+" founded");
				this.getBrain().setDebugString("taking food found");
				for (WarPercept p : foodPercepts) {
					Vector2 pos = Tools.getPositionOfEntityFromMine(curentPosition, p.getAngle(), p.getDistance());
					Environnement.getInstance().addFreeFood(pos);
				}
			}
			
			if (targetFood == null) {
				targetFood = Environnement.getInstance().getFreeFood();
			}
			
			if (targetFood != null) {

				if (Tools.isNextTo(curentPosition, targetFood, ControllableWarAgent.MAX_DISTANCE_GIVE)) {
					this.getBrain().setDebugString("taking food");
					this.targetFood = null;
					toReturn = MovableWarAgent.ACTION_TAKE;
					return true;
				} else {
					this.getBrain().setDebugString("going to "+targetFood);
					Tools.setHeadingOn(this.getBrain(), curentPosition, targetFood);
					this.toReturn = WarExplorer.ACTION_MOVE;
					return true;
				}
			}  else {
				this.randomMove();
				this.getBrain().setDebugString("searching food");
				return true;
			}
			
		}  catch (NotExistException e) {
			return false;
		}

	}

	private boolean returnFood() {
		
		if(!getBrain().isBagFull()){
			return false;
		}

		try {
			Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
			
			getBrain().setDebugStringColor(Color.green.darker());
			getBrain().setDebugString("Returning Food");

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
		}  catch (NotExistException e) {
			return false;
		}


	}

	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);

		toReturn = WarExplorer.ACTION_IDLE;
		
		if(getBrain().isBlocked()) {
			getBrain().setHeading(getBrain().getHeading()+90);
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

