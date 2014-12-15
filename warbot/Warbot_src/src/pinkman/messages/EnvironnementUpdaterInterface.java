package pinkman.messages;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;

public interface EnvironnementUpdaterInterface {

	public void setWeAreInTop(boolean weAreInTop);
	public void setPositionBaseAttacked(Vector2 pos);
	public void registerExplorer(Integer id);
	public void addFreeFood(Vector2 lastFood, int ID);
	public void setMainBase(WarBaseBrain mainBase);
	public void updatePositionOfEnemy(int ID, Vector2 newPosCart, int life, WarAgentType type);
	public void updatePositionOfAlly(WarBrain e, Vector2 newPosCart, WarAgentType type);
	public void decrementTtlOfAll();
	
}
