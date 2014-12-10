package edu.turtlekit3.warbot.teams.doe.cheat;

import com.badlogic.gdx.math.Vector2;

public class StructWarBrainEnemy extends StructWarBrain {

	private int life;
	
	public StructWarBrainEnemy(int ID, Vector2 posCart, int life) {
		super(ID, posCart);
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
