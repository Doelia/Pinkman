package protos.role;

import protos.tasks.rocketlauncher.InitRocketLauncher;
import protos.tasks.rocketlauncher.RocketLauncherRoutine;
import protos.tasks.rocketlauncher.StartHunt;

public abstract class RocketLauncherRole extends WarRole
{
	protected RocketLauncherRole(String role)
	{
		super(role);
	}

	//Free of his actions
	public static final String VANGUARD = "VANGUARD";
	
	
	public static final RocketLauncherRole iVANGUARD = new RocketLauncherRole(VANGUARD) {
		
		@Override
		protected void initRoleTasks() 
		{
			super.addRoleTask(RocketLauncherRoutine.getInstance());
		}
	};
	
	//Obey to the request of a leader in a group
	public static final String MERCENARY = "MERCENARY";
	
	//Track and kill 
	public static final String HUNTER = "HUNTER";


	public static final RocketLauncherRole iHUNTER = new RocketLauncherRole(VANGUARD) {
		
		@Override
		protected void initRoleTasks() 
		{
			//ADD HUNTER TASK
			//SPECIAL BECAUSE WE NEED DETAILS OF TARGET
			//SO MAYBE 
			addRoleTask(StartHunt.getInstance());//SEARCH TARGET
			//TRACK AND KILL
		}
	};
	


	public static final RocketLauncherRole iMERCENARY = new RocketLauncherRole(MERCENARY) {
		
		@Override
		protected void initRoleTasks() {
			
			//TODO REPLACE BY TASK MERCENARY_WAITING_FOR_CONTRACT
			//TODO ADD FOCUS ON CONTRACT
			addRoleTask(InitRocketLauncher.getInstance());
		}
	};
	
	public static RocketLauncherRole iUNKNOWN = new RocketLauncherRole(UNKNOWN) {
		
		@Override
		protected void initRoleTasks() {
			addRoleTask(InitRocketLauncher.getInstance());
		}
	};
	
	public static final String[] values()
	{
		String tab[]=new String[RocketLauncherRole.class.getFields().length];
		for(int i=0;i<RocketLauncherRole.class.getFields().length;i++)
		{
			tab[i]=RocketLauncherRole.class.getFields()[0].getName();
		}
		return tab;
	}

	public static RocketLauncherRole getRocketLauncherRoleByName(String name) {
		switch(name)
		{
		case VANGUARD:
			return iVANGUARD;
		case MERCENARY:
			return iMERCENARY;
		case HUNTER:
			return iHUNTER;
		default:
			return iUNKNOWN;
		}
	}
}
