package edu.turtlekit3.warbot.teams.noe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.WarKamikazeBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	private String toReturn = null;
	boolean iAbleToFireBase = false;
	private double lastAngle = 0;
	private int angleModifier = new Random().nextInt(90);


	ArrayList<WarMessage> messages;

	public WarRocketLauncherBrainController() {
		super();
		if(new Random().nextBoolean()) {
			angleModifier = -angleModifier;
		}
	}

	@Override
	public String action() {
		// Develop behaviour here

		toReturn = null;
		this.messages = getBrain().getMessages();

		// if(iAbleToFireBase)
		//	 attaquerBase();

		attackRocketLaunchers();
		wiggle();

		//		if(toReturn == null){
		//			if (getBrain().isBlocked())
		//				getBrain().setRandomHeading();
		//			toReturn = WarRocketLauncher.ACTION_MOVE;
		//		}

		return toReturn;
	}

	private void attackRocketLaunchers() {
		if(toReturn != null)
			return;

		if(!getBrain().isReloaded() && !getBrain().isReloading()){
			toReturn =  WarRocketLauncher.ACTION_RELOAD;
			return;
		}

		getBrain().setHeading(getBrain().getHeading() - lastAngle);
		lastAngle = 0;
		getBrain().setDebugStringColor(Color.blue);
		getBrain().setDebugString("Attack launchers");

		if(!unstuck()) {
			ArrayList<WarPercept> percept = getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocketLauncher);
			// Je un agentType dans le percept
			if(percept != null && percept.size() > 0){

				//je le dit aux autres
				getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.enemyTankHere, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
				getBrain().broadcastMessageToAgentType(WarAgentType.WarExplorer, Constants.enemyTankHere, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
				
				if(getBrain().isReloaded()){
					getBrain().setHeading(percept.get(0).getAngle());
					toReturn = WarRocketLauncher.ACTION_FIRE;
				}else{
					getBrain().setHeading(percept.get(0).getAngle() + angleModifier);
					lastAngle = angleModifier;
					toReturn = WarRocketLauncher.ACTION_MOVE;
				}
			}else{
				//si j'ai un message me disant qu'il y a  un autre tank a tuer

				WarMessage m = getFormatedMessageAboutEnemyTankToKill();
				if(m != null){
					CoordPolar p = getBrain().getIndirectPositionOfAgentWithMessage(m);
					getBrain().setHeading(p.getAngle());
					toReturn = WarRocketLauncher.ACTION_MOVE;
				}
			}	
		}
	}

	private boolean unstuck() {
		ArrayList<WarPercept> percept = getBrain().getPerceptsAlliesByType(WarAgentType.WarRocketLauncher);

		if(percept.size() > 0 && percept.get(0).getDistance() < 20) {
			getBrain().setHeading(getBrain().getHeading() - angleModifier);
			toReturn = MovableWarAgent.ACTION_MOVE;
			return true;
		}
		return false;
	}

	private void wiggle() {
		if(toReturn != null)
			return;

		if(getBrain().isBlocked())
			getBrain().setRandomHeading();

		getBrain().setDebugStringColor(Color.black);
		getBrain().setDebugString("Looking for ennemies");


		if(!unstuck()) {
			double angle = getBrain().getHeading() + new Random().nextInt(10) - new Random().nextInt(10);
			getBrain().setHeading(angle);
			toReturn = MovableWarAgent.ACTION_MOVE;		
		}

	}

	private WarMessage getFormatedMessageAboutEnemyTankToKill() {
		for (WarMessage m : this.messages) {
			if(m.getMessage().equals(Constants.enemyTankHere) && m.getContent() != null && m.getContent().length == 2){
				return m;
			}
		}
		return null;
	}
}