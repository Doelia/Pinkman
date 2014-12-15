package protos.tasks.explorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import protos.WarExplorerBrainController;
import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import protos.role.BaseRole;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class Collector extends Task
{
	
	private static Task instance;
	
	@Override
	public ResultTask execute(WarBrainController wa)
	{
		String r = null;
		wa.getBrain().setDebugString(this.getClass().getSimpleName());
		// architecture réactive
		// subsumption
		if(wa.getBrain().isBagFull())//AND SEE A BASE
			r = giveToBase(wa);
		if (!wa.getBrain().isBagEmpty() && r == null)
			r = giveFoodNearRequestOfYou(wa);
		if (!wa.getBrain().isBagFull() && r == null)// pas de sac plein
			r = getFood(wa);
		if (!wa.getBrain().isBagFull() && r == null)// pas de sac plein
			r = lookForFood(wa);
		if (r == null)
			r = giveToBase(wa);
		
		if (r == null)
			r = MovableWarAgent.ACTION_MOVE;
		if (((WarExplorerBrainController) wa).getBrain().isBlocked())
			wa.getBrain().setRandomHeading();
		return new ResultTask(this, r);
	}
	
	private String giveToBase(WarBrainController wa)
	{
		List<WarPercept> percepts = wa.getBrain().getPerceptsAlliesByType(
				WarAgentType.WarBase);
		
		if (percepts != null & percepts.size() > 0)
		{
			WarPercept wp = percepts.get(0);
			wa.getBrain().setDebugString
			(this.getClass().getSimpleName().concat("give food to base"));
			if (wp.getDistance() > WarExplorer.MAX_DISTANCE_GIVE)
			{
				wa.getBrain().setHeading(wp.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			} else
			{
				wa.getBrain().setIdNextAgentToGive(wp.getID());
				return MovableWarAgent.ACTION_GIVE;
			}
		}
		
		for (WarMessage wm : wa.getBrain().getMessages())
		{
			if (wm.getMessage().equals(ProtosCommunication.PING))
			{
				if (wm.getContent().length > 0
						&& wm.getContent()[0].equals(BaseRole.HEADQUARTERS))
				{
					wa.getBrain().setDebugString
					(this.getClass().getSimpleName().concat("give food to base"));
					wa.getBrain().setHeading(wm.getAngle());
					return MovableWarAgent.ACTION_MOVE;
				}
			}
		}
		
		return null;
	}
	
	private String giveFoodNearRequestOfYou(WarBrainController wa)
	{
		List<WarMessage> l = wa.getBrain().getMessages();
		
		if (l != null & !l.isEmpty())
		{
			List<WarMessage> askFood = new ArrayList<>();
			for (WarMessage wm : l)
			{
				if (wm.getMessage().equals(ProtosCommunication.ASK_FOR_FOOD))
				{
					askFood.add(wm);
				}
			}
			if (!askFood.isEmpty())
			{
				wa.getBrain().setDebugString
				(this.getClass().getSimpleName().concat("give food near"));
				for (WarMessage wm : askFood)
				{
					if (wm.getDistance() < WarExplorer.DISTANCE_OF_VIEW)
					{
						wa.getBrain().setIdNextAgentToGive(wm.getSenderID());
						return WarExplorer.ACTION_GIVE;
					}
				}
				
				Collections.sort(askFood, new Comparator<WarMessage>()
				{
					
					@Override
					public int compare(WarMessage o1, WarMessage o2)
					{
						if (o1.getSenderType().equals(WarAgentType.WarBase)
								&& o2.getSenderType().equals(
										WarAgentType.WarBase))
						{
							if (o1.getDistance() < o2.getDistance())
							{
								return 1;
							} else
							{
								return -1;
							}
						} else if (!o1.getSenderType().equals(
								WarAgentType.WarBase)
								|| !o2.getSenderType().equals(
										WarAgentType.WarBase))
						{
							if (!o1.getSenderType()
									.equals(WarAgentType.WarBase))
							{
								return -1;
							} else
							{
								return 1;
							}
						} else
						{
							if (o1.getDistance() < o2.getDistance())
							{
								return 1;
							} else
							{
								return -1;
							}
						}
					}
				});
				
				wa.getBrain().setHeading(askFood.get(0).getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}
		}
		
		return null;
	}
	
	private String getFood(WarBrainController wa)
	{
		
		List<WarPercept> foodPercepts = wa.getBrain().getPerceptsResources();
		
		if (foodPercepts != null && foodPercepts.size() > 0)
		{
			WarPercept foodP = foodPercepts.get(0);
			
			wa.getBrain().setDebugString(
					wa.getBrain().getDebugString().concat(":get food"));
			if (foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE)
			{
				wa.getBrain().setHeading(foodP.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			} else
			{
				TargetMessageWrapper
						.wrapAndSendMessageToAgentType(
								ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION,
								wa.getBrain(), foodP, WarAgentType.WarBase);
				
				return MovableWarAgent.ACTION_TAKE;
			}
		}
		return null;
	}
	
	private String lookForFood(WarBrainController wa)
	{
		wa.getBrain().setDebugString(
				wa.getBrain().getDebugString().concat(":look for food"));
		List<WarMessage> foodMessages = getFoodMessages(wa);
		if (foodMessages.isEmpty())
		{
			if (((WarExplorerBrain) wa.getBrain()).isBlocked())
				wa.getBrain().setRandomHeading();
			return MovableWarAgent.ACTION_MOVE;
		}
		TargetMessageWrapper current = null;
		
		for (WarMessage foodMessage : foodMessages)
		{
			TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(foodMessage);
			CoordPolar cp = tmw.compute();
			if (current == null
					|| cp.getDistance() < current.compute().getDistance())
				current = tmw;
		}
		// Forcément non nul car liste non vide
		
		wa.getBrain().setHeading(current.compute().getAngle());
		
		if (current.compute().getDistance() < WarExplorer.DISTANCE_OF_VIEW)
		{
			
			TargetMessageWrapper.wrapAndSendMessageToAgentType(
					ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION,
					wa.getBrain(), current.compute(), WarAgentType.WarBase);
		}
		
		if (((WarExplorerBrain) wa.getBrain()).isBlocked())
			wa.getBrain().setRandomHeading();
		return MovableWarAgent.ACTION_MOVE;
	}
	
	private List<WarMessage> getFoodMessages(WarBrainController wa)
	{
		List<WarMessage> l = new ArrayList<>();
		for (WarMessage wm : wa.getBrain().getMessages())
		{
			if (wm.getMessage().equals(ProtosCommunication.INFORM_FOUND_FOOD))
			{
				l.add(wm);
			}
		}
		List<WarMessage> dontExistAnymoreFood = new ArrayList<>();
		for (WarMessage wm : wa.getBrain().getMessages())
		{
			if (wm.getMessage().equals(
					ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION))
			{
				for (WarMessage wm2 : l)
				{
					TargetMessageWrapper t = TargetMessageWrapper.unwrap(wm2);
					TargetMessageWrapper t2 = TargetMessageWrapper.unwrap(wm);
					wa.getBrain().setDebugString(
							wa.getBrain().getDebugString()
									.concat(t.compute() + " " + t2.compute()));
					if (t.compute().getAngle() == t2.compute().getAngle()
							&& t.compute().getDistance() == t2.compute()
									.getDistance())
					{
						
						dontExistAnymoreFood.add(wm2);
					}
				}
			}
		}
		l.removeAll(dontExistAnymoreFood);
		
		return l;
	}
	
	public static Task getInstance()
	{
		if (instance == null)
		{
			synchronized (Collector.class)
			{
				if (instance == null)
				{
					instance = new Collector();
				}
			}
		}
		return instance;
	}
	
}
