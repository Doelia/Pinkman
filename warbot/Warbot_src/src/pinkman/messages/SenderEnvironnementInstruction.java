package pinkman.messages;

import pinkman.environement.Environnement;
import pinkman.tasks.Task;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
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
		this.sendMessage(TypeUpdateEnv.SET_POSITION_BASE_ATTACKED, String.valueOf(pos), "", "");
		
	}

	@Override
	public void registerExplorer(Integer id) {
		this.sendMessage(TypeUpdateEnv.REGISTER_EXPLORER, String.valueOf(id), "", "");
	}

	@Override
	public void addFreeFood(Vector2 lastFood, int ID) {
		this.sendMessage(TypeUpdateEnv.ADD_FREE_FOOD, String.valueOf(lastFood), String.valueOf(ID), "");
		
	}

	@Override
	public void setMainBase(WarBaseBrain mainBase) {
		this.sendMessage(TypeUpdateEnv.SET_MAIN_BASE, String.valueOf(mainBase), "", "");
		
	}

	@Override
	public void updatePositionOfEnemy(int ID, Vector2 newPosCart, int life,
			WarAgentType type) {
		this.sendMessage(TypeUpdateEnv.UPDATE_POSITION_ENEMY, String.valueOf(ID),  String.valueOf(newPosCart),  String.valueOf(life));
		
	}

	@Override
	public void updatePositionOfAlly(WarBrain e, Vector2 newPosCart,
			WarAgentType type) {
		this.sendMessage(TypeUpdateEnv.UPDATE_POSITION_ALLY, String.valueOf(e),  String.valueOf(newPosCart),  String.valueOf(type));
	}

	@Override
	public void decrementTtlOfAll() {
		this.sendMessage(TypeUpdateEnv.DECREMENT_TTL, "", "", "");
	}
	
	public static Environnement createNewSender(WarBrain b) {
		return Task.getTypeAvailable(b.getTeamName().contains("bis")?0:1);
	}
	
	
	
}
