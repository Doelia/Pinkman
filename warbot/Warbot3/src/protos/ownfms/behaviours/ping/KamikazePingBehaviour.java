package protos.ownfms.behaviours.ping;

import protos.Nexus;
import protos.role.WarRole;

public class KamikazePingBehaviour extends PingBehaviour<Nexus>{
	
	private static KamikazePingBehaviour instance;
	
	public static KamikazePingBehaviour getInstance()
	{
		if(instance == null)
		{
			synchronized (KamikazePingBehaviour.class) 
			{
				if(instance == null)
					instance = new KamikazePingBehaviour();
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
