package edu.turtlekit3.warbot.teams.doe.cheat;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class StructWarBrainAllie extends StructWarBrain {

	private WarBrain e;
	
	public StructWarBrainAllie(WarBrain e, Vector2 posCart) {
		super(e.getID(), posCart);
		this.e = e;
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
	

}
