package edu.turtlekit3.warbot.teams.doe.messages;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;

public interface EnvironnementUpdaterInterface {

	public void setWeAreInTop(boolean weAreInTop);
	public void setPositionBaseAttacked(Vector2 pos);
	public void registerExplorer(Integer id);
	public void setKilledFirstBase();
	public void voteToKillBase(Integer baseId);
	public void addFreeFood(Vector2 lastFood, int ID);
	public void setMainBase(WarBaseBrain mainBase);
	
}
