package edu.turtlekit3.warbot.teams.protos.tasks.explorator;

import java.util.ArrayList;
import java.util.List;

import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.communication.messages.TargetMessageWrapper;
import edu.turtlekit3.warbot.teams.protos.tasks.ResultTask;
import edu.turtlekit3.warbot.teams.protos.tasks.Task;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class Collector extends Task {

	private static Task instance;

	@Override
	public ResultTask execute(WarBrainController wa) {
		String r = null;
		// architecture réactive
		// subsumption
		if (!wa.getBrain().isBagEmpty())
			r = giveFoodNearRequestOfYou(wa);

		if (!wa.getBrain().isBagFull() && r == null)//pas de sac plein
			r = getFood(wa);
		if (!wa.getBrain().isBagFull() && r == null)//pas de sac plein
			r = lookForFood(wa);
		if (r == null)
			r = giveToBase(wa);
		
		if(r == null)
			r = MovableWarAgent.ACTION_MOVE;
		
		return new ResultTask(this, r);
	}

	private String giveToBase(WarBrainController wa) {
		// TODO Auto-generated method stub
		return null;
	}

	private String giveFoodNearRequestOfYou(WarBrainController wa) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFood(WarBrainController wa) {
		List<WarPercept> foodPercepts = wa.getBrain().getPerceptsResources();

		if (foodPercepts != null && foodPercepts.size() > 0) {
			WarPercept foodP = foodPercepts.get(0);

			if (foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE) {
				wa.getBrain().setHeading(foodP.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			} else {
				return MovableWarAgent.ACTION_TAKE;
			}
		}
		return null;
	}

	private String lookForFood(WarBrainController wa) {
		List<WarMessage> foodMessages = getFoodMessages(wa);
		if(foodMessages.isEmpty())
		{	
			if(((WarExplorerBrain) wa.getBrain()).isBlocked())
				wa.getBrain().setRandomHeading();
			return MovableWarAgent.ACTION_MOVE;
		}
		TargetMessageWrapper current = null;
		
		for(WarMessage foodMessage : foodMessages)
		{
			TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(foodMessage);
			CoordPolar cp = tmw.compute();
			if(current== null || cp.getDistance() < current.compute().getDistance())
				current = tmw;
		}
		//Forcément non nul car liste non vide
		
		wa.getBrain().setHeading(current.compute().getAngle());
		if(((WarExplorerBrain) wa.getBrain()).isBlocked())
			wa.getBrain().setRandomHeading();
		return MovableWarAgent.ACTION_MOVE;
	}

	private List<WarMessage> getFoodMessages(WarBrainController wa) {
		List<WarMessage> l = new ArrayList<>();
		for(WarMessage wm : wa.getBrain().getMessages())
		{
			if(wm.getMessage().equals(ProtosCommunication.INFORM_FOUND_FOOD))
			{
				l.add(wm);
			}
		}
		return l;
	}

	public static Task getInstance()
	{
		if (instance == null) {
			synchronized (Collector.class) {
				if (instance == null) {
					instance = new Collector();
				}
			}
		}
		return instance;
	}

}
