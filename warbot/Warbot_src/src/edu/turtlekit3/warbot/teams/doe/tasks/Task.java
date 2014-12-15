package edu.turtlekit3.warbot.teams.doe.tasks;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public abstract class Task {

	protected WarBrain brain;
	protected WarAgentType type;
	protected Environnement e;
	
	public Task(WarBrainController brain, WarAgentType type, Environnement e){
		this.brain = brain.getBrain();
		this.type = type;
		this.e = e;
	}
	
	public abstract boolean exec();
	
	public Vector2 getCurentPosition() throws NotExistException {
		try {
			return e.getStructWarBrain(this.getBrain().getID()).getPosition();
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	protected WarBrain getBrain() {
		return brain;
	}
	
}
