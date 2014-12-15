package pinkman;

import edu.turtlekit3.warbot.agents.agents.WarKamikaze;
import edu.turtlekit3.warbot.brains.braincontrollers.WarKamikazeAbstractBrainController;

public class WarKamikazeBrainController extends WarKamikazeAbstractBrainController {
	
	public WarKamikazeBrainController() {
		super();
	}

	@Override
	public String action() {
		return WarKamikaze.ACTION_IDLE;
	}
}
