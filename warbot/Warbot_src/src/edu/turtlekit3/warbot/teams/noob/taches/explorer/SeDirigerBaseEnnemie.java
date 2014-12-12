package edu.turtlekit3.warbot.teams.noob.taches.explorer;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.noob.WarExplorerBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;

public class SeDirigerBaseEnnemie extends TacheAgent
{

	public SeDirigerBaseEnnemie(WarBrainController b) {
		super(b);
	}

	@Override
	public void exec() {
		WarExplorerBrainController explorer = (WarExplorerBrainController) typeAgent;
		
		// L'espion passe automatiquement en localiser base lorsqu'il la perçoit dans les réflèxes
		
		if (explorer.getDistance() > 0) {
			explorer.setDistance(explorer.getDistance() - WarExplorer.SPEED);
		}
		else {
			explorer.setTacheCourante(new ChercherEnnemis(explorer));
		}
		
		explorer.setToReturn(WarExplorer.ACTION_MOVE);
	}

	@Override
	public String toString() {
		return "Tache Aller base ennemie";
	}
	
}
