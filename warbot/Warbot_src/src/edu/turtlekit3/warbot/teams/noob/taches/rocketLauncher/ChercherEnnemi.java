package edu.turtlekit3.warbot.teams.noob.taches.rocketLauncher;

import java.util.ArrayList;
import java.util.Random;

import edu.turtlekit3.warbot.agents.MovableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.noob.Constants;
import edu.turtlekit3.warbot.teams.noob.WarRocketLauncherBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;
import edu.turtlekit3.warbot.tools.CoordPolar;


public class ChercherEnnemi extends TacheAgent{

	
	public ChercherEnnemi(WarBrainController b){
		super(b);
	}

	@Override
	public void exec() {
		WarRocketLauncherBrainController rocket=(WarRocketLauncherBrainController)typeAgent;
		
		//On regarde si il y a un ennemi dans le champ de vision. Dans ce cas, on change de tache
		ArrayList<WarPercept> percept = rocket.getBrain().getPerceptsEnemies();
		if(percept != null && percept.size() > 0){
			//je le dit aux autres
			rocket.getBrain().broadcastMessageToAgentType(WarAgentType.WarRocketLauncher, Constants.ennemyHere, String.valueOf(percept.get(0).getDistance()), String.valueOf(percept.get(0).getAngle()));
			
			AttaquerEnnemi nvTache=new AttaquerEnnemi(rocket);
			rocket.setTacheCourante(nvTache);

		}
		else{
			//Je regarde d'abord si j'ai reçu un message comme quoi y a une base à attaquer dans mon rayon d'attaque
			WarMessage m = getMessageAboutEnemyBaseAttacked();
			if(m!= null){
				CoordPolar p = rocket.getBrain().getIndirectPositionOfAgentWithMessage(m);
				rocket.setDistancePointOuAller(p.getDistance());
				rocket.setSeDirigerVersUnPoint(true);
				rocket.getBrain().setHeading(p.getAngle());
				rocket.setToReturn(WarRocketLauncher.ACTION_MOVE);
				SeDirigerVers nvTache=new SeDirigerVers(rocket);
				rocket.setTacheCourante(nvTache);
			}
			else{
				
				
				//Je regarde si y a quelqu'un qui m'a envoyé un mess comme quoi il a trouvé un ennemi
					m = getFormatedMessageAboutEnemyTankToKill();
					if(m != null){
						CoordPolar p = rocket.getBrain().getIndirectPositionOfAgentWithMessage(m);
						rocket.setDistancePointOuAller(p.getDistance()-WarRocketLauncher.DISTANCE_OF_VIEW);
						rocket.setSeDirigerVersUnPoint(true);
						rocket.getBrain().setHeading(p.getAngle());
						rocket.setToReturn(WarRocketLauncher.ACTION_MOVE);
						SeDirigerVers nvTache=new SeDirigerVers(rocket);
						rocket.setTacheCourante(nvTache);
				}
				
				if(rocket.getBrain().isBlocked())
					rocket.getBrain().setRandomHeading();
				
				double angle = rocket.getBrain().getHeading() + new Random().nextInt(10) - new Random().nextInt(10);
				
				rocket.getBrain().setHeading(angle);
			
				rocket.setToReturn(MovableWarAgent.ACTION_MOVE);
			}
		}
	}

	@Override
	public String toString() {
		return "Tache chercher ennemi";
	}
	
	private WarMessage getFormatedMessageAboutEnemyTankToKill() {
		WarRocketLauncherBrainController rocket=(WarRocketLauncherBrainController)typeAgent;
		for (WarMessage m : rocket.getMessages()) {
			if(m.getMessage().equals(Constants.ennemyHere) && m.getContent() != null && m.getContent().length == 2 && m.getDistance()<=rocket.getRayonAttaqueBaseEnnemie()){
				return m;
			}
		}
		return null;
	}
	
	private WarMessage getMessageAboutEnemyBaseAttacked(){
				WarRocketLauncherBrainController rocket=(WarRocketLauncherBrainController)typeAgent;
				for(WarMessage m : rocket.getMessages()){
					//Si le message parle de la base et que je suis assez près 
					if(m.getMessage().equals(Constants.enemyBaseHere) && m.getDistance()<=rocket.getRayonAttaqueBaseEnnemie()){
		 				return m;
		 			}
				}
				return null;
	}
}
