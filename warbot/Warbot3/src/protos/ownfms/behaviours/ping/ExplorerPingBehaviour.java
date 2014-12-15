package protos.ownfms.behaviours.ping;

import protos.Nexus;
import protos.role.WarRole;

public class ExplorerPingBehaviour extends PingBehaviour<Nexus>{
	
	private static ExplorerPingBehaviour instance;
	
	public static ExplorerPingBehaviour getInstance()
	{
		if(instance == null)
		{
			synchronized (ExplorerPingBehaviour.class) 
			{
				if(instance == null)
					instance = new ExplorerPingBehaviour();
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
