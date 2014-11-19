package edu.turtlekit3.warbot.agents.percepts;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.game.Game;

public class InRadiusPerceptsGetter extends PerceptsGetter {

	public InRadiusPerceptsGetter(ControllableWarAgent agent) {
		super(agent);
	}
	
	@Override
	public ArrayList<WarPercept> getPercepts() {
		ArrayList<WarPercept> percepts = new ArrayList<>();

		for (WarAgent a : Game.getInstance().getAllAgentsInRadiusOf(getAgent(), getAgent().getDistanceOfView())) {
			percepts.add(new WarPercept(getAgent(), a));
		}

		return percepts;
	}


}
