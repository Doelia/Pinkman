package edu.turtlekit3.warbot.FSM.plan;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.action.WarActionChercherNouriture;
import edu.turtlekit3.warbot.FSM.action.WarActionHeal;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.FSM.condition.WarConditionActionTerminate;
import edu.turtlekit3.warbot.FSM.condition.WarConditionAttributCheck;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.WarBrain;

/**
 * Desciption du plan et de ces actions
 * ATTENTION ce plan est bas� sur la size bag d'un explorer et la la vie max d'un explorer
 * @author Olivier
 *
 */
public class WarPlanHealer extends WarPlan{
	
	boolean healAlly = true;
	
	int pourcentageLife;
	
	int pourcentageLifeAlly;

	public WarPlanHealer(WarBrain brain, int  pourcentageLife) {
		this(brain, pourcentageLife, -1);
		this.healAlly = false;
	}
	
	public WarPlanHealer(WarBrain brain, int  lifePourcentage,
			int lifePourcentageAlly) {
		super(brain, "Plan healer");
		
		this.pourcentageLife = lifePourcentage;
		this.pourcentageLifeAlly = lifePourcentageAlly;
		
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionHeal = new WarActionHeal(getBrain(), this.pourcentageLife, 
				this.pourcentageLifeAlly, healAlly);
		addAction(actionHeal);
		
		WarAction actionFindFood = new WarActionChercherNouriture(getBrain(), WarExplorer.BAG_SIZE);
		addAction(actionFindFood);
		
		WarCondition condTerminateHeal = new WarConditionActionTerminate(getBrain(), actionHeal);
		condTerminateHeal.setDestination(actionFindFood);
		actionHeal.addCondition(condTerminateHeal);
		
		WarCondition condTerminateFindFood = new WarConditionActionTerminate(getBrain(), actionFindFood);
		condTerminateFindFood.setDestination(actionHeal);
		actionFindFood.addCondition(condTerminateFindFood);
		
		WarCondition condLowLife = new WarConditionAttributCheck(getBrain(), WarConditionAttributCheck.HEALTH, "<", WarExplorer.MAX_HEALTH, 50);
		condLowLife.setDestination(actionHeal);
		actionFindFood.addCondition(condLowLife);
		
		setFirstAction(actionHeal);
	}
	
}
