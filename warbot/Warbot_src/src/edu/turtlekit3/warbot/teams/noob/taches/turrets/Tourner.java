package edu.turtlekit3.warbot.teams.noob.taches.turrets;

import java.util.ArrayList;

import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.noob.WarTurretBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;

public class Tourner extends TacheAgent {

	public Tourner(WarBrainController b) {
		super(b);
	}

	@Override
	public void exec() {
		WarTurretBrainController turret = (WarTurretBrainController) typeAgent;
		
		ArrayList<WarPercept> percept = turret.getBrain().getPerceptsEnemies();

		if(percept != null && percept.size() > 0){	
			Attaquer nvTache=new Attaquer(turret);
			turret.setTacheCourante(nvTache);
		}
		else{
			turret.getBrain().setHeading(turret.getBrain().getHeading()+180);
		}
	}

	@Override
	public String toString() {
		return "Tache Tourner";
	}
	
}
