package edu.turtlekit3.warbot.FSM.action;

import java.util.ArrayList;

import edu.turtlekit3.warbot.FSM.WarFSMMessage;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarRocketLauncherBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

/**
 * @author Olivier
 *
 */
public class WarActionDefendre extends WarAction{
	
	CoordPolar coordBase;
	
	public WarActionDefendre(WarBrain brain) {
		super(brain);
	}

	public String executeAction(){
		
		//si j'ai pas de missile et que je recharge pas, je recharge
		if(!getBrain().isReloaded() && !getBrain().isReloading()){
			return WarRocketLauncher.ACTION_RELOAD;
		}
		
		ArrayList<WarPercept> perceptEnemy = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);

		// si j'ai un enemy dans mon percept
		if(perceptEnemy != null & perceptEnemy.size() > 0){
			
			// si j'ai rechargé
			if(getBrain().isReloaded()){
				getBrain().setHeading(perceptEnemy.get(0).getAngle());
				return WarRocketLauncher.ACTION_FIRE;
				
			}else{// si j'ai pas rechargé
				if(perceptEnemy.get(0).getDistance() > WarRocket.EXPLOSION_RADIUS + WarRocketLauncher.SPEED + 1){
					getBrain().setHeading(perceptEnemy.get(0).getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}else{
					return MovableWarAgent.ACTION_IDLE;
				}
				
			}
			
		}else{//Si j'ai pas d'enemy dans mon percept
			
			WarMessage m = getMessageFromBase();
			
			if(m != null)
				getBrain().setHeading(m.getAngle());
			
			return MovableWarAgent.ACTION_MOVE;

		}
		
	}
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, WarFSMMessage.whereAreYou, "");
		return null;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
	@Override
	public WarRocketLauncherBrain getBrain(){
		return (WarRocketLauncherBrain)(super.getBrain());
	}

}
