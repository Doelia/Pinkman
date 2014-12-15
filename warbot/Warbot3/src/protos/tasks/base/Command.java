package protos.tasks.base;

import protos.Nexus;
import protos.communication.ProtosCommunication;
import protos.communication.messages.ContractWrapper;
import protos.communication.messages.TargetMessageWrapper;
import protos.contract.Contract;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import protos.tasks.kamikaze.GoKillYourselfOnEnemyBase;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class Command extends Task<Nexus>
{
	
	private static Task<Nexus> instance;
	
	@Override
	public ResultTask execute(Nexus w)
	{
		w.getBrain().setDebugString(getClass().getSimpleName());
	
		
		// initStats seulement quand besoin
		if (!w.haveCharts())
			w.startCharts();
		
		w.getCharts().handleStats(w.getBrain().getMessages());
		w.getCharts().handleInformationTargets(w.getBrain().getMessages());
		
		basicCommunication(w);
		
		askCommunication(w);
		
		
		if(w.getCharts().haveEnemyBaseTargets())
		{
		
			
			for(CoordPolar cp :w.getCharts().getEnemyBaseTargets())
			{
				TargetMessageWrapper.wrapAndSendMessageToAll
				(ProtosCommunication.INFORM_ENEMY_BASE,w.getBrain(),cp );
			}
			
		}
		
		
		
		if(w.getCharts().haveFoodTargets())
		{
			for(w.getCharts().getFoodLocalisations())
			TargetMessageWrapper.wrapAndSendMessageToAgentType
			(ProtosCommunication.INFORM_FOUND_FOOD, w.getBrain(),w.getCharts().getNearestFood(),WarAgentType.WarExplorer);
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
		if(w.getBrain().getHealth() >  WarBase.MAX_HEALTH*0.6)
		{
			w.getBrain().setNextAgentToCreate(WarAgentType.WarRocketLauncher);
			return new ResultTask(this, WarBase.ACTION_CREATE);
		}
		
		return new ResultTask(this, WarBase.ACTION_IDLE);
	}
	
	public static Task<Nexus> getInstance()
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
	
	
	public void basicCommunication(Nexus w)
	{
		// basic communication
		for (WarMessage wm : w.getBrain().getMessages())
		{
			if (wm.getMessage().equals(ProtosCommunication.ASK_HQ_ID))
			{// Gestion des demandeurs de HQ
				w.getBrain().reply(wm, ProtosCommunication.INFORM_HQ_DECLARED);
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_FOUND_ENEMY_BASE))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getCharts().addEnemyBaseCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_DESTROYED_ENEMY_BASE))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getCharts().addDestroyedBaseCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.ASK_HQ_POSITION))
			{
				w.getBrain().reply(wm, ProtosCommunication.INFORM_HQ_POSITION);
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_FOUND_FOOD))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getCharts().addPossibleFoodCoord(tmw.compute());
			} else if (wm.getMessage().equals(
					ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION))
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
				w.getCharts().removePossibleFoodCoord(tmw.compute());
			}
		}
	}
	
	public void askCommunication(Nexus w)
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
					if (w.getCharts().haveEnemyBaseTargets())
					{
						CoordPolar cp = w.getCharts().getNearestEnemyBase();
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
				if (w.getStats().haveEnemyBaseTargets())
				{
					TargetMessageWrapper.wrapAndSendMessageToID(
							ProtosCommunication.INFORM_ENEMY_BASE,
							w.getBrain(), w.getStats().getNearestEnemyBase(),
							wm.getSenderID());
				}
			}
		}
	}
}
