package edu.turtlekit3.warbot.teams.protos.communication.messages;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordCartesian;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class TargetMessageWrapper {

	private String message;
	private CoordPolar sender;
	private CoordCartesian target;
	
	private TargetMessageWrapper()
	{
		
	}
	
	public static TargetMessageWrapper unwrap(WarMessage wm)
	{
		TargetMessageWrapper tmw = new TargetMessageWrapper();
		tmw.setMessage(wm.getMessage());
		tmw.setTargetCoord(Double.parseDouble(wm.getContent()[0]),Double.parseDouble(wm.getContent()[1]));
			
		tmw.setSenderCoord(wm.getDistance(),wm.getAngle());
		
		return tmw; 
	}

	public static void wrapAndSendMessageToAll(String message,WarBrain wb,WarPercept wp)
	{
		
		CoordPolar c = new CoordPolar(wp.getDistance(), wp.getAngle());
		CoordCartesian c2 = c.toCartesian();

		String sX = new String("" + c2.getX());
		String sY = new String("" + c2.getY());
		wb.broadcastMessageToAll(message,sX,sY);
	}
	
	public static void wrapAndSendMessageToAll(String message,WarBrain wb,CoordPolar wp)
	{
		
		CoordPolar c = wp;
		CoordCartesian c2 = c.toCartesian();

		String sX = new String("" + c2.getX());
		String sY = new String("" + c2.getY());
		wb.broadcastMessageToAll(message,sX,sY);
	}
	
	public static void wrapAndSendMessageToID(String message, WarBrain wb,WarPercept wp,int id)
	{
		
		CoordPolar c = new CoordPolar(wp.getDistance(), wp.getAngle());
		CoordCartesian c2 = c.toCartesian();

		String sX = new String("" + c2.getX());
		String sY = new String("" + c2.getY());
		wb.sendMessage(id,message,sX,sY);
		
	}
	
	public static void wrapAndSendMessageToAgentType(String message,WarBrain wb,WarPercept wp,WarAgentType type)
	{
		
		CoordPolar c = new CoordPolar(wp.getDistance(), wp.getAngle());
		CoordCartesian c2 = c.toCartesian();

		String sX = new String("" + c2.getX());
		String sY = new String("" + c2.getY());
		wb.broadcastMessageToAgentType(type,message,sX,sY);
		
	}

	private void setSenderCoord(double distance, double angle) {
		this.sender = new CoordPolar(distance,angle);
	}
	
	private void setTargetCoord(double sX, double sY) {
		this.target = new CoordCartesian(sX, sY);
	}
	
	private void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public CoordPolar compute()
	{
		CoordCartesian cc = sender.toCartesian();
		cc.add(target);
		return cc.toPolar();
	}
	
	public void turnToTarget(WarBrainController wbc)
	{
		wbc.getBrain().setHeading(compute().getAngle());
	}

	public static void wrapAndSendMessageToID(
			String message, WarBaseBrain brain,
			CoordPolar nearEnemyBase, int ID)
	{
		CoordCartesian c2 = nearEnemyBase.toCartesian();
		String sX = new String("" + c2.getX());
		String sY = new String("" + c2.getY());
		brain.sendMessage(ID,message,sX,sY);
	}
}
