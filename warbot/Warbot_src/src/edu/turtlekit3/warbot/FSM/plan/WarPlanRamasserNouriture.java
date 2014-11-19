package edu.turtlekit3.warbot.FSM.plan;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.action.WarActionChercherNouriture;
import edu.turtlekit3.warbot.FSM.action.WarActionRaporterNouriture;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.FSM.condition.WarConditionActionTerminate;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;

/**
 * Desciption du plan et de ces actions
 * action cercher nouriture
 * Si j'ai finit => raporter la nouriture	(ajouter si le sac est plaein comme condition)
 * action raporter de la nouriture
 * si j'ai finit => action chercher de la nouriture	(ajouter si le sac est vide comme condition)
 * @author Olivier
 *
 */
public class WarPlanRamasserNouriture extends WarPlan{
	
	public WarPlanRamasserNouriture(MovableWarAgentBrain brain) {
		super(brain, "PlanRammaserNouriture");
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction actionChercheN = new WarActionChercherNouriture(getBrain(), 4);
		addAction(actionChercheN);

		WarAction actionRamenerN = new WarActionRaporterNouriture(getBrain(), 0);
		addAction(actionRamenerN);
		
		WarCondition condStopChercher = new WarConditionActionTerminate(getBrain(), actionChercheN);
		actionChercheN.addCondition(condStopChercher);
		condStopChercher.setDestination(actionRamenerN);
		
		WarCondition condStopRamener = new WarConditionActionTerminate(getBrain(), actionRamenerN);
		actionRamenerN.addCondition(condStopRamener);
		condStopRamener.setDestination(actionChercheN);
		
		setFirstAction(actionChercheN);
	}
	
}
