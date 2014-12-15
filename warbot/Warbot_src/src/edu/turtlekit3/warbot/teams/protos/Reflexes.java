package edu.turtlekit3.warbot.teams.protos;

import static edu.turtlekit3.warbot.teams.protos.tools.ToolsForWarAgent.addHeading;

import java.util.List;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.communication.messages.TargetMessageWrapper;

public class Reflexes {

	public static String allReflexes(WarBrainController warController,
			WarAgentType wat) {
		String move = null;
		if (!(wat.equals(WarAgentType.WarBase) || wat
				.equals(WarAgentType.WarTurret)) ) {
			move = dodgeMissile(warController);
		}

		detectBases(warController);
		detectFoods(warController);

		return move;
	}

	public static String dodgeMissile(WarBrainController warController) {
		
		
		List<WarPercept> l = warController.getBrain().getPerceptsEnemiesByType(
				WarAgentType.WarRocket);
		
		
		if(l!=null && l.size() > 0) {
			List<WarPercept> le = warController.getBrain().getPerceptsAlliesByType(
					WarAgentType.WarRocket);
			if(le!=null)
				l.addAll(le);
			
			WarPercept minWap = l.get(0);
			double distMinWap = l.get(0).getDistance();
			
			for(WarPercept wp : l){
				if(distMinWap > wp.getDistance()) {
					minWap = wp;
					distMinWap = wp.getDistance();
				}
			}
			
			double enemieAngle = minWap.getAngle();
			
			if (enemieAngle >= 0 && enemieAngle < 180)
				addHeading(warController, 45);
			else
				addHeading(warController, 45 * -1);
			return MovableWarAgent.ACTION_MOVE;
		}
		return null;
		
	}

	public static void detectBases(WarBrainController warController) {
		List<WarPercept> l = warController.getBrain().getPerceptsEnemiesByType(
				WarAgentType.WarBase);

		if (l != null) {
			for (WarPercept wp : l) {
				TargetMessageWrapper.wrapAndSendMessageToAgentType(
						ProtosCommunication.INFORM_FOUND_ENEMY_BASE,
						warController.getBrain(), wp, WarAgentType.WarBase);
			}
		}
	}

	public static void detectFoods(WarBrainController warController) {
		List<WarPercept> l = warController.getBrain().getPerceptsResources();
		if (l != null) {
			for (WarPercept wp : l) {
				TargetMessageWrapper.wrapAndSendMessageToAgentType(
						ProtosCommunication.INFORM_FOUND_FOOD,
						warController.getBrain(), wp, WarAgentType.WarBase);
				;
			}
		}
	}
}
