package protos.tasks.base;

import java.util.List;

import protos.Nexus;
import protos.communication.ProtosCommunication;
import protos.role.BaseRole;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class InitBase extends Task<Nexus> {

	private static InitBase instance;

	@Override
	public ResultTask execute(Nexus wa) {

		wa.getBrain().setDebugString(getClass().getSimpleName());
		ResultTask rt = null;


			
			if(!haveHQDefined(wa))
			{
				//Become HQ
				wa.setWarRole(BaseRole.iHEADQUARTERS);
				// or wa.setWaRole(BaseRole.getBaseRoleByName(BaseRole.HEADQUARTERS));
				//Indique qu'un HQ est déclaré
				wa.getBrain().broadcastMessageToAll(ProtosCommunication.PING,wa.getWarRole().getRoleName());
				rt = new ResultTask(wa.getWarRole().getFirstTask(),WarBase.ACTION_IDLE);
			}
			else
			{
				if(haveHQDefined(wa) && !haveSafeBranchDefined(wa))
				{
					wa.setWarRole(BaseRole.iSAFE_BRANCH);
					wa.getBrain().broadcastMessageToAll(ProtosCommunication.PING,wa.getWarRole().getRoleName());
					rt = new ResultTask(wa.getWarRole().getFirstTask(),WarBase.ACTION_IDLE);
				}
				else
				{
					if(haveHQDefined(wa) && haveSafeBranchDefined(wa))
					{
						wa.setWarRole(BaseRole.iFACTORY);
						wa.getBrain().broadcastMessageToAll(ProtosCommunication.PING,wa.getWarRole().getRoleName());
						rt = new ResultTask(wa.getWarRole().getFirstTask(),WarBase.ACTION_IDLE);
					}
				}
			}
		

		if (rt == null)
			rt = new ResultTask(this, WarBase.ACTION_IDLE);

		return rt;
	}

	private boolean haveSafeBranchDefined(WarBrainController wbc) {
		List<WarMessage> list = wbc.getBrain().getMessages();
		if(list==null || list.isEmpty())
			
			return false;
		
		for(WarMessage wm : list)
		{
			if(wm.getMessage().equals(ProtosCommunication.PING) &&
					wm.getContent()[0].equals(BaseRole.SAFE_BRANCH))
				return true;
				
		}
		return false;
	}

	private boolean haveHQDefined(WarBrainController wbc) {

		List<WarMessage> list = wbc.getBrain().getMessages();
		if(list==null || list.isEmpty())
			return false;
		
		for(WarMessage wm : list)
		{
			if(wm.getMessage().equals(ProtosCommunication.PING) &&
					wm.getContent()[0].equals(BaseRole.HEADQUARTERS))
				return true;
				
		}
		return false;
		
	}

	public static Task<Nexus> getInstance() {
		if (instance == null) {
			synchronized (InitBase.class) {
				if (instance == null) {
					instance = new InitBase();
				}
			}
		}
		return instance;
	}

}
