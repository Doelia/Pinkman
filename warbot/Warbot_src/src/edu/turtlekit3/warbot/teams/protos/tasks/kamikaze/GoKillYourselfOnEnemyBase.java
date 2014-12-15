

package edu.turtlekit3.warbot.teams.protos.tasks.kamikaze;

import static edu.turtlekit3.warbot.teams.protos.tools.ToolsForWarAgent.sameHeading;

import java.util.List;

import edu.turtlekit3.warbot.teams.protos.WarKamikazeBrainController;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosKamikazeCommunication;
import edu.turtlekit3.warbot.teams.protos.communication.messages.TargetMessageWrapper;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarKamikaze;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.projectiles.WarBomb;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;


public class GoKillYourselfOnEnemyBase extends Task{
	
	private static Task instance;
	public static Task getInstance() {
		if (instance == null) {
			synchronized (GoKillYourselfOnEnemyBase.class) {
				if (instance == null) {
					instance = new GoKillYourselfOnEnemyBase();
				}
			}
		}
		return instance;
	}
	
	@Override
	public ResultTask execute(WarBrainController wa) {
		
		WarKamikazeBrainController w = (WarKamikazeBrainController) wa;
	
		List<WarMessage> l = w.getBrain().getMessages();
		WarMessage wmMostNearButNotInRange = null;

		if(w.getBrain().isBlocked())
			w.getBrain().setRandomHeading();
		
		if(l!=null & !l.isEmpty())
		{
			for(WarMessage wm :l)
			{
				if(wm.getMessage().equals(ProtosCommunication.REQUEST_ATTACK))
				{
					TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
		
					CoordPolar cp = tmw.compute();
		
					sameHeading(w, cp);
		
					w.getBrain().setDebugString("Task:GoKillYourselfOnEnemyBase");

						if(cp.getDistance() < WarBomb.EXPLOSION_RADIUS - 1) {
						
							w.getBrain().broadcastMessage("HQ", "HQ", ProtosKamikazeCommunication.INFORM_EXPLOSION_IMMINENT);
							
							if(w.getChrono() == -1) {
								w.initChrono();
							}
							else if(w.getChrono() == 0){
								
								w.getBrain().broadcastMessage("HQ", "HQ", ProtosKamikazeCommunication.INFORM_EXPLOSION_NOW);
								
								return new ResultTask(this, WarKamikaze.ACTION_FIRE);
							}
							else {
								w.deCreChrono();
							}
							
							return new ResultTask(this, MovableWarAgent.ACTION_IDLE);
						}
			
					return new ResultTask(this,WarRocketLauncher.ACTION_MOVE);
				}	
			}
			
			if(wmMostNearButNotInRange!=null)
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wmMostNearButNotInRange);
				CoordPolar cp = tmw.compute();
				sameHeading(w, cp);
				return new ResultTask(this,WarKamikaze.ACTION_MOVE);
			}
			else
			{
				w.getBrain().broadcastMessage("HQ", "HQ", ProtosCommunication.ASK_ENEMY_BASE_POSITION);
				
				return new ResultTask(this, WarKamikaze.ACTION_MOVE);
			}
		}
		
		return new ResultTask(this, MovableWarAgent.ACTION_IDLE);
	}

}

