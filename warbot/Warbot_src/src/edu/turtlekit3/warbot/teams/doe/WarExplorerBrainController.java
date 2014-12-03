package edu.turtlekit3.warbot.teams.doe;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarExplorer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarExplorerAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.environnement.AllieNotFoundException;
import edu.turtlekit3.warbot.teams.doe.environnement.Environnement;
import edu.turtlekit3.warbot.teams.doe.environnement.WarBrainUtils;

public class WarExplorerBrainController extends WarExplorerAbstractBrainController {
	
	public WarExplorerBrainController() {
		super();
	}
	
	private Vector2 position = new Vector2(0, 0);

	@Override
	public String action() {
		if (Environnement.chef == null) {
			Environnement.chef = this.getBrain();
		}
		
		WarBrainUtils.updatePosition(this.getBrain(), position);
		
		if (position != null) {
			if (Environnement.chef.getID() != this.getBrain().getID()) {
				Vector2 posChef;
				try {
					posChef = Environnement.getInstance().getPositionOf(Environnement.chef);
					float distance = posChef.dst(this.position);
					if (distance > 5)
						WarBrainUtils.setHeadingOn(this.getBrain(), position, posChef);
				} catch (AllieNotFoundException e) {
				}
			}
			else
				this.getBrain().setRandomHeading(30);
		} else {
			this.getBrain().setDebugString("position null");
		}
		
		return WarExplorer.ACTION_MOVE;
	}
	
}

