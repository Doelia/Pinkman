package edu.turtlekit3.warbot.FSM.condition;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrain;

public class WarConditionPerceptCounter extends WarCondition{
	
	int reference;
	String operand;
	
	boolean enemy;
	WarAgentType agentType;
	
	/**
	 * 
	 * @param brain
	 * @param agentType
	 * @param enemy
	 * @param operand
	 * @param nameAtt
	 * @param ref
	 */
	public WarConditionPerceptCounter(WarBrain brain, WarAgentType agentType, boolean enemy,
			String operand, int ref) {

		super(brain);
		
		this.agentType = agentType;
		this.operand = operand;
		this.reference = ref;
		
		this.enemy = enemy;
	}
	
	@Override
	public boolean isValide() {

		// Recupere les percepts
		ArrayList<WarPercept> percepts = new ArrayList<>();
		
		if(this.enemy){
			if(this.agentType == null){
				percepts= getBrain().getPerceptsEnemies();
			}else{
				percepts = getBrain().getPerceptsEnemiesByType(this.agentType);
			}
		}else{
			if(this.agentType == null){
				percepts= getBrain().getPerceptsAllies();
			}else{
				percepts = getBrain().getPerceptsAlliesByType(this.agentType);
			}
		}
		
		if(percepts == null || percepts.size() == 0)
			return false;
		
		// Fait les verifications
		int nbPercept = percepts.size();

		switch (this.operand) {
		case "<":
			return nbPercept < this.reference;
		case ">":
			return nbPercept > this.reference;
		case "==":
			return nbPercept == this.reference;
		default:
			System.err.println("FSM : unknown operateur " + this.operand + this.getClass());
			return false;
		}
		
			
	}

}
