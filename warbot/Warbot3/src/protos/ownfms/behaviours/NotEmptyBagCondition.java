package protos.ownfms.behaviours;

import protos.WarExplorerBrainController;
import protos.ownfms.AbstractCondition;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class NotEmptyBagCondition extends AbstractCondition<WarBrainController> {

	private static NotEmptyBagCondition instance;

	@Override
	public boolean isAvailable(WarBrainController controller) {
		return !controller.getBrain().isBagEmpty();
	}

	public static AbstractCondition<WarExplorerBrainController> getInstance() {
		if(instance == null)
		{
			synchronized (NotEmptyBagCondition.class) {
				if(instance == null)
					instance = new NotEmptyBagCondition();
			}
		}
		return null;
	}

}
