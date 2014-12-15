package protos.ownfms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class AbstractBehaviour<T extends WarBrainController>
{
	
	private List<AbstractCondition<T>> conditions;
	
	
	protected AbstractBehaviour()
	{
		conditions=new ArrayList<>();
		buildConditions();
	}
	

	protected void addCondition(AbstractCondition<T> condition)
	{
		this.conditions.add(condition);
	}
	
	protected void addConditions(Collection<AbstractCondition<T>> conditions)
	{
		this.conditions.addAll(conditions);
	}
	
	public List<AbstractCondition<T>> getConditions()
	{
		return this.conditions;
	}
	
	protected abstract void buildConditions();


	public boolean isActive(T controller)
	{
		int cpt=0;
		for(AbstractCondition<?> condition : conditions)
		{
			if(condition.isAvailable(controller))
				cpt++;
		}
		return cpt==conditions.size();
	}
	
	/**
	 * 
	 * @param controller
	 * @return null or a constant string with the action to do
	 */
	public abstract String act(T controller);
	
}
