package edu.turtlekit3.warbot.teams.noobteam;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {
	
	private int baseAngle;
	private boolean imGiving = false;
	private String toReturn;
	
	public WarExplorerBrainController() {
		super();
	}

	@Override
	public String action() {
		// Develop behaviour here
		
		randomMove();
		isBouffe();
		isEnnemyBase();
		
		return toReturn;
	}
	
	private void randomMove(){
		getBrain().setRandomHeading(5);
		if(getBrain().isBlocked()){
			getBrain().setHeading(180);
		}
		toReturn = WarExplorer.ACTION_MOVE;
	}
	
	private void isBouffe(){
		ArrayList<WarPercept> bouffe = getBrain().getPerceptsResources();
		if(bouffe.size() > 0){
			getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, "BOUFFE", "");
		}	
	}
	
	private void isEnnemyBase(){
		ArrayList<WarPercept> ennemyBase = getBrain().getPerceptsEnemiesByType(WarAgentType.WarBase);
		if(ennemyBase.size() > 0){
			getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, "ennemyBase", "");
		}
	}
}