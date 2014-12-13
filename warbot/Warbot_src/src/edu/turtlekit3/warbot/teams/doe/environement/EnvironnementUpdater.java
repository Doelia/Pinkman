package edu.turtlekit3.warbot.teams.doe.environement;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class EnvironnementUpdater {

	private Environnement e;
	
	public EnvironnementUpdater(Environnement environnement) {
		this.e = environnement;
	}

	private WarMessage getMessageFromBase(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			if (m.getSenderType().equals(WarAgentType.WarBase) && m.getMessage().equals(Constants.here))
				return m;
		}

		return null;
	}
	
	/**
	 * Detecte les ennemis aux alentours et met à jour l'environnement
	 */
	private void detectEnemis(WarBrain brain) {
		try {
			for (int i = 0; i < 3; i++) {
				Vector2 myPosition = e.getStructWarBrain(brain.getID()).getPosition();
				for (WarPercept p : brain.getPercepts()) {
					if (!p.getTeamName().equals(brain.getTeamName())) {
						int id = p.getID();
						e.updatePositionOfEnemy(id, Tools.getPositionOfEntityFromMine(myPosition, (float) p.getAngle(), (float) p.getDistance()), p.getHealth(), p.getType());
					}
				}
				brain.setHeading(brain.getHeading() + 120);
			}
		} catch (Exception e) {
		}
	}
	
	private void detectAllies(WarBrain brain, WarAgentType type) {
		try {
			WarMessage m = getMessageFromBase(brain);
			double angle = m.getAngle();
			double distance = m.getDistance();
			if (angle != Double.NaN && distance != Double.NaN) {
				e.updatePositionOfAlly(
						brain,
						Tools.cartFromPolaire(angle, distance),
						type
						);
			}
		} catch (NullPointerException e) {
		}
	}
	
	public void updateEnvironement(WarBrain brain, WarAgentType type) {
		this.detectAllies(brain, type);
		this.detectEnemis(brain);
	}
}
