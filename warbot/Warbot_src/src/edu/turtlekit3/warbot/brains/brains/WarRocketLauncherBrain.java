package edu.turtlekit3.warbot.brains.brains;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;
import edu.turtlekit3.warbot.brains.capacities.AgressiveBrain;

public class WarRocketLauncherBrain extends MovableWarAgentBrain implements AgressiveBrain {
	
	public WarRocketLauncherBrain(WarRocketLauncher agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarRocketLauncher) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarRocketLauncher) _agent).isReloading();
	}

}
