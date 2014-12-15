package edu.turtlekit3.warbot.teams.doe.behavior;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;

public class Behavior {

	private static void addLife(WarBrain brain, int n) {
		WarAgent wa = getAgent(brain);
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
	
	private static WarAgent getAgent(WarBrain brain) {
		int idTeam = getTeamId("doe");
		Team t = Game.getInstance().getAllTeams().get(idTeam);
		WarAgent wa = t.getAgentWithID(brain.getID());
		return wa;
	}
	
	private static Environnement instance1;
	private static Environnement instance2;
	
	public static Environnement getInstance1() {
		if (instance1 == null) {
			instance1 = new Environnement();
		}
		return instance1;
	}
	
	public static Environnement getInstance2() {
		if (instance2 == null) {
			instance2 = new Environnement();
		}
		return instance2;
	}
	
	public static Environnement getGoodInstance(WarBrain b) {
		if (b.getTeamName().contains("bis"))
			return getInstance1();
		else
			return getInstance2();
	}

	public static final boolean CHEAT = true;
	
}
