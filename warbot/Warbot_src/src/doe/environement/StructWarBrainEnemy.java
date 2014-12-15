package doe.environement;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public class StructWarBrainEnemy extends StructWarBrain {

	private int life;
	private int ttl;
	
	public StructWarBrainEnemy(int ID, Vector2 posCart, int life, WarAgentType type) {
		super(ID, posCart, type);
		this.life = life;
		this.resetTtl();
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	@Override
	public int getHealth() {
		return this.life;
	}
	
	public int getTtl() {
		return ttl;
	}
	
	public void decrementTtl() {
		if (!this.isBase() || this.getHealth() <= 400) {
			ttl--;
		}
	}
	
	public void resetTtl() {
		if(isBase()) {
			ttl = 100;
		} else {
			ttl = 50;
		}
	}
	
	


}
