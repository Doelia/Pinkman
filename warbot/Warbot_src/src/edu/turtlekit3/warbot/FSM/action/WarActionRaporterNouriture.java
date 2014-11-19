package edu.turtlekit3.warbot.FSM.action;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;

/**
 * Raporte la nouriture
 * @author Olivier
 *
 */
public class WarActionRaporterNouriture extends WarAction{
	
	private final int nbElementToKeep;	

	public WarActionRaporterNouriture(WarBrain brain, int nombreAGarder) {
		super(brain);
		this.nbElementToKeep = nombreAGarder;
	}

	public String executeAction(){
		
		if(getBrain().isBagEmpty() | getBrain().getNbElementsInBag() <= nbElementToKeep)
			setActionTerminate(true);
		
		if(getBrain().isBlocked())
			getBrain().setRandomHeading();

		ArrayList<WarPercept> basePercepts = getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);
		
		//Si je ne voit pas de base vois une base
		if(basePercepts == null | basePercepts.size() == 0){
			
			WarMessage m = this.getMessageFromBase();
			//Si j'ai un message de la base je vais vers elle
			if(m != null)
				getBrain().setHeading(m.getAngle());
			
			//j'envoi un message a la base pour savoir où ell està
			//getBrain().broadcastMessageDefaultRole(WarBase., "whereAreYou", null);
			
			return MovableWarAgent.ACTION_MOVE;
			
		}else{//si je vois une base
			WarPercept base = basePercepts.get(0);
			
			if(base.getDistance() > MovableWarAgent.MAX_DISTANCE_GIVE){
				getBrain().setHeading(base.getAngle());
				return MovableWarAgent.ACTION_MOVE;
			}else{
				getBrain().setIdNextAgentToGive(base.getID());
				return MovableWarAgent.ACTION_GIVE;
			}
			
		}
		
	}
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : getBrain().getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, "whereAreYou", "");
		return null;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
	
	@Override
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)(super.getBrain());
	}

}
