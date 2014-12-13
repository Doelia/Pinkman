package edu.turtlekit3.warbot.teams.doe.tasks;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class SendAlliesTask extends Task {

	public SendAlliesTask(WarBrainController brain, WarAgentType type,
			Environnement e) {
		super(brain, type, e);
	}
	
	private WarMessage getMessageFromBase(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			if (m.getSenderType().equals(WarAgentType.WarBase) && m.getMessage().equals(Constants.here))
				return m;
		}

		return null;
	}

	@Override
	public boolean exec() {
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
		return true;
		
	}

}
