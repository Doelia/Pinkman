package edu.turtlekit3.warbot.teams.doe.messages;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;


public class ReceiverEnvironementInstruction {

	Environnement e;
	
	public void onMessage(WarMessage m) {
		
		if (m.getMessage().equals(TypeUpdateEnv.SET_WERE_TOP)) {
			e.setWeAreInTop(Boolean.parseBoolean(m.getContent()[0]));
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.ADD_FREE_FOOD)) {
			e.addFreeFood(
					new Vector2(
							Integer.parseInt(m.getContent()[0]),
							Integer.parseInt(m.getContent()[1])
					),
					Integer.parseInt(m.getContent()[2]));
							
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.SET_MAIN_BASE)) {
			e.addFreeFood(
					new Vector2(
							Integer.parseInt(m.getContent()[0]),
							Integer.parseInt(m.getContent()[1])
					),
					Integer.parseInt(m.getContent()[2]));
							
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.SET_WERE_TOP)) {
			e.setWeAreInTop(Boolean.parseBoolean(m.getContent()[0]));
							
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.REGISTER_EXPLORER)) {
			e.registerExplorer(Integer.parseInt(m.getContent()[0]));
		}
		
		if (m.getMessage().equals(TypeUpdateEnv.UPDATE_POSITION_ENEMY)) {
			e.registerExplorer
				(Integer.parseInt(m.getContent()[0]));
		}
		
	}
}
