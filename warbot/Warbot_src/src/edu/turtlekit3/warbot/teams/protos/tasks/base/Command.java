package edu.turtlekit3.warbot.teams.protos.tasks.base;

import edu.turtlekit3.warbot.teams.protos.WarBaseBrainController;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.communication.messages.ContractWrapper;
import edu.turtlekit3.warbot.teams.protos.communication.messages.TargetMessageWrapper;
import edu.turtlekit3.warbot.teams.protos.contract.Contract;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.teams.protos.tasks.kamikaze.GoKillYourselfOnEnemyBase;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class Command extends Task
{
	
	private static Task instance;
	
	@Override
	public ResultTask execute(WarBrainController wa)
	{
		WarBaseBrainController w = (WarBaseBrainController) wa;
		// initStats seulement quand besoin
		if (!w.haveStats())
			w.startStats();
		
		basicCommunication(w);
		
		askCommunication(w);
		
		
		if(w.getStats().haveEnemyBaseTarget())
		{
			TargetMessageWrapper.wrapAndSendMessageToAll
			(ProtosCommunication.REQUEST_ATTACK,wa.getBrain(),w.getStats().getNearEnemyBase() );
		}
		
		// Contract Handler
		/*
		 * else if(wm.getMessage().equals(ProtosCommunication.ASK_ENEMY_BASE)) {
		 * if(w.getStats().haveEnemyBaseTarget()) {
		 * TargetMessageWrapper.wrapAndSendMessageToID
		 * (ProtosCommunication.INFORM_ENEMY_BASE, w.getBrain(),
		 * w.getStats().getNearEnemyBase(), wm.getSenderID()); } } else
		 * if(wm.getMessage().equals(ProtosCommunication.INFORM_TAKE_CONTRACT))
		 * { int idContract = Integer.parseInt(wm.getContent()[0]);
		 * if(w.getContractManager().existContractWithID(idContract) &&
		 * w.getContractManager
		 * ().isStillAccessible(idContract,wm.getSenderType()) ) {
		 * w.getContractManager().
		 * addContractorOnContract(idContract,wm.getSenderType
		 * (),wm.getSenderID()); } else {
		 * 
		 * } }
		 */
		
		return new ResultTask(this, WarBase.ACTION_IDLE);
	}
	
	public static Task getInstance()
	{
		if (instance == null)
		{
			synchronized (Command.class)
			{
				if (instance == null)
					instance = new Command();
			}
		}
		return instance;
	}
	
	public void basicCommunication(WarBaseBrainController w)
	{
		// basic communication
		for (WarMessage wm : w.getBrain().getMessages())
		{
			if (wm.getMessage().equals(ProtosCommunication.ASK_HQ_ID))
			{// Gestion des demandeurs de HQ
				w.getBrain().reply(wm, ProtosCommunication.INFORM_HQ_DECLARED);
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_MY_TYPE))
			{// Gestion des nouveaux arrivants
				w.getStats().addTypeToStats(wm.getSenderType());
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_FOUND_ENEMY_BASE))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getStats().addEnemyBaseCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_DESTROY_ENEMY_BASE))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getStats().addDestroyedBaseCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.ASK_HQ_POSITION))
			{
				w.getBrain().reply(wm, ProtosCommunication.INFORM_HQ_POSITION);
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_FOUND_FOOD))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getStats().addPossibleFoodCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getStats().removePossibleFoodCoord(tmw.compute());
			}
		}
	}
	
	public void askCommunication(WarBaseBrainController w)
	{
		for (WarMessage wm : w.getBrain().getMessages())
		{
			if (wm.getMessage()
					.equals(ProtosCommunication.INFORM_WITHOUT_ORDER))
			{
				// TODO PUT IT IN METHOD
				//TODO HANDLING UNBUSY PEOPLE
				if (wm.getSenderType().equals(WarAgentType.WarKamikaze))
				{
					// give him a blank contract with a simple task
					Contract c = new Contract();
					c.setLevel(0);
					c.setTaskName(GoKillYourselfOnEnemyBase.class
							.getSimpleName());
					c.need(WarAgentType.WarKamikaze, 1);
					if (w.getStats().haveEnemyBaseTarget())
					{
						CoordPolar cp = w.getStats().getNearEnemyBase();
						c.addContractArgument("" + cp.toCartesian().getX());
						c.addContractArgument("" + cp.toCartesian().getY());
					}
					// Donne à l'agent un contrat s'il ne glande rien
					ContractWrapper.wrapAndSendMessageToID(c, w.getBrain(),
							wm.getSenderID());
					
					// w.getContractManager().addContractWithContractor(c,wm.getSenderID());
				} else if (wm.getSenderType().equals(WarAgentType.WarBase))
				{
					// wm.get
				}
				
			} else if (wm.getMessage().equals(
					ProtosCommunication.ASK_ENEMY_BASE_POSITION))
			{ //Gestion des demandes de localisation bases enemies
				if (w.getStats().haveEnemyBaseTarget())
				{
					TargetMessageWrapper.wrapAndSendMessageToID(
							ProtosCommunication.INFORM_ENEMY_BASE,
							w.getBrain(), w.getStats().getNearEnemyBase(),
							wm.getSenderID());
				}
			}
		}
	}
}
