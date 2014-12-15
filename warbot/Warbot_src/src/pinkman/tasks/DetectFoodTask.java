package pinkman.tasks;

import java.util.ArrayList;

import pinkman.environement.Environnement;
import pinkman.exceptions.NotExistException;
import pinkman.tools.Tools;

import com.badlogic.gdx.math.Vector2;


import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class DetectFoodTask extends Task {

	public DetectFoodTask(WarBrainController brain, WarAgentType type,
			Environnement e) {
		super(brain, type, e);
	}

	@Override
	public boolean exec() {
		try {
			Vector2 curentPosition = this.getCurentPosition();
			ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();

			if (foodPercepts != null && foodPercepts.size() > 0){
				for (WarPercept p : foodPercepts) {
						Vector2 pos = Tools.getPositionOfEntityFromMine(curentPosition, p.getAngle(), p.getDistance());
						sender.addFreeFood(pos, p.getID());
				}
			}
		}  catch (NotExistException e) {
		}
		return true;
	}

}
