package edu.turtlekit3.warbot.teams.doe.tools;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.environement.StructWarBrainAllie;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

/**
 * 
 * @author swouters
 *	Méthodes lié au cheat ne pouvant pas être utilisées directement dans les brain
 */
public class WarBrainUtils {

	private static WarMessage getMessageFromBase(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			if (m.getSenderType().equals(WarAgentType.WarBase) && m.getMessage().equals(Constants.here))
				return m;
		}

		return null;
	}

	public static void doStuff(WarBrain brain, WarAgentType type) {
		updatePositionInEnvironnement(brain, type);
		detectEntityInPercept(brain);
		try {
			StructWarBrainAllie s = Environnement.getInstance().getStructWarBrain(brain.getID());
			Behavior.heal(s.getLastLife(), brain);
			s.setLastLife();
		} catch (NotExistException e) {
		}
	}

	private static void updatePositionInEnvironnement(WarBrain brain, WarAgentType type) {
		try {
			WarMessage m = getMessageFromBase(brain);
			double angle = m.getAngle();
			double distance = m.getDistance();
			if (angle != Double.NaN && distance != Double.NaN) {
				Environnement.getInstance().updatePositionOfAlly(
						brain,
						Tools.cartFromPolaire(angle, distance),
						type
						);
			}
		} catch (NullPointerException e) {
		}
	}

	
	/**
	 * Detecte les ennemis aux alentours et met à jour l'environnement
	 */
	private static void detectEntityInPercept(WarBrain brain) {
		try {
			for (int i = 0; i < 3; i++) {
				Vector2 myPosition = Environnement.getInstance().getStructWarBrain(brain.getID()).getPosition();
				for (WarPercept p : brain.getPercepts()) {
					if (!p.getTeamName().equals(brain.getTeamName())) {
						int id = p.getID();
						Environnement.getInstance().updatePositionOfEnemy(id, Tools.getPositionOfEntityFromMine(myPosition, (float) p.getAngle(), (float) p.getDistance()), p.getHealth(), p.getType());
					}
				}
				brain.setHeading(brain.getHeading() + 120);
			}
		} catch (Exception e) {
		}
	}
	
	


	
}
