package edu.turtlekit3.warbot.FSM.action;

import java.util.ArrayList;

import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.brains.WarBrain;

public abstract class WarAction {
	
	ArrayList<WarCondition> conditions = new ArrayList<>();
	
	private WarBrain brain;
	
	private String nom;
	
	private boolean terminate = false;
	
	public WarAction(WarBrain b){
		this.brain = b;
		this.nom = this.getClass().getSimpleName();
	}
	
	public WarAction(WarBrain b, String nom){
		this.brain = b;
		this.nom = nom;
	}
	
	/**
	 * Méthode appelé a chaque tik où l'action doit etre executé
	 */
	public abstract String executeAction();

	/**
	 * ATTENTION cette méthode doit etre redefinit pour 
	 * avoir acces aux méthodes des agents specfiques sur 
	 * lesquels l'action va s'appliquer
	 */
	public WarBrain getBrain(){
		return this.brain;
	}

	
	public String getNom(){
		return this.nom;
	}

	public ArrayList<WarCondition> getConditions() {
		return this.conditions;
	}
	
	public void setActionTerminate(boolean b){
		this.terminate = b;
	}
	
	public boolean isTerminate(){
		return this.terminate;
	}
	
	public void addCondition(WarCondition cond){
		this.conditions.add(cond);
	}
	
	/**
	 * Méthode appelé avant chaque première execution de l'action
	 */
	public void actionWillBegin(){
		setActionTerminate(false);
		getBrain().setDebugString(this.getClass().getSimpleName());
	}

}
