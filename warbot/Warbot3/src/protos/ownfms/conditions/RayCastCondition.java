package protos.ownfms.conditions;

import java.util.List;

import protos.ownfms.AbstractCondition;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class RayCastCondition<T extends WarBrainController> extends
		AbstractCondition<T> {

	private double split;
	
	private WarPercept collided;

	public double getSplit() {
		return split;
	}

	public RayCastCondition(double split) {
		this.split = split;
	}

	@Override
	public boolean isAvailable(WarBrainController controller) {
		resetCollider();
		for (WarPercept wp : getPercepts(controller)) {
			if (moreOrLess(wp, controller))
			{
				setCollider(wp);
				return true;
			}

		}

		return false;
	}
	
	private void setCollider(WarPercept wp) {
		this.collided=wp;
	}

	private void resetCollider() {
		this.collided=null;
	}

	protected abstract List<WarPercept> getPercepts(WarBrainController controller);

	protected boolean moreOrLess(WarPercept wp, WarBrainController controller) {
		return Math.abs(controller.getBrain().getHeading() - wp.getAngle()) < getSplit();
	}

	protected void turnToCollidedPercept(WarBrainController controller)
	{
		if(this.isAvailable(controller))
			controller.getBrain().setHeading(controller.getBrain().getHeading() + controller.getBrain().getHeading() - collided.getAngle());
	}
}
