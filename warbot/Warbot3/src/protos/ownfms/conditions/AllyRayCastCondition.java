package protos.ownfms.conditions;

import java.util.Collections;
import java.util.List;

import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class AllyRayCastCondition extends RayCastCondition<WarBrainController> {

	public AllyRayCastCondition(double split) {
		super(split);
	}

	@Override
	protected List<WarPercept> getPercepts(WarBrainController controller) {
		List<WarPercept> list = controller.getBrain().getPerceptsAllies();
		if(list!=null)
			return list;
		else
			return Collections.emptyList();
	}

}
