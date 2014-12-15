package protos.ownfms.behaviours.ping;

import protos.Nexus;
import protos.role.WarRole;

public class NexusPingBehaviour extends PingBehaviour<Nexus>{
	
	private static NexusPingBehaviour instance;
	
	public static NexusPingBehaviour getInstance()
	{
		if(instance == null)
		{
			synchronized (NexusPingBehaviour.class) 
			{
				if(instance == null)
					instance = new NexusPingBehaviour();
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
