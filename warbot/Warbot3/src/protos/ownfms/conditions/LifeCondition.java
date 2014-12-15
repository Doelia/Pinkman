package protos.ownfms.conditions;

import protos.ownfms.AbstractCondition;
import edu.turtlekit3.warbot.brains.WarBrainController;



public class LifeCondition extends AbstractCondition<WarBrainController>{

	
	private int healthThreshold;

	private int direction;
	
	public static final int INF=-2;
	public static final int INF_OR_EQUALS=-1;
	public static final int EQUALS = 0;
	public static final int SUP_OR_EQUALS=1;
	public static final int SUP=2;
	
	
	public LifeCondition(int threshold) {
		this.healthThreshold=threshold;
		this.direction = INF;
	}
	
	public LifeCondition(int maxHealth, int direction) {
		this(maxHealth);
		if(direction == INF_OR_EQUALS)
			this.direction=INF_OR_EQUALS;
		else if(direction  == EQUALS)
			this.direction=EQUALS;
		else if(direction == SUP_OR_EQUALS)
			this.direction=SUP_OR_EQUALS;
		else if(direction >= SUP)
			this.direction=SUP;
	}

	@Override
	public boolean isAvailable(WarBrainController controller) {
		
		if(direction == INF)
			return controller.getBrain().getHealth() < getHealthThreshold();
		else if(direction == INF_OR_EQUALS)
			return controller.getBrain().getHealth() <= getHealthThreshold();
		else if(direction  == EQUALS)
			return controller.getBrain().getHealth() == getHealthThreshold();
		else if(direction == SUP_OR_EQUALS)
			return controller.getBrain().getHealth() >= getHealthThreshold();
		else
			return controller.getBrain().getHealth() > getHealthThreshold();
	}
	
	public int getHealthThreshold()
	{
		return healthThreshold;
	}

}
