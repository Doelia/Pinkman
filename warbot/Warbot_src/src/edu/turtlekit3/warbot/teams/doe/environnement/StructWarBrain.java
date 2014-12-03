package edu.turtlekit3.warbot.teams.doe.environnement;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;

public class StructWarBrain {

	private WarBrain e;
	private Vector2 posCart;
	private int ID;

	public StructWarBrain(WarBrain e, Vector2 posCart) {
		super();
		this.e = e;
		this.posCart = posCart;
		this.ID = e.getID();
	}

	public boolean isAlive() {
		try {
			return e.getHealth() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public WarBrain getBrain() throws NotExistException {
		if(!isAlive()) {
			throw new NotExistException();
		}
		return e;
	}
	
	public Vector2 getPosition() throws NotExistException {
		if(!isAlive()) {
			throw new NotExistException();
		}
		return posCart;
	}
	
	public int getID(){
		return ID;
	}

	public void setPosition(Vector2 posCart) {
		this.posCart = posCart;
	}
}
