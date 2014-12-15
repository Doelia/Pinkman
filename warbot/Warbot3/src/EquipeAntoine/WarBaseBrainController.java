package EquipeAntoine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.agents.resources.WarFood;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class WarBaseBrainController extends WarBaseAbstractBrainController {
	
	private int timerInfo;
	
	private WarAgentType lastCreateUnit = null;
	private String toReturn;
	
	private static final int MIN_HEATH_TO_CREATE = (int) (WarBase.MAX_HEALTH * 0.8);
	
	public WarBaseBrainController() {
		super();
		timerInfo = 0;
	}


	@Override
	public String action() {
		
		toReturn = null;
		
		handleMessages();
		
		prevenirRocketLauncher();
		
		healMySelf();
		
		createUnit(WarAgentType.WarRocketLauncher);
		
		if(toReturn == null)
			toReturn = WarBase.ACTION_IDLE;
	
		return toReturn;
	}

	private void healMySelf() {
		if(toReturn != null)
			return;
		
		if(getBrain().isBagEmpty())
			return;
		
		if(getBrain().getHealth() <= WarBase.MAX_HEALTH - WarFood.HEALTH_GIVEN)
			toReturn = WarBase.ACTION_EAT;
	}
	
	private void prevenirRocketLauncher(){
		ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
		if (percept != null && percept.size() > 0){
			getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.baseIsAttack, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
		}
	}
	
	
	/*private void initRoleForExplorer(){
		Random rand = new Random();
		if (rand.nextInt(1) == 0){
			getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.newRole, new String("espion"));
		}
		else {
			getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.newRole, new String("cueilleur"));
		}
	}*/

	private void createUnit(WarAgentType a1) {
		if(toReturn != null)
			return;
		
		if(getBrain().getHealth() > MIN_HEATH_TO_CREATE){
				getBrain().setNextAgentToCreate(a1);
				getBrain().setDebugString("Create: "+a1.name());
			
			toReturn = WarBase.ACTION_CREATE;
		}
		
	}

	private void handleMessages() {
		ArrayList<WarMessage> msgs = getBrain().getMessages();
		for(WarMessage msg : msgs) {
			if (msg.getMessage().equals(Constants.whereAreYou)) {
				getBrain().sendMessage(msg.getSenderID(), Constants.here, "");
			}
		}
				
	}
	
}
