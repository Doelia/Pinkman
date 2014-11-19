package edu.turtlekit3.warbot.FSM.plan;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.action.WarActionAttaquer;
import edu.turtlekit3.warbot.FSM.action.WarActionChercherBase;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;

/**
 * @author Olivier
 */
public class WarPlanPatrouiller extends WarPlan{
	
	boolean offensif;
	
	/**
	 * @param brain
	 * @param offensif = true, defensif = false
	 */
	public WarPlanPatrouiller(MovableWarAgentBrain brain, boolean offensif) {
		super(brain, "Plan Patrouiller");
		this.offensif = offensif;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionSeekBase = new WarActionChercherBase(getBrain());
		addAction(actionSeekBase);

		if(this.offensif){
			WarAction actionKillEnemy = new WarActionAttaquer(getBrain(), WarAgentType.WarRocketLauncher);
			addAction(actionKillEnemy);
		
			WarCondition condSeekToKill = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, ">", 1);
			actionSeekBase.addCondition(condSeekToKill);
			condSeekToKill.setDestination(actionKillEnemy);
		
			WarCondition condKillToSeek = new WarConditionPerceptCounter(getBrain(), WarAgentType.WarRocketLauncher, true, "==", 0);
			actionKillEnemy.addCondition(condKillToSeek);
			condKillToSeek.setDestination(actionSeekBase);
		
		}
		setFirstAction(actionSeekBase);
	}
	
}
