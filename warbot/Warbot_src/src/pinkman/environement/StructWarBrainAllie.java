package pinkman.environement;

import java.util.ArrayList;

import pinkman.exceptions.NotExistException;

import com.badlogic.gdx.math.Vector2;


import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;

public class StructWarBrainAllie extends StructWarBrain {

	private WarBrain e;
	int lastLife = 0;
	private ArrayList<Vector2> targetFood = new ArrayList<Vector2>();
	
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
	
	public void addTargetFood(Vector2 targetFood) {
		this.targetFood.add(targetFood);
	}
	
	public void setFirstTargetFound() {
		this.targetFood.remove(0);
	}
	
	public Vector2 getTargetFood() {
		if (targetFood.size() > 0)
			return targetFood.get(0);
		else
			return null;
	}
	
	public boolean canTargetNewFood() {
		try {
			return ((this.targetFood.size() + this.getBrain().getNbElementsInBag()) < this.getBrain().getBagSize());
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
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
