package edu.turtlekit3.warbot.FSM.plan;

import java.util.ArrayList;

import edu.turtlekit3.warbot.FSM.action.WarAction;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;
import edu.turtlekit3.warbot.brains.WarBrain;

public abstract class WarPlan{

	private WarAction actionCourante;
	private WarAction firstAction;
	private ArrayList<WarAction> actions = new ArrayList<>();
	
	private WarBrain brain;
	
	private String nom;
	
	public WarPlan(WarBrain brain, String nomPlan){
		this.brain = brain;
		this.nom = nomPlan;
	}
	
	/**
	 * Ajoute les actions qui contituent le plan.
	 * Les actions doivent etre li� avec des conditions
	 */
	public abstract void buildActionList();
	
	public void initPlan() {
		this.buildActionList();
		
		if(this.actions.size() < 1){
			System.err.println("ERREUR le plan <" + this.nom + "> ne contient aucune actions � executer");
			System.exit(0);
		}
		
		if(this.firstAction == null){
			this.firstAction = actions.get(0);
			System.out.println("ATTENTION vous devez choisir une action de depart : par defaut la premi�re action ajouter est choisit comme action de d�part <" + this.firstAction.getNom() + ">");
		}
	
		
		this.actionCourante = this.firstAction;
		
		if(this.getBrain().getDebugString() == "")
			this.getBrain().setDebugString(this.getFirstAction().getNom());

		this.printTrace();
		
	}
	
	/**
	 * TODO : doc
	 */
	public String executePlan() {
		
		String instructionResultat;
		
		//On execute l'action
		instructionResultat = this.actionCourante.executeAction();
		
		//On change d'�tat si besoin
		ArrayList<WarCondition> conditions = this.actionCourante.getConditions();
			
		for (WarCondition conditionCourante : conditions) {
			if(conditionCourante.isValide()){
				this.actionCourante = conditionCourante.getActionDestination();
				this.actionCourante.actionWillBegin();
				break;
			}
		}
		
		if(instructionResultat == null){
			System.err.println("ERREUR l'instruction renvoy� par <" + this.actionCourante.getNom() + "> est vide");
			System.exit(0);
		}
		
		return instructionResultat;
	}
	
	private void printTrace() {
		if(!this.printTrace)
			return;
		
		System.out.println("\tLe plan contient <" + this.getNom() + "> contient " + this.actions.size() + " actions");
		
		for (WarAction act : this.actions) {
			System.out.println("\t\tL'action <" + act.getNom() + "> contient " + act.getConditions().size() + " conditions de sortie");
			
			for (WarCondition cond : act.getConditions()) {
				System.out.println("\t\t\tLa condition <" + cond.getClass().getSimpleName() + " a pour destination <" + cond.getActionDestination().getNom() + ">");
			}
		}
		
	}
	

	public void addAction(WarAction a){
		this.actions.add(a);
	}
	
	public WarBrain getBrain(){
		return this.brain;
	}
	
	public String getNom(){
		return this.nom;
	}
	
	public void setFirstAction(WarAction a){
		this.firstAction = a;
	}
	
	public WarAction getFirstAction(){
		return this.firstAction;
	}
	
	public void setPrintTrace(boolean b){
		this.printTrace = b;
	}
	
	public ArrayList<WarAction> getListAction(){
		return this.actions;
	}
	
	private boolean printTrace = false;

}
