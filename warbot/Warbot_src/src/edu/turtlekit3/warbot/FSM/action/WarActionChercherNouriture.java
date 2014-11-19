package edu.turtlekit3.warbot.FSM.action;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;

/**
 * Va chercher de la nouriture
 * @author Olivier
 *
 */
public class WarActionChercherNouriture extends WarAction{
	
	private final int nbMaxElement;	

	public WarActionChercherNouriture(WarBrain brain, int nombreNuritureMax) {
		super(brain);
		this.nbMaxElement = nombreNuritureMax;
	}

	public String executeAction(){
		
		if(getBrain().isBagFull() | getBrain().getNbElementsInBag() >=this.nbMaxElement)
			setActionTerminate(true);
		
		if(getBrain().isBlocked())
			getBrain().setRandomHeading();

		ArrayList<WarPercept> foodPercepts = getBrain().getPerceptsResources();
		
		//Si il ny a pas de nouriture dans le percept
		if(foodPercepts.size() == 0){
			
			WarMessage m = getMessageAboutFood();
			if(m != null){
				getBrain().setHeading(m.getAngle());
			}
			
			//Sinon je vais tout droit
			return MovableWarAgent.ACTION_MOVE;
				
		}else{//Si il y a de la nouriture
			WarPercept foodP = foodPercepts.get(0); //le 0 est le plus proche normalement
			
			//si il y a beaucoup de nourriture je previens mes allié
			if(foodPercepts.size() > 1)
				getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, "foodHere", "");
			
			if(foodP.getDistance() > ControllableWarAgent.MAX_DISTANCE_GIVE){
				getBrain().setHeading(foodP.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else{
				return MovableWarAgent.ACTION_TAKE;
			}
		}
		
	}

	private WarMessage getMessageAboutFood() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getMessage().equals("foodHere"))
				return m;
		}
		return null;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
		getBrain().setHeading(getBrain().getHeading() + 180);
	}
	
	@Override
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)(super.getBrain());
	}

}
