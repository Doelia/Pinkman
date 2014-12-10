package edu.turtlekit3.warbot.teams.doe.cheat;

import java.util.Date;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public abstract class StructWarBrain {

	private Vector2 posCart;
	private int ID;
	private Date lastUpdatePosition;
	
	public static int LIFE_TIME = 10; // En secondes

	public StructWarBrain(int ID, Vector2 posCart) {
		super();
		this.posCart = posCart;
		this.lastUpdatePosition = new Date();
	}

	public boolean isAlive() {
		try {
			return this.getHealth() > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	public abstract int getHealth();

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
		this.lastUpdatePosition = new Date();
	}
	
	
	public boolean positionIsUptodate() {
		Date now = new Date();
		long ms = now.getTime() - this.lastUpdatePosition.getTime();
		return (ms/1000 < LIFE_TIME);
	}
	
}
