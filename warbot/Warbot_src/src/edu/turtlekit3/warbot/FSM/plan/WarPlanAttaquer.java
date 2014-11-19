package edu.turtlekit3.warbot.FSM.plan;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.action.WarActionAttaquer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;

/**
 * @author Olivier
 *
 */
public class WarPlanAttaquer extends WarPlan{
	
	WarAgentType agentType;
	
	public WarPlanAttaquer(MovableWarAgentBrain brain, WarAgentType agentType) {
		super(brain, "Plan Attaquer");
		this.agentType = agentType;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionAttaquer = new WarActionAttaquer(getBrain(), this.agentType);
		addAction(actionAttaquer);
		
		setFirstAction(actionAttaquer);
	}
	
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)super.getBrain();
	}
	
}
