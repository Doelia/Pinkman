package doe.tasks;

import com.badlogic.gdx.math.Vector2;

import doe.behavior.Behavior;
import doe.environement.Environnement;
import doe.exceptions.NotExistException;
import doe.messages.EnvironnementUpdaterInterface;
import doe.messages.SenderEnvironnementInstruction;

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
		
		if (Behavior.CHEAT) {
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
	
}
