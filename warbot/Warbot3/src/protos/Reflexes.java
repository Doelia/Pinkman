package protos;

import static protos.tools.ToolsForWarAgent.addHeading;

import java.util.List;

import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class Reflexes {

	public static String allReflexes(WarBrainController warController,
			WarAgentType wat) {
		String move = null;
		warController.getBrain().setDebugString("test");
		if (!(wat.equals(WarAgentType.WarBase) || wat
				.equals(WarAgentType.WarTurret)) ) {
			move = dodgeMissile(warController);
		}

		detectBases(warController);
		detectFoods(warController);
		move = eatOrAskForHelpToSurvive(warController,wat);
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
	
	public static String eatOrAskForHelpToSurvive(WarBrainController warController,
			WarAgentType wat) {
		
		int maxHealth=0;
		int percent =100;
		if(WarAgentType.WarBase.equals(wat))
		{
			maxHealth = WarBase.MAX_HEALTH;
			percent = 80;
		}
		else if (WarAgentType.WarRocketLauncher.equals(wat))
		{
			maxHealth = WarRocketLauncher.MAX_HEALTH;
			percent = 30;
		}
		else if (WarAgentType.WarEngineer.equals(wat))
		{
			maxHealth = WarEngineer.MAX_HEALTH;
			percent = 30;
		}
		else if (WarAgentType.WarExplorer.equals(wat))
		{
			maxHealth = WarExplorer.MAX_HEALTH;
			percent = 30;
		}
		else
		{
			return null;
		}
		
		if(warController.getBrain().getHealth() < (maxHealth*(percent/100)))
		{
			if(warController.getBrain().isBagEmpty())
			{
				warController.getBrain().broadcastMessageToAll(ProtosCommunication.ASK_FOR_FOOD);
			}
			else
			{
				return MovableWarAgent.ACTION_EAT;
			}
		}
		return null;
	}
	
}
