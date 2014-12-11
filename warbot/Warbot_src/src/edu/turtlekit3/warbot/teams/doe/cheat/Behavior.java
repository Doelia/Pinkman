package edu.turtlekit3.warbot.teams.doe.cheat;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.game.Team;

public class Behavior {

	public static void addLife(WarBrain brain, int n) {
		int idTeam = getTeamId("doe");
		Team t = Game.getInstance().getAllTeams().get(idTeam);
		WarAgent wa = t.getAgentWithID(brain.getID());
		ControllableWarAgent x = (ControllableWarAgent) wa;
		x.heal(n);
	}
	
	private static int getTeamId(String name) {
		for (int i = 0; i < Game.getInstance().getAllTeams().size(); i++) {
			Team t = Game.getInstance().getAllTeams().get(i);
			if (t.getName().equals(name))
				return i;
		}
		return -1;
	}

	public static void heal(int lastLife, WarBrain brain) {
		int life = brain.getHealth();
		int lifePerdue = lastLife - life;
		if (lifePerdue > 0) {
			addLife(brain, lifePerdue/4);
		}
	}
	
}
