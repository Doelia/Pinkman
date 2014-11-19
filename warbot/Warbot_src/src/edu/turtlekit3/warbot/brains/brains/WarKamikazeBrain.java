package edu.turtlekit3.warbot.brains.brains;

import edu.turtlekit3.warbot.agents.agents.WarKamikaze;
import edu.turtlekit3.warbot.brains.MovableWarAgentBrain;
import edu.turtlekit3.warbot.brains.capacities.AgressiveBrain;

public class WarKamikazeBrain extends MovableWarAgentBrain implements AgressiveBrain {
	
	public WarKamikazeBrain(WarKamikaze agent) {
		super(agent);
	}

	@Override
	public boolean isReloaded() {
		return ((WarKamikaze) _agent).isReloaded();
	}

	@Override
	public boolean isReloading() {
		return ((WarKamikaze) _agent).isReloading();
	}
}