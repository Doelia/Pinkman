package edu.turtlekit3.warbot.FSM;

import java.util.ArrayList;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.FSM.plan.WarPlan;

public class WarEtat {
	
	private WarPlan plan;
	private ArrayList<WarCondition> conditions = new ArrayList<>();
	
	private String nom;
	
	public WarEtat(String nom, WarPlan plan){
		this.nom = nom;
		this.plan = plan;
	}

	public void addCondition(WarCondition cond1) {
		this.conditions.add(cond1);
	}

	public WarPlan getPlan() {
		return this.plan;
	}
	
	public ArrayList<WarCondition> getConditions() {
		return this.conditions;
	}

	public void initEtat() {
		
		if(this.plan == null){
			System.err.println("ERREUR : un etat doit obligatoirement contenir un plan <" + this.nom + ">");
			System.exit(0);
		}
		
		if(this.conditions.size() < 1)
			System.out.println("ATTENTION l'état <" + this.nom + "> ne contient aucune conditions de sortie");
		
		for (WarCondition cond : this.conditions) {
			cond.init();
		}
		
		System.out.println("\tL'état <" + this.getNom() + "> " + "contient le plan <" + this.getPlan().getNom() + ">" + " et " + this.conditions.size() + " conditions de sorties");
		
		this.plan.initPlan();
		
	}

	public String getNom() {
		return this.nom;
	}

}
