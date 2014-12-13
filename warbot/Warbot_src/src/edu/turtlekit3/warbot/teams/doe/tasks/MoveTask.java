package edu.turtlekit3.warbot.teams.doe.tasks;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.brains.brains.WarExplorerBrain;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class MoveTask extends Task {

	private Vector2 target = null;
	
	public MoveTask(WarBrainController brain, WarAgentType type, Environnement e) {
		super(brain, type, e);
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
