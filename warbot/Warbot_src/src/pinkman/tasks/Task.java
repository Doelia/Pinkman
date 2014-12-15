package pinkman.tasks;

import pinkman.environement.Environnement;
import pinkman.exceptions.NotExistException;
import pinkman.messages.EnvironnementUpdaterInterface;
import pinkman.messages.SenderEnvironnementInstruction;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class Task {

	protected WarBrain brain;
	protected WarAgentType type;
	protected Environnement e;
	protected EnvironnementUpdaterInterface sender;
	
	public Task(WarBrainController brain, WarAgentType type, Environnement e){
		this.brain = brain.getBrain();
		this.type = type;
		this.e = e;
		
		if (SetBaseAttackedTask.isdefine) {
			sender = this.e;
		} else {
			sender = new SenderEnvironnementInstruction(brain.getBrain());
		}
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
	
	
	public static Environnement getTypeAvailable(int i) {
		if (Environnement.types == null) {
			Environnement.types = new Environnement[2];
			Environnement.types[0] = new Environnement();
			Environnement.types[1] = new Environnement();
		}
		return Environnement.types[i];
	}
	
}
