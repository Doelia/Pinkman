package edu.turtlekit3.warbot.teams.noob.taches.rocketLauncher;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.noob.WarRocketLauncherBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;

public class SeDirigerVers extends TacheAgent{

	public SeDirigerVers(WarBrainController b) {
		super(b);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec() {
		// TODO Auto-generated method stub
		WarRocketLauncherBrainController rocket=(WarRocketLauncherBrainController)typeAgent;
		
		//Si seDirigerVersPoint et vrai
		if(rocket.getSeDirigerVersPoint()){
			
			//Si on est plus ou moins arrivé au point
			if(rocket.getDistancePointOuAller()<=0){
				rocket.setSeDirigerVersUnPoint(false);
				AttaquerEnnemi nvTache=new AttaquerEnnemi(rocket);
				rocket.setTacheCourante(nvTache);
				//Sinon on avance
			}else{
				rocket.setDistancePointOuAller(
				rocket.getDistancePointOuAller()-WarRocketLauncher.SPEED);
				rocket.setToReturn(WarRocketLauncher.ACTION_MOVE);
			}
		}
		
		//Sinon on passe en mode chercherEnnemi
		else{
			ChercherEnnemi nvTache=new ChercherEnnemi(rocket);
			rocket.setTacheCourante(nvTache);
			rocket.setToReturn(WarRocketLauncher.ACTION_MOVE);
		}
		
	}

	@Override
	public String toString() {
		return "Tache se diriger vers";
	}

}
