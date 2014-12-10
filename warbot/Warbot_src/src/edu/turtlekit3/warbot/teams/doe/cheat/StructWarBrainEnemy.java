package edu.turtlekit3.warbot.teams.doe.cheat;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public class StructWarBrainEnemy extends StructWarBrain {

	private int life;
	
	public StructWarBrainEnemy(int ID, Vector2 posCart, int life, WarAgentType type) {
		super(ID, posCart, type);
		this.life = life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	@Override
	public int getHealth() {
		return this.life;
	}


}
