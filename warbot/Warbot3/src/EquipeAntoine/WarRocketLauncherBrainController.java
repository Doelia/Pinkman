package EquipeAntoine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.WarKamikazeBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;



public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {
	
	private String toReturn;
	private TaskRocketLauncher ctask;
	
	private int idTask = Constants.NO_TASK;
	
	//Attributs pour la tactique de contournement
	private boolean contourne = true;
	private double lastHeading = Constants.NO_HEADING;
	
	private double veryLastHeading = Constants.NO_HEADING;
	
	private HashMap<Timer,Integer> timers;
	private ArrayList<WarMessage> messages;
	
	private static TaskRocketLauncher searchForEnemy = new TaskRocketLauncher(){
		public void exec(WarRocketLauncherBrainController b){
			b.searchForEnemy();
		}
	};
	
	private static TaskRocketLauncher goToEnemyBase = new TaskRocketLauncher(){
		public void exec(WarRocketLauncherBrainController b){
			b.goToEnemyBase();
		}
	};
	
	private static TaskRocketLauncher attackEnemyTankDistance = new TaskRocketLauncher(){
		public void exec(WarRocketLauncherBrainController b){
			b.attackEnemyTankDistance();
		}
	};
	
	
	
	public WarRocketLauncherBrainController() {
		super();
		ctask = searchForEnemy;
		toReturn = null;
		messages = new ArrayList<WarMessage>();
		
		//timers
		timers = new HashMap<Timer,Integer>();
		timers.put(Timer.WAITMESSAGE, 0);
		timers.put(Timer.CONTOURNE, 0);
	}
	
	
	@Override
	public String action() {
		
		toReturn = null;
		messages = getBrain().getMessages();
		
		//REFLEXES
		reflexes();
		
		if (toReturn != null){
			return toReturn;
		}
		
		//FSM
		ctask.exec(this);
		
		return toReturn;
	}
	
	
	private void reflexes(){
		//On recharge si pas recharg�
		if(!getBrain().isReloaded() && !getBrain().isReloading()){
			toReturn =  WarRocketLauncher.ACTION_RELOAD;
			return;
		}
		
		//Si on est en phase de contourner un allie, on execute cette partie en priorite
		if (contourneAllie()){
			toReturn =  WarRocketLauncher.ACTION_MOVE;
			return;
		}
		
		//Si un allie est juste devant nous, on s'apprete � le contourner, sinon si il y a un ennemie devant on l'attaque
		ArrayList<WarPercept> percepts = getBrain().getPerceptsAllies();
		if (percepts != null && percepts.size() > 0 && percepts.get(0).getDistance() < 10){
			contourne = true;
			lastHeading = getBrain().getHeading();
			getBrain().setHeading(getBrain().getHeading()+90);
			toReturn = WarRocketLauncher.ACTION_MOVE;
		}
		else {
			attackEnemyBase();
			attackTank();
		}
	}
	
	
	/**
	 * Si c'est un reflexe, on reprend la tache courante apr�s avor attaquer l'ennemie, par contre, si c'est un etat de la FSM
	 * on prend bien soin de repasser en recher d'ennemie apres que la tache soit effectuee
	 */
	private void attackEnemyBase(){
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		if(percept != null && percept.size() > 0){
			
			//INFO CONSOLE
			afficher_etat("Attack enemy Base", Color.blue);
			
			if(getBrain().isReloaded()){
				getBrain().setHeading(percept.get(0).getAngle());
				toReturn = WarRocketLauncher.ACTION_FIRE;
			}
			else{
				//si je suis pas trop pres de l'enemie je m'approche
				if(percept.get(0).getDistance() > WarRocket.EXPLOSION_RADIUS + 10)
					toReturn = WarRocketLauncher.ACTION_MOVE;
				else
					toReturn = WarRocketLauncher.ACTION_IDLE;
			}
		}
		/*else if (ctask == attackEnemyBase){
			ctask = searchForEnemy;
		}*/
	}
	
	
	private void attackTank() {
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if(percept != null && percept.size() > 0){
			
			//INFO CONSOLE
			afficher_etat("Attack Launchers", Color.blue);
			
			//je le dit aux autres
			getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.enemyTankHere, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
			
			if(getBrain().isReloaded()){
				getBrain().setHeading(percept.get(0).getAngle());
				toReturn = WarRocketLauncher.ACTION_FIRE;
				return;
			}
			else {
				//si je suis pas trop pres de l'enemy je m'approche
				if(percept.get(0).getDistance() > WarRocket.EXPLOSION_RADIUS + 1)
					toReturn = WarRocketLauncher.ACTION_MOVE;
				else
					toReturn = WarRocketLauncher.ACTION_IDLE;
			}
			toReturn = WarRocketLauncher.ACTION_IDLE;
		}
	}
	
	
	/**
	 * Etat de disponibilit�, en recherche d'ennemie
	 */
	private void searchForEnemy(){
		//INFO CONSOLE
		afficher_etat("Search for enemy", Color.black);
		
		//MESSAGES
		handleMessages();
		
		wiggle();
	}
	
	
	/**
	 * Etat d'aller vers la base enemie rep�r�e par un explorer
	 */
	private void goToEnemyBase(){
		//INFO CONSOLE
		afficher_etat("Go to enemy Base", Color.yellow);
		
		WarMessage m = getMessageAboutAttackEnemyBase();
		if (m!=null){
			getBrain().sendMessage(m.getSenderID(), Constants.accept, (String []) null);
			reinitialiser(Timer.WAITMESSAGE);
			getBrain().setHeading(getBrain().getIndirectPositionOfAgentWithMessage(m).getAngle());
			toReturn = WarRocketLauncher.ACTION_MOVE;
		}
		else if (timers.get(Timer.WAITMESSAGE) < Constants.MAX_TIMER_TANK_WAIT_MESSAGE){
				incrementer(Timer.WAITMESSAGE);
				wiggle();
		}
		else {
			reinitialiser(Timer.WAITMESSAGE);
			ctask = searchForEnemy;
			wiggle();
		}
	}
	
	
	/**
	 * Etat d'attaque d'un enemie rep�r� par un coequipier
	 */
	private void attackEnemyTankDistance(){
		//INFO CONSOLE
		afficher_etat("attack enemy Tank distance", Color.green);
		
		WarMessage m = this.getMessageAboutEnemyTankHere();
		if (m!=null && idTask==m.getSenderID()){
			reinitialiser(Timer.WAITMESSAGE);
			CoordPolar posTank = getBrain().getIndirectPositionOfAgentWithMessage(m);
			getBrain().setHeading(posTank.getAngle());
			
			if (m.getDistance() < Constants.MAX_DISTANCE){
				if(getBrain().isReloaded()){
					toReturn = WarRocketLauncher.ACTION_FIRE;
				}
				else {
					toReturn = WarRocketLauncher.ACTION_IDLE;
				}
			}
			else {
				toReturn = WarRocketLauncher.ACTION_MOVE;
			}
		}
		else if (timers.get(Timer.WAITMESSAGE) < Constants.MAX_TIMER_TANK_WAIT_MESSAGE){
			incrementer(Timer.WAITMESSAGE);
			wiggle();
		}
		else {
			reinitialiser(Timer.WAITMESSAGE);
			idTask = Constants.NO_TASK;
			ctask = searchForEnemy;
			wiggle();
		}
	}
	
	
	private void retournerBase(){
		WarMessage m = getFormatedMessageAboutBaseAlliee();
		if(m != null){
			CoordPolar p = getBrain().getIndirectPositionOfAgentWithMessage(m);
			if (p.getDistance() < 300 ){
			getBrain().setHeading(p.getAngle());
			toReturn = WarRocketLauncher.ACTION_MOVE;
			getBrain().setDebugStringColor(Color.yellow);
			getBrain().setDebugString("Retourne a la base");
			}
		}
	}
	
	
	/**
	 * Traitement des messages quand on n'a pas de tache en cours
	 */
	private void handleMessages(){
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.enemyBaseHere)){
					getBrain().sendMessage(m.getSenderID(), Constants.propose, "");
			}
			
			if(m.getMessage().equals(Constants.attackEnemyBase)){
					getBrain().sendMessage(m.getSenderID(), Constants.accept, "");
					CoordPolar p = getBrain().getIndirectPositionOfAgentWithMessage(m);
					getBrain().setHeading(p.getAngle());
					ctask = goToEnemyBase;
					return;
			}
			
			if(m.getMessage().equals(Constants.enemyTankHere)){
				CoordPolar p = getBrain().getIndirectPositionOfAgentWithMessage(m);
				getBrain().setHeading(p.getAngle());
				idTask = m.getSenderID();
				ctask = attackEnemyTankDistance;
				return;
			}
		}
	}
	
	
	private void wiggle() {
		if(getBrain().isBlocked()){
			getBrain().setRandomHeading();
		}
		
		double angle = getBrain().getHeading() + new Random().nextInt(10) - new Random().nextInt(10);
		
		getBrain().setHeading(angle);
	
		toReturn = MovableWarAgent.ACTION_MOVE;		
	}
	
	
	private boolean contourneAllie(){
		if (contourne){
			if (timers.get(Timer.CONTOURNE)<Constants.MAX_TIMER_DECALAGE){
				incrementer(Timer.CONTOURNE);
				return true;
			}
			else {
				reinitialiser(Timer.CONTOURNE);
				contourne = false;
				getBrain().setHeading(lastHeading);
				lastHeading = Constants.NO_HEADING;
			}
		}
		return false;
	}
	
	
	private void incrementer(Timer t){
		if (timers.containsKey(t)){
			timers.put(t, timers.get(t)+1);
		}
		else {
			System.err.println("Le timer " + t + " n'existe pas");
		}
	}
	
	
	private void reinitialiser(Timer t){
		timers.put(t, 0);
	}
	
	
	private void afficher_etat(String s, Color c){
		getBrain().setDebugStringColor(c);
		getBrain().setDebugString(s);
	}

	
	
	
	
	private WarMessage getMessageAboutEnemyBaseHere(){
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.enemyBaseHere)){
				return m;
			}
		}
		return null;
	}
	
	
	private WarMessage getMessageAboutAttackEnemyBase(){
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.attackEnemyBase)){
				return m;
			}
		}
		return null;
	}
	
	
	private WarMessage getMessageAboutEnemyTankHere() {
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.enemyTankHere)){
				return m;
			}
		}
		return null;
	}
	
	
	private WarMessage getFormatedMessageAboutBaseAlliee(){
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.baseIsAttack) && m.getContent() != null && m.getContent().length == 2){
				return m;
			}
		}
		return null;
	}
}