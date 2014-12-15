package protos.ownfms.behaviours.ping;

import protos.Nexus;
import protos.role.WarRole;

public class RocketLauncherPingBehaviour extends PingBehaviour<Nexus>{
	
	private static RocketLauncherPingBehaviour instance;
	
	public static RocketLauncherPingBehaviour getInstance()
	{
		if(instance == null)
		{
			synchronized (RocketLauncherPingBehaviour.class) 
			{
				if(instance == null)
					instance = new RocketLauncherPingBehaviour();
			}
		}
		
		return instance;
	}
	
	@Override
	public WarRole getRole(Nexus controller) 
	{
		return controller.getWarRole();
	}

	

}
