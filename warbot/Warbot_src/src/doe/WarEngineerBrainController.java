package doe;

import edu.turtlekit3.warbot.agents.agents.WarKamikaze;
import edu.turtlekit3.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;

public class WarEngineerBrainController extends WarEngineerAbstractBrainController {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		return WarKamikaze.ACTION_IDLE;
	}
}
