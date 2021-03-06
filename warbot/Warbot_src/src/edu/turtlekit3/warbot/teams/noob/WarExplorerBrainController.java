package edu.turtlekit3.warbot.teams.noob;

import java.awt.Color;
import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;
import edu.turtlekit3.warbot.teams.noob.taches.explorer.ChercherEnnemis;
import edu.turtlekit3.warbot.teams.noob.taches.explorer.ChercherNourriture;
import edu.turtlekit3.warbot.teams.noob.taches.explorer.LocaliserBase;


public class WarExplorerBrainController extends WarExplorerAbstractBrainController
{	
	/** Attributs **/
	
	// Action de l'explorer à retourner
	private String toReturn;
	
	// Tache courante
	private TacheAgent tacheCourante;
	
	// Distance de la nourriture indiquée par d'autres agents
	private double distance = 0.0;
	
	// Mode de l'explorer
	private boolean cueilleur;
	
	// Nombre de cueilleurs
	private static int nbCueilleurs = 0;
	
	// Nombre d'espions
	private static int nbEspions = 0;
	
	// Liste de messages
	private ArrayList<WarMessage> messages;
	
	
	/**
	 * Constructeur
	 */
	public WarExplorerBrainController() {
		super();
		
		// Si le nb de cueilleurs < min OU inférieur à 70% du nb d'explorers total
		if (nbCueilleurs < Constants.nbMinExplorer || nbEspions >= 1) {
			tacheCourante = new ChercherNourriture(this);
			nbCueilleurs++;
			cueilleur = true;
		}
		else {
			tacheCourante = new ChercherEnnemis(this);
			nbEspions++;
			cueilleur = false;
		}
	}
	
	public ArrayList<WarMessage> getListeMessages() {
		return this.messages;
	}
	
	public boolean estCueilleur() {
		return this.cueilleur;
	}
	
	/**
	 * @action change le toReturn
	 * */
	public void setToReturn(String nvReturn){
		toReturn=nvReturn;
	}
	
	public void setTacheCourante(TacheAgent nvTache){
		tacheCourante=nvTache;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double nvDistance){
		distance=nvDistance;
	}
	
	
	/**
	 * @action Définit le comportement de l'explorer
	 * @return Action à effectuer (move,take etc...)
	 */
	public String action() 
	{	
		toReturn = null;
		
		this.messages = getBrain().getMessages();
		
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		
		doReflex();
		
		getBrain().setDebugStringColor(Color.black);
		getBrain().setDebugString(tacheCourante.toString());
		
		if(toReturn == null)
			tacheCourante.exec();

		if(toReturn == null) {
			return WarExplorer.ACTION_MOVE;
		} 
		else {
			return toReturn;
		}
	}
	
	
	/**
	 * @action Définit l'ensemble des réflèxes de l'agent
	 */
	private void doReflex()
	{
		refreshExplorersNb();
		
		changeComportement();
		
		perceptEnemyBase();
		
		perceptFood();
		
		imAlive();
	}
	
	
	private void refreshExplorersNb() {
		for (WarMessage m : messages) {
			if (m.getMessage().equals(Constants.noEspion)) {
				nbEspions = 0;
			}
		}
		
		if (nbEspions == 0 && nbCueilleurs >= Constants.nbMinExplorer) {
			if (cueilleur) {
				cueilleur = false;
				tacheCourante = new ChercherEnnemis(this);
				nbCueilleurs--;
				nbEspions++;
			}
		}
	}
	
	
	/**
	 * @action Vérifie les messages de la base et change de comportement
	 * 			si demandé.
	 */
	private void changeComportement()
	{
		for (WarMessage m : messages)
		{
			if (m.getMessage().equals("cueille")) {
				if (!cueilleur) {
					cueilleur = true;
					tacheCourante = new ChercherNourriture(this);
					nbEspions--;
					nbCueilleurs++;
				}
			}
			else if (m.getMessage().equals("chercheEnnemis")) {
				if (cueilleur) {
					cueilleur = false;
					tacheCourante = new ChercherEnnemis(this);
					nbCueilleurs--;
					nbEspions++;
				}
			}
		}
	}
	
	
	/**
	 * @action Prévient la base que l'agent est encore vivant
	 */
	private void imAlive()
	{
		if (estCueilleur()) {
			getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, Constants.imAlive, "c");
		} else {
			getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, Constants.imAlive, "e");
		}
	}
	
	
	/**
	 * @action Envoie un message à sa base et aux rockets launcher s'il perçoit la base ennemie
	 */
	private void perceptEnemyBase()
	{
		ArrayList<WarPercept> basesEnnemies = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		
		if (basesEnnemies != null && basesEnnemies.size() > 0)
		{	
			WarPercept base = basesEnnemies.get(0);
			
			// On envoie la position de la base ennemie
			getBrain().broadcastMessageToAll(Constants.enemyBaseHere, String.valueOf(base.getDistance()), String.valueOf(base.getAngle()));
		
			// Si c'est un espion, on tourne
			if (!estCueilleur()) {
				getBrain().setHeading(base.getAngle() + 45);
				setDistance(base.getDistance());
				setTacheCourante(new LocaliserBase(this));
			}
		}
	}
	
	
	/**
	 * @action Envoie un message aux autres explorers pour avertir qu'il y a de la nourriutre
	 */
	private void perceptFood()
	{
		ArrayList<WarPercept> nourriture = getBrain().getPerceptsResources();
		
		if (nourriture != null && nourriture.size() > 0)
		{
			WarPercept food = nourriture.get(0);
			
			// On envoie un message aux autres explorer pour dire qu'il y a de la nourriture
			getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.foodHere,
					String.valueOf(food.getDistance()), String.valueOf(food.getAngle()));
		}
	}
}
