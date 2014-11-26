package edu.turtlekit3.warbot.teams.doe;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.environnement.WarBrainUtils;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {
	
	public WarExplorerBrainController() {
		super();
	}
	
	private Vector2 position = new Vector2(0, 0);

	@Override
	public String action() {
		// Develop behaviour here
		
		WarBrainUtils.updatePosition(this.getBrain(), position);
		
		//if (getBrain().isBlocked())
		//	getBrain().setRandomHeading();
		
		if (position != null) {
			WarBrainUtils.setHeadingOn(this.getBrain(), position, new Vector2(0,0));
			this.getBrain().setDebugString(this.getBrain().getHeading()+"");
		} else {
			this.getBrain().setDebugString("position null");
		}
			
		
		
		
		return WarExplorer.ACTION_MOVE;
	}
}

