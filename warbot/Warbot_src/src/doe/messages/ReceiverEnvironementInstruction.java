package doe.messages;

import com.badlogic.gdx.math.Vector2;

import doe.environement.Environnement;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;


public class ReceiverEnvironementInstruction {

	Environnement e;
	
	public ReceiverEnvironementInstruction(Environnement e) {
		this.e = e;
	}
	
	public void processMessages(WarBrain brain) {
		for (WarMessage m : brain.getMessages()) {
			this.onMessage(m);
		}
	}
	
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
