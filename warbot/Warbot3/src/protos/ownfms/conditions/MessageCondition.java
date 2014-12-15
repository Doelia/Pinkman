package protos.ownfms.conditions;

import java.util.List;

import protos.WarExplorerBrainController;
import protos.ownfms.AbstractCondition;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class MessageCondition extends
		AbstractCondition<WarExplorerBrainController> {

	private String message;
	
	private CoordPolar target;

	private int idTarget;

	public MessageCondition(String message) {
		this.message = message;
	}

	@Override
	public boolean isAvailable(WarBrainController controller) {
		List<WarMessage> list = controller.getBrain().getMessages();
		if(list==null || list.isEmpty())
			return false;
		target = null;
		for(WarMessage wm : list)
		{
			if(wm.getMessage().equals(getMessage()))
				idTarget = wm.getSenderID();
			{	target = new CoordPolar(wm.getDistance(), wm.getAngle());break;}
		}
		return target!=null;
	}

	public String getMessage()
	{
		return this.message;
	}

	public CoordPolar getTarget() {
		return target;
	}
	
	public int getIDTarget()
	{
		return idTarget;
	}
}
