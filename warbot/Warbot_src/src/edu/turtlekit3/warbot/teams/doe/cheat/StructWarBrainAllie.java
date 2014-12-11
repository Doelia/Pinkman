package edu.turtlekit3.warbot.teams.doe.cheat;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class StructWarBrainAllie extends StructWarBrain {

	private WarBrain e;
	int lastLife = 0;
	
	public StructWarBrainAllie(WarBrain e, Vector2 posCart, WarAgentType type) {
		super(e.getID(), posCart, type);
		this.e = e;
		this.lastLife = this.getHealth();
	}
	
	public WarBrain getBrain() throws NotExistException {
		if(!isAlive()) {
			throw new NotExistException();
		}
		return e;
	}
	
	public int getHealth() {
		return e.getHealth();
	}
	
	public void setLastLife() {
		this.lastLife = this.getHealth();
	}
	
	public int getLastLife() {
		return lastLife;
	}
	

}
