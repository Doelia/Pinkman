package edu.turtlekit3.warbot.teams.noob.taches.explorer;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.noob.WarExplorerBrainController;
import edu.turtlekit3.warbot.teams.noob.taches.TacheAgent;

public class LocaliserBase extends TacheAgent
{
	private static final int NB_PAS_LOCALISATION = 30;
	
	private double nbPasRestants;
	
	public LocaliserBase(WarBrainController b) {
		super(b);
		nbPasRestants = NB_PAS_LOCALISATION;
	}

	@Override
	public void exec() {
		WarExplorerBrainController explorer = (WarExplorerBrainController) typeAgent;
	
		if (nbPasRestants > 0) {
			nbPasRestants -= WarExplorer.SPEED;
		}
		else {
			nbPasRestants = NB_PAS_LOCALISATION;
			explorer.getBrain().setHeading(explorer.getBrain().getHeading() + 180);
		}
		
		explorer.setToReturn(WarExplorer.ACTION_MOVE);
	}

	@Override
	public String toString() {
		return "Tache Localiser Base";
	}
	
}
