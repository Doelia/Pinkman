package edu.turtlekit3.warbot.teams.doe.tasks;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;

public class SetBaseAttackedTask extends Task {

	public SetBaseAttackedTask(WarBrainController brain, WarAgentType type,
			Environnement e) {
		super(brain, type, e);
	}

	@Override
	public boolean exec() {
		ArrayList<WarPercept> percept = brain.getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if(percept.size() > 0) {
			e.getTeamManager().setBaseAttacked(true);
		} else {
			e.getTeamManager().setBaseAttacked(false);
		}
		return true;
	}

}
