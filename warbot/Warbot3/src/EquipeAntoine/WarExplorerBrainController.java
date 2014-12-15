package EquipeAntoine;

import java.awt.Color;
import java.util.*;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {
	
	private static int role = 0;
	
	private String toReturn;
	private boolean estEspion;
	
	private Task ctaskCueilleur;
	private Task ctaskEspion;
	
	private HashMap<Integer,Integer> listeTankProposer;
	
	private WarPercept agentPercu;
	private int vieBaseEnemie = 0;
	
	private HashMap<Timer,Integer> timers;
	private ArrayList<WarMessage> messages = new ArrayList<WarMessage>();
	
	//FSM
	private static Task searchForFood = new Task(){
		@Override
		public void exec(WarExplorerBrainController b) {
			
			b.agentPercu = null;
			
			//INFO CONSOLE
			b.afficher_etat("searching food", Color.BLUE);
			
			if(b.getBrain().isBagFull()){
				b.toReturn = MovableWarAgent.ACTION_MOVE;
				b.ctaskCueilleur = returnFood;
				return;
			}
			
			if(b.getBrain().isBlocked()){
				b.getBrain().setRandomHeading();
			}
			
			ArrayList<WarPercept> foodPercepts = b.getBrain().getPerceptsResources();
			
			//Si il y a de la nouriture
			if(foodPercepts != null && foodPercepts.size() > 0){
				WarPercept foodP = foodPercepts.get(0); //le 0 est le plus proche normalement
				
				if(foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE){
					b.getBrain().setHeading(foodP.getAngle());
					b.toReturn = MovableWarAgent.ACTION_MOVE;
				}else{
					b.toReturn = MovableWarAgent.ACTION_TAKE;
					b.getBrain().broadcastMessageToAll(Constants.foodHere, (String[]) null);
				}
			} else {
				b.wiggle();
			}
		}
	};
	
	private static Task returnFood = new Task(){
		@Override
		public void exec(WarExplorerBrainController b) {
			if(b.getBrain().isBagEmpty()){
				b.getBrain().setHeading(b.getBrain().getHeading() + 180);
				b.toReturn = MovableWarAgent.ACTION_MOVE;
				b.ctaskCueilleur = searchForFood;
				return;
			}
			
			if(b.getBrain().isBlocked())
				b.getBrain().setRandomHeading();
			
			//INFO CONSOLE
			b.afficher_etat("returning Food", Color.GREEN);

			ArrayList<WarPercept> basePercepts = b.getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);
			
			//Si je ne voit pas de base
			if(basePercepts == null | basePercepts.size() == 0){
				
				WarMessage m = b.getMessageFromBase();
				//Si j'ai un message de la base je vais vers elle
				if(m != null){
					b.getBrain().setHeading(m.getAngle());
				}
				
				//j'envoie un message aux bases pour savoir où elle sont..
				b.getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, Constants.whereAreYou, (String[]) null);
				
				b.toReturn = MovableWarAgent.ACTION_MOVE;
				
			}else{
				//si je vois une base
				WarPercept base = basePercepts.get(0);
				
				if(base.getDistance() > MovableWarAgent.MAX_DISTANCE_GIVE){
					b.getBrain().setHeading(base.getAngle());
					b.toReturn = MovableWarAgent.ACTION_MOVE;
				}else{
					b.getBrain().setIdNextAgentToGive(base.getID());
					b.toReturn = MovableWarAgent.ACTION_GIVE;
				}
				
			}
		}
	};
	
	private static Task searchForEnemy = new Task(){
		
		@Override
		public void exec(WarExplorerBrainController b) {
			
			//INFO CONSOLE
			b.afficher_etat("search for Enemy", Color.RED);
			
			if (b.agentPercu != null){
				if (b.agentPercu.getType().equals(WarAgentType.WarBase)){
					b.toReturn = MovableWarAgent.ACTION_IDLE;
					b.ctaskEspion = warnBaseEnemyHere;
				}
				if (b.agentPercu.getType().equals(WarAgentType.WarRocketLauncher)){
					b.toReturn = MovableWarAgent.ACTION_MOVE;
					b.ctaskEspion = warnTankEnemyHere;
				}
			}
			else {
				b.wiggle();
			}
		}
	};
	
	private static Task warnBaseEnemyHere = new Task(){
		@Override
		public void exec(WarExplorerBrainController b) {
			if (b.agentPercu != null){
				
				//INFO CONSOLE
				b.afficher_etat("warn Base here", Color.YELLOW);
				
				b.getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.alreadyHere, String.valueOf(b.agentPercu.getID()));
				
				if (b.timers.get(Timer.WARNTANK) < Constants.MAX_TIMER_EXPLORER){
					b.getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.enemyBaseHere, (String[]) null);
					b.remplirListeTankProposer();
					b.incrementer(Timer.WARNTANK);
					b.vieBaseEnemie = b.agentPercu.getHealth();
				}
				else {
					b.choisirAttaquantsBaseEnemie();
					if (!b.situationBaseEnemieEvolue()){
						b.reinitialiseListeTankProposer();
					}
				}
				b.toReturn = MovableWarAgent.ACTION_IDLE;
			}
			else {
				b.toReturn = MovableWarAgent.ACTION_MOVE;
				b.ctaskEspion = searchForEnemy;
			}
			
		}
	};
	
	private static Task warnTankEnemyHere = new Task(){
		@Override
		public void exec(WarExplorerBrainController b) {
			if (b.agentPercu != null){
				
				//INFO CONSOLE
				b.afficher_etat("warn Tank here", Color.YELLOW);
				
				b.getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.alreadyHere, String.valueOf(b.agentPercu.getID()));

				b.getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.enemyTankHere, String.valueOf(b.agentPercu.getDistance()), String.valueOf(b.agentPercu.getAngle()));
				
				b.getBrain().setHeading(b.agentPercu.getAngle());
				
				if (b.agentPercu.getDistance() < Constants.MAX_DISTANCE){
					b.toReturn = MovableWarAgent.ACTION_IDLE;
				}
				else {
					b.toReturn = MovableWarAgent.ACTION_MOVE;
				}
			}
			else {
				b.toReturn = MovableWarAgent.ACTION_MOVE;
				b.ctaskEspion = searchForEnemy;
			}
			
		}
	};

	
	public WarExplorerBrainController() {
		super();
		ctaskCueilleur = searchForFood;
		ctaskEspion = searchForEnemy;
		if (role%2 == 0){
			estEspion = true;
		}
		else {
			estEspion = false;
		}
		role++;
		listeTankProposer = new HashMap<Integer,Integer>();
		agentPercu = null;
		
		//timers
		timers = new HashMap<Timer,Integer>();
		timers.put(Timer.WARNTANK, 0);
		timers.put(Timer.SITUATION, 0);
		timers.put(Timer.VEROUILLECIBLE, 0);
		
	}
	

	@Override
	public String action() {
		toReturn = null;
		messages = getBrain().getMessages();
		
		//REFLEXES
		if (estEspion){
			reflexesEspion();
		}
		
		if (toReturn != null){
			return toReturn;
		}
		
		//FSM
		if (estEspion){
			ctaskEspion.exec(this);
		}
		else {
			ctaskCueilleur.exec(this);
		}
		
		return toReturn;
	}
	
	
	
	private void reflexesEspion(){
		determinerAgentPercu();
	}
		
	
	private WarMessage getMessageAboutFood() {
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.foodHere))
				return m;
		}
		return null;
	}
	
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : messages) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, Constants.whereAreYou, "");
		return null;
	}
	
	
	public void remplirListeTankProposer(){
		for (WarMessage m : messages) {
			if(m.getSenderType().equals(WarAgentType.WarRocketLauncher) && m.getMessage().equals(Constants.propose)){
				int id = m.getSenderID();
				int distance = (int) m.getDistance();
					
				listeTankProposer.put(id,distance);
			}
		}
		//System.out.println(listeTankProposer);
	}
	
	
	public void choisirAttaquantsBaseEnemie(){
		int nbElus = 0, elu = -1;
		ArrayList<Integer> elus = new ArrayList<Integer>();
		
		while (nbElus < Constants.NB_TANK_ATTACK_ENEMY_BASE){
			int min = 2000;
			for (Integer i : listeTankProposer.keySet()){
				if (!elus.contains(i) && listeTankProposer.get(i) < min){
					elu = i;
					min = listeTankProposer.get(i);
				}
			}
			if (elu != -1){
				elus.add(elu);
			}
			
			elu = -1;
			nbElus++;
		}
		
		for (Integer e : elus){
			getBrain().sendMessage(e, Constants.attackEnemyBase, String.valueOf(agentPercu.getDistance()), String.valueOf(agentPercu.getAngle()));
		}
	}
	
	
	private boolean situationBaseEnemieEvolue(){
		incrementer(Timer.SITUATION);
		for (WarMessage m : messages){
			if (m.getMessage().equals(Constants.accept)){
				reinitialiser(Timer.SITUATION);
				return true;
			}
		}
		if (timers.get(Timer.SITUATION) > Constants.MAX_TIMER_EXPLORER){
			return false;
		}
		return true;
	}
	
	
	private void reinitialiseListeTankProposer(){
		reinitialiser(Timer.WARNTANK);
		reinitialiser(Timer.SITUATION);
		listeTankProposer = new HashMap<Integer,Integer>();
	}
	
	
	private void wiggle() {
		if(getBrain().isBlocked()){
			getBrain().setRandomHeading();
		}
		double angle = getBrain().getHeading() + new Random().nextInt(10) - new Random().nextInt(10);
		getBrain().setHeading(angle);
		toReturn = MovableWarAgent.ACTION_MOVE;		
	}
	
	
	private void determinerAgentPercu(){
		if (timers.get(Timer.VEROUILLECIBLE) == Constants.MAX_TIMER_VEROUILLE_CIBLE){
		ArrayList<WarPercept> perceptsBase = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		if (perceptsBase != null && perceptsBase.size() > 0){
			agentPercu = perceptsBase.get(0);
			if (peutEtreAgentPercu()){
				return;
			}
		}
		ArrayList<WarPercept> perceptsTank = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if (perceptsTank != null && perceptsTank.size() > 0){
			agentPercu = perceptsTank.get(0);
			if (peutEtreAgentPercu()){
				return;
			}
		}
		}
		else {
			incrementer(Timer.VEROUILLECIBLE);
		}
		agentPercu = null;
	}
	
	
	private void afficher_etat(String s, Color c){
		getBrain().setDebugStringColor(c);
		getBrain().setDebugString(s);
	}
	
	
	private boolean peutEtreAgentPercu(){
		ArrayList<WarMessage> listeMessages = getMessagesAboutAlreadyHere();
		if (listeMessages != null){
			for (WarMessage m : listeMessages){
				if (Integer.parseInt(m.getContent()[0]) == agentPercu.getID()){
					agentPercu = null;
					reinitialiser(Timer.VEROUILLECIBLE);
					return false;
				}
			}
		}
		return true;
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
	
	
	
	
	private ArrayList<WarMessage> getMessagesAboutAlreadyHere() {
		ArrayList<WarMessage> l = new ArrayList<WarMessage>();
		for (WarMessage m : messages) {
			if(m.getMessage().equals(Constants.alreadyHere) && m.getContent() != null){
				l.add(m);
			}
		}
		if (l.size()==0){
			return null;
		}
		return l;
	}
}