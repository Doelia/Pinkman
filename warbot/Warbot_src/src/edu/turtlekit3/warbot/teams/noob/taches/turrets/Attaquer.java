package edu.turtlekit3.warbot.teams.noob.taches.turrets;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.noob.WarTurretBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;

public class Attaquer extends TacheAgent {

	public Attaquer(WarBrainController b) {
		super(b);
	}

	@Override
	public void exec() {
		WarTurretBrainController turret = (WarTurretBrainController) typeAgent;
		
		ArrayList<WarPercept> percept = turret.getBrain().getPerceptsEnemies();
		if(percept != null && percept.size() > 0){	
			
			if(turret.getBrain().isReloaded()){
				turret.getBrain().setHeading(percept.get(0).getAngle());
				turret.setToReturn(WarRocketLauncher.ACTION_FIRE);
			}
		}
		else{
			Tourner nvTache=new Tourner(turret);
			turret.setTacheCourante(nvTache);
		}
	}

	@Override
	public String toString() {
		return "Tache Attaquer";
	}
	
}
