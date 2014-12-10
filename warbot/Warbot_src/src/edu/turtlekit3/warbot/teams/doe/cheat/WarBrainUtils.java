package edu.turtlekit3.warbot.teams.doe.cheat;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.Tools;

/**
 * 
 * @author swouters
 *	Méthodes lié au cheat ne pouvant pas être utilisées directement dans les brain
 */
public class WarBrainUtils {

	private static WarMessage getMessageFromBase(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase) && m.getMessage().equals(Constants.here))
				return m;
		}

		return null;
	}
	
	public static void doStuff(WarBrain brain, WarAgentType type) {
		updatePositionInEnvironnement(brain, type);
		detectEntityInPercept(brain);
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
	 * Detecte les enemis aux allentour et met à jour l'environnement
	 */
	private static void detectEntityInPercept(WarBrain brain) {
		try {
			Vector2 myPosition = Environnement.getInstance().getStructWarBrain(brain.getID()).getPosition();
			for (WarPercept p : brain.getPercepts()) {
				if (!p.getTeamName().equals(brain.getTeamName())) {
					int id = p.getID();
					Vector2 posCart = Tools.cartFromPolaire(p.getAngle(), p.getDistance());
					posCart.add(myPosition);
					Environnement.getInstance().updatePositionOfEnemy(id, posCart, p.getHealth(), p.getType());
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Fait pointer brain vers la position voulue
	 */
	public static void setHeadingOn(WarBrain brain, Vector2 pos, Vector2 target) {
		Vector2 sortie = new Vector2(0,0);
		sortie.sub(pos);
		sortie.add(target);
		brain.setHeading(-sortie.angle());
	}
}
