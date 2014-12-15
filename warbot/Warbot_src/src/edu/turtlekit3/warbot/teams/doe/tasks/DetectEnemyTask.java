package edu.turtlekit3.warbot.teams.doe.tasks;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class DetectEnemyTask extends Task {

	public DetectEnemyTask(WarBrainController brain, WarAgentType type, Environnement e) {
		super(brain, type, e);
	}

	@Override
	public boolean exec() {
		try {
			for (int i = 0; i < 3; i++) {
				Vector2 myPosition = e.getStructWarBrain(brain.getID()).getPosition();
				for (WarPercept p : brain.getPercepts()) {
					if (!p.getTeamName().equals(brain.getTeamName())) {
						int id = p.getID();
						sender.updatePositionOfEnemy(id, Tools.getPositionOfEntityFromMine(myPosition, (float) p.getAngle(), (float) p.getDistance()), p.getHealth(), p.getType());
					}
				}
				brain.setHeading(brain.getHeading() + 120);
			}
		} catch (Exception e) {
		}
		return true;
	}

}
