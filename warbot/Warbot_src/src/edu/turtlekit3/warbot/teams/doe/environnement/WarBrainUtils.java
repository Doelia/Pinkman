package edu.turtlekit3.warbot.teams.doe.environnement;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;

public class WarBrainUtils {

	private static WarMessage getMessageFromBase(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase) && m.getMessage().equals(Constants.here))
				return m;
		}

		return null;
	}

	public static void updatePositionInEnvironnement(WarBrain brain) {
		try {
			WarMessage m = getMessageFromBase(brain);
			double angle = m.getAngle();
			double distance = m.getDistance();
			if (angle != Double.NaN && distance != Double.NaN) {
				Environnement.getInstance().updatePosition(brain, Environnement.cartFromPolaire(angle, distance));
			}
		} catch (NullPointerException e) {
		}
	}

	public static void setHeadingOn(WarBrain brain, Vector2 pos, Vector2 target) {
		Vector2 sortie = new Vector2(0,0);
		sortie.sub(pos);
		sortie.add(target);
		brain.setHeading(-sortie.angle());
		/*double coteOpp = pos.x - target.x;
		double coteAdj = target.y - pos.y;
		System.out.println(coteOpp+"/"+coteAdj+"="+coteOpp/coteAdj);
		double angle = Math.atan(coteOpp/coteAdj);*/
	}
}
