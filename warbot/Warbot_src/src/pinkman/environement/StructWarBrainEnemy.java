package pinkman.environement;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public class StructWarBrainEnemy extends StructWarBrain {

	private int life;
	private int ttl;
	private int timeWithoutUpdate = 3000;
	
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
		
		if (this.isBase()) {
			if (this.getHealth() <= 800) {
				ttl--;
			}
			this.timeWithoutUpdate++;
		} else {
			ttl--;
		}
		
	}
	
	public int getTimeLife() {
		return timeWithoutUpdate;
	}
	
	public void resetTtl() {
		this.timeWithoutUpdate = 0;
		if(isBase()) {
			ttl = 200;
		} else {
			ttl = 10;
		}
	}
	
	


}
