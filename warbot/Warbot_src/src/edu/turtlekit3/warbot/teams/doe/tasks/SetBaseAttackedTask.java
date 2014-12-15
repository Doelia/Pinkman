package edu.turtlekit3.warbot.teams.doe.tasks;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.doe.Constants;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class SetBaseAttackedTask extends Task {

	public SetBaseAttackedTask(WarBrainController brain, WarAgentType type,
			Environnement e) {
		super(brain, type, e);
	}

	@Override
	public boolean exec() {
		ArrayList<WarPercept> percept = brain.getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if(percept.size() > 0) {
			getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.enemyTankHere, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
			e.getTeamManager().setBaseAttacked(true);
			try {
				sender.setPositionBaseAttacked(e.getStructWarBrain(brain.getID()).getPosition());
			} catch (NotExistException e1) {}
		} else {
			e.getTeamManager().setBaseAttacked(false);
		}
		return true;
	}

}
