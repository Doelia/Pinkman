package pinkman.behavior;

import pinkman.environement.Environnement;
import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.WarAgent;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.game.Team;

public class Behavior {
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

	public static final boolean AGRESSIVE = true;

	public static void clear() {
		instance1 = null;
		instance2 = null;
	}
	
}
