package doe.messages;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;


public class SenderEnvironnementInstruction implements EnvironnementUpdaterInterface {

	WarBrain e;
	public SenderEnvironnementInstruction(WarBrain e) {
		this.e = e;
	}
	
	private void sendMessage(String type, String p1, String p2, String p3) {
		e.broadcastMessageToAll(type, p1, p2, p3);
	}

	@Override
	public void setWeAreInTop(boolean weAreInTop) {
		this.sendMessage(TypeUpdateEnv.SET_WERE_TOP, String.valueOf(weAreInTop), "", "");
	}

	@Override
	public void setPositionBaseAttacked(Vector2 pos) {
		this.sendMessage(TypeUpdateEnv.setPositionBaseAttacked, String.valueOf(pos), "", "");
		
	}

	@Override
	public void registerExplorer(Integer id) {
		this.sendMessage(TypeUpdateEnv.registerExplorer, String.valueOf(id), "", "");
		
	}


	@Override
	public void addFreeFood(Vector2 lastFood, int ID) {
		this.sendMessage(TypeUpdateEnv.addFreeFood, String.valueOf(lastFood), String.valueOf(ID), "");
		
	}

	@Override
	public void setMainBase(WarBaseBrain mainBase) {
		this.sendMessage(TypeUpdateEnv.setMainBase, String.valueOf(mainBase), "", "");
		
	}
	
	
}
