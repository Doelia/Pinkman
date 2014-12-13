package edu.turtlekit3.warbot.teams.doe.messages;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;


public class UpdateEnvironementInstruction {

	Environnement e;
	
	public void onMessage(WarMessage m) {
		
		if (m.getMessage().equals(TypeUpdateEnv.setWeAreInTop)) {
			e.setWeAreInTop(Boolean.parseBoolean(m.getContent()[0]));
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.addFreeFood)) {
			e.addFreeFood(
					new Vector2(
							Integer.parseInt(m.getContent()[0]),
							Integer.parseInt(m.getContent()[1])
					),
					Integer.parseInt(m.getContent()[2]));
							
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.setMainBase)) {
			e.addFreeFood(
					new Vector2(
							Integer.parseInt(m.getContent()[0]),
							Integer.parseInt(m.getContent()[1])
					),
					Integer.parseInt(m.getContent()[2]));
							
		}
		
	}
}
