package pinkman.tasks;

import pinkman.environement.Environnement;
import pinkman.exceptions.NotExistException;
import pinkman.tools.Tools;

import com.badlogic.gdx.math.Vector2;


import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;

public class MoveTask extends Task {

	private Vector2 target = null;
	
	public MoveTask(WarBrainController brain, WarAgentType type, Environnement e) {
		super(brain, type, e);
		e.registerExplorer(getBrain().getID());
	}

	@Override
	public boolean exec() {
		
		try {
			WarExplorerBrain b = (WarExplorerBrain) this.getBrain();
			
			if (b.isBlocked()) {
				getBrain().setHeading(getBrain().getHeading()+20);
			} else {
				if (target == null) {
					this.getBrain().setDebugString("randomised");
					getBrain().setRandomHeading(20);
				} else {
					Tools.setHeadingOn(this.getBrain(), this.getCurentPosition(), target);
				}
			}
		
		} catch (NotExistException e) {
			this.getBrain().setRandomHeading(20);
		}
		return true;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}

	public Vector2 getTarget() {
		return target;
	}
}
