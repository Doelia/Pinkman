package edu.turtlekit3.warbot.FSM;

import java.util.ArrayList;

import edu.turtlekit3.warbot.FSM.Reflexe.WarReflexe;
import edu.turtlekit3.warbot.FSM.condition.WarCondition;

public class WarFSM {
	
	
	private WarEtat firstEtat;
	private WarEtat etatCourant;

	private ArrayList<WarEtat> listeEtat = new ArrayList<>();
	private ArrayList<WarReflexe> listeReflexe = new ArrayList<>();
	

	public void initFSM() {
		System.out.println("*** Initialisation de la FSM ***");
		System.out.println("La FSM contient " + this.listeEtat.size() + " �tats");
		
		
		if(this.listeEtat.size() < 1){
			System.err.println("ERREUR La FSM doit contenir au moins 1 �tat !");
			System.exit(0);
		}
		
		if(firstEtat == null){
			this.firstEtat = listeEtat.get(0);
			System.out.println("ATTENTION vous devez choisir un �tat de depart : par defaut le premier �tat ajouter est choisit comme �tat de d�part <" + this.firstEtat.getNom() + ">");
		}
	
		this.etatCourant = firstEtat;
		
		for (WarEtat e : this.listeEtat) {
			e.initEtat();
		}
	
		
	}
	
	public String executeFSM(){
		String actionResultat = null;
		
		if(this.listeReflexe == null | this.listeEtat == null | this.etatCourant == null){
			System.err.println("La FSM � des pointeur null etes vous sur de l'avoir ben initialis� ?");
			System.exit(0);
		}
		
		//On execute les reflexes
		for (WarReflexe r : this.listeReflexe) {
			actionResultat = r.executeReflexe();
		}
		
		//On execute le plan courant
		if(actionResultat == null)
			actionResultat = this.etatCourant.getPlan().executePlan();

		//On change d'�tat si besoin
		ArrayList<WarCondition> conditions = this.etatCourant.getConditions();
			
		for (WarCondition conditionCourante : conditions) {
			if(conditionCourante.isValide()){
				this.etatCourant = conditionCourante.getEtatDestination();
				break;
			}
		}
		
		return actionResultat;
		
	}

	public void addEtat(WarEtat e) {
		if(this.listeEtat.contains(e))
			System.out.println("ATTENTION le plan <" + e.getNom() + "> a deja ete ajoute a la FSM");
		this.listeEtat.add(e);
		
	}
	
	public void setFirstEtat(WarEtat e){
		this.firstEtat = e;
	}

	public void addReflexe(WarReflexe r) {
		this.listeReflexe.add(r);
	}

}
