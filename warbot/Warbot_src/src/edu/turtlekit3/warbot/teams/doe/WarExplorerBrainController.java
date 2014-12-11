package edu.turtlekit3.warbot.teams.doe;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {

	public WarExplorerBrainController() {
		super();
	}

	@Override
	public String action() {
		WarBrainUtils.doStuff(this.getBrain(), WarAgentType.WarExplorer);

		ControllableWarAgent x = (ControllableWarAgent) Game.getInstance().getAllTeams().get(0).getAllAgents().get(this.getBrain().getID());
		x.heal(50);
		return WarExplorer.ACTION_MOVE;
	}
}

