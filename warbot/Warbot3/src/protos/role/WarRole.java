package protos.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import protos.tasks.Task;


/**
 * 
 * @author beugnon
 *
 */
public abstract class WarRole {
	
	
	public static final String UNKNOWN = "UNKNOWN";

	public static final boolean isUnknown(String role) {
		return UNKNOWN.equals(role);
	}

	private String roleName;
	
	private List<Task> roleTasks;
	
	protected WarRole(String role)
	{
		roleTasks = new ArrayList<>();
		initRoleTasks();
	}
	
	protected abstract void initRoleTasks();
	
	public String getRoleName()
	{
		return this.roleName;
	}
	
	public final List<Task> getRoleTasks()
	{
		return (List<Task>) Collections.unmodifiableList(roleTasks);
	}
	
	protected void addRoleTask(Task instance)
	{
		this.roleTasks.add(instance);
	}
	
	public Task getFirstTask()
	{
		return this.roleTasks.get(0);
	}
}
