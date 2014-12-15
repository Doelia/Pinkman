package protos.ownfms.conditions;

import java.util.List;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class RocketOnMeCondition extends RayCastCondition<WarBrainController> {

	public RocketOnMeCondition(double split) {
		super(split);
	}

	@Override
	public boolean isAvailable(WarBrainController controller) {
		
		for (WarPercept wp : controller.getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocket)) {
			if (moreOrLess(wp, controller))
			{
				return true;
			}

		}

		return false;
	}
	
	@Override
	protected boolean moreOrLess(WarPercept wp, WarBrainController controller) {
		return Math.abs(controller.getBrain().getHeading() - wp.getAngle()) < getSplit();
	}

	@Override
	protected List<WarPercept> getPercepts(WarBrainController controller) 
	{
		return controller.getBrain().getPerceptsEnemiesByType(WarAgentType.WarRocket);
	}

}
