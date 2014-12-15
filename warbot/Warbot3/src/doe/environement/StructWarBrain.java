package doe.environement;

import java.util.Date;

import com.badlogic.gdx.math.Vector2;

import doe.exceptions.NotExistException;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public abstract class StructWarBrain {

	private Vector2 posCart;
	private int ID;
	private Date lastUpdatePosition;
	private WarAgentType type;
	
	public static int LIFE_TIME = 3; // En secondes

	public StructWarBrain(int ID, Vector2 posCart, WarAgentType type) {
		super();
		this.type = type;
		this.posCart = posCart;
		this.lastUpdatePosition = new Date();
		this.ID = ID;
	}
	
	public WarAgentType getType() {
		return type;
	}

	public boolean isAlive() {
//		try {
//			return this.getHealth() > 0;
//		} catch (Exception e) {
//			return false;
//		}
		return this.getHealth() > 0;
	}
	
	public abstract int getHealth();

	public Vector2 getPosition() throws NotExistException {
		if (!isAlive()) {
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
	
	public boolean isBase() {
		return (this.type.equals(WarAgentType.WarBase));
	}
	
}
