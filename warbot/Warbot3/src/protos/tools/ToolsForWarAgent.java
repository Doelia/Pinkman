package protos.tools;

import java.util.List;

import protos.Nexus;
import protos.WarEngineerBrainController;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class ToolsForWarAgent {

	
	public static void addHeading(WarBrainController warController, double angle) {
		warController.getBrain().setHeading(warController.getBrain().getHeading() + angle);
	}
	
	public static void sameHeading(WarBrainController wa, WarPercept wp) {
		wa.getBrain().setHeading(wp.getAngle());
	}
	
	public static void sameHeading(WarBrainController wa, CoordPolar cp) {
		wa.getBrain().setHeading(cp.getAngle());
	}
	public static void positioning(WarBrainController wa, double angle) {
		wa.getBrain().setHeading(angle);
	}
	
	public static WarEngineerBrainController engineer(WarBrainController wbc)
	{
		return (WarEngineerBrainController) wbc;
	}
	
	public static Nexus base(WarBrainController wbc)
	{
		return (Nexus) wbc;
	}
	
	public static WarPercept nearestPercept(List<WarPercept> l, WarAgentType type) {
		if(l != null){
		
			WarPercept minWap = l.get(0);
			double distMinWap = l.get(0).getDistance();
		
			int index = 0;
			while(index < l.size() && !minWap.getType().equals(type))
			{
				index++;
				minWap = l.get(index);
			}

			if(minWap.getType().equals(type)) {
				for(WarPercept wp : l){
					if(distMinWap > wp.getDistance() && minWap.getType().equals(type)) {
						minWap = wp;
						distMinWap = wp.getDistance();
					}
				}
				
				return minWap;
			}
		}	
		
		return null;
	}
	
	/*public static WarMessage getMessageFromBase(WarBrainController wa) {
		for (WarMessage m : wa.getBrain().getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		wa.getBrain().broadcastMessageToAgentType(WarAgentType.WarBase, ProtosCommunication.ASK_BASE_WERE_ARE_YOU, "");
		
		return null;
	}*/
	
	
}
