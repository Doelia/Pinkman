package edu.turtlekit3.warbot.teams.doe;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

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
	private Vector2 target = new Vector2(0, 0);

	public WarExplorerBrainController() {
		super();
	}

	private String toReturn;

	private void recordFood() {
		
		try {
			Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();

			ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();

			if (foodPercepts != null && foodPercepts.size() > 0){
				for (WarPercept p : foodPercepts) {
					Vector2 pos = Tools.getPositionOfEntityFromMine(curentPosition, p.getAngle(), p.getDistance());
					Environnement.getInstance().addFreeFood(pos, p.getID());
				}
			}
			
		}  catch (NotExistException e) {
		}

	}


	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);

		toReturn = WarExplorer.ACTION_MOVE;
		
		try {
			Vector2 curentPosition = Environnement.getInstance().getStructWarBrain(this.getBrain().getID()).getPosition();
			
			this.recordFood();
			
			if (getBrain().isBagFull()){
				this.getBrain().setDebugString("return base");
				
				this.target = new Vector2(0,0);
				
				ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);

				if(basePercepts != null && basePercepts.size() > 0){
					WarPercept base = basePercepts.get(0);
					if (base.getDistance() <= MovableWarAgent.MAX_DISTANCE_GIVE){
						getBrain().setIdNextAgentToGive(base.getID());
						toReturn = MovableWarAgent.ACTION_GIVE;
					}
				}
				
			} else {
				
				if (this.targetFood == null) {
					this.targetFood = Environnement.getInstance().getFreeFood();
				} else {
					this.getBrain().setDebugString("target food");
					if (Tools.isNextTo(curentPosition, this.targetFood, MovableWarAgent.MAX_DISTANCE_GIVE)) {
						this.getBrain().setDebugString("taking food");
						toReturn = MovableWarAgent.ACTION_TAKE;
						this.targetFood = null;
					}
				}
				this.target = this.targetFood;
				
			}

			if (getBrain().isBlocked()) {
				this.getBrain().setDebugString("blocked");
				getBrain().setHeading(getBrain().getHeading()+45);
			} else {
				if (target == null) {
					this.getBrain().setDebugString("randomised");
					this.getBrain().setRandomHeading(20);
				} else {
					Tools.setHeadingOn(this.getBrain(), curentPosition, target);
				}
			}

		}  catch (NotExistException e) {
		}
		
		return toReturn;


	}
}

