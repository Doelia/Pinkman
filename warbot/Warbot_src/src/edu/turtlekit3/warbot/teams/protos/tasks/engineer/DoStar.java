package edu.turtlekit3.warbot.teams.protos.tasks.engineer;

import static edu.turtlekit3.warbot.teams.protos.tools.ToolsForWarAgent.addHeading;
import static edu.turtlekit3.warbot.teams.protos.tools.ToolsForWarAgent.engineer;

import java.awt.Color;
import java.util.List;

import edu.turtlekit3.warbot.teams.protos.WarEngineerBrainController;
import edu.turtlekit3.warbot.teams.protos.WarEngineerBrainController.EngineerRole;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class DoStar extends Task {
	
	private static Task instance;
	
	private DoStar()
	{
	}

	@Override
	public ResultTask execute(WarBrainController wa) {
		
		ResultTask rt;
		
		if(engineer(wa).getRole() == EngineerRole.DEFENSER)
			putTurret(engineer(wa));
		else {
			
			// TODO MODE ATTAQUE
		}
			
		rt = followUp(engineer(wa));
		
		return rt;
	}

	private void putTurret(WarEngineerBrainController engineer) {	
		if (engineer.getCurrentTick() == engineer.getReturnTick()) {
			engineer.setLockReturnBase(true);
			engineer.resetCurrentTick();
		}
		else {
			engineer.getBrain().setDebugStringColor(Color.red.darker());
			engineer.getBrain().setDebugString("Engineer go put turret");
		}
	}

	
	private ResultTask followUp(WarEngineerBrainController engineer)
	{
		if (engineer.isLockReturnBase()) {
			
			if (!engineer.isLockTurret()) {
				engineer.setLockTurret(true);
				return createUnit(engineer);
			}else
			{
				return returnBase(engineer);
			}
		} else {
			engineer.addTick();
			return new ResultTask(this, MovableWarAgent.ACTION_MOVE);
		}
	}
	
	private ResultTask createUnit(WarEngineerBrainController engineer) {
		if (engineer.getBrain().getHealth() > engineer.getMinHeathToCreate()) {
			engineer.getBrain().setNextAgentToCreate(WarAgentType.WarTurret);
			engineer.getBrain().setDebugString("Create: " + WarAgentType.WarTurret.name());
			return new ResultTask(this,WarEngineer.ACTION_CREATE);
		}
		
		return new ResultTask(this,MovableWarAgent.ACTION_MOVE);
	}
	
	private ResultTask returnBase(WarEngineerBrainController engineer) {
		
		engineer.getBrain().setDebugStringColor(Color.red.darker());
		engineer.getBrain().setDebugString("Engineer returning Base");

		List<WarPercept> basePercepts = engineer.getBrain().getPerceptsAlliesByType(
				WarAgentType.WarBase);
		
		WarPercept base = null;
		
		for(WarPercept b : basePercepts)
			if(b.getID() == engineer.getBaseId())
				base = b;
			
		// Si je ne voit pas de base
		if (base == null) {
			
			// TODO RECUP LOCALISATION BASE
			
			return new ResultTask(this,MovableWarAgent.ACTION_MOVE);

		} else {// si je vois une base

			if (base.getDistance() > MovableWarAgent.MAX_DISTANCE_GIVE) {
				engineer.getBrain().setHeading(base.getAngle());
				return new ResultTask(this,MovableWarAgent.ACTION_MOVE);
			} else {
				
				if(engineer.getBrain().getHealth() < engineer.getMinHeathToCreate()) {
					
					// TODO se soigner
					
					 
					return new ResultTask(this,MovableWarAgent.ACTION_IDLE);
				}
				else
				{
					addHeading(engineer, 180);
					addHeading(engineer, engineer.getNextAngleForContinue());
					engineer.setLockReturnBase(false);
					engineer.setLockTurret(false);
					return new ResultTask(this,MovableWarAgent.ACTION_MOVE);
				}
			}

		}

	}
	
	public static Task getInstance() {
		if(instance == null)
		{
			synchronized (DoStar.class) {
				if(instance == null)
					instance = new DoStar();
			}
		}
		return instance;
	}
}
