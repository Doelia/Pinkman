package protos.ownfms.behaviours.ping;

import protos.Nexus;
import protos.role.WarRole;

public class EngineerPingBehaviour extends PingBehaviour<Nexus>{
	
	private static EngineerPingBehaviour instance;
	
	public static EngineerPingBehaviour getInstance()
	{
		if(instance == null)
		{
			synchronized (EngineerPingBehaviour.class) 
			{
				if(instance == null)
					instance = new EngineerPingBehaviour();
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
