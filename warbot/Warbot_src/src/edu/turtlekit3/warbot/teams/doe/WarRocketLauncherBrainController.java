package edu.turtlekit3.warbot.teams.doe;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.environnement.Environnement;
import edu.turtlekit3.warbot.teams.doe.environnement.NoTeamFoundException;
import edu.turtlekit3.warbot.teams.doe.environnement.NotExistException;
import edu.turtlekit3.warbot.teams.doe.environnement.Team;
import edu.turtlekit3.warbot.teams.doe.environnement.WarBrainUtils;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x, y;
	int angleModifier;
	public WarRocketLauncherBrainController() {
		super();
		x = - new Random().nextInt() * 1500;
		y = - new Random().nextInt() * 1500;
		angleModifier = new Random().nextInt() * 90;
	}
	@Override
	public String action() {
		
		WarBrainUtils.updatePositionInEnvironnement(this.getBrain());
		Environnement ev = Environnement.getInstance();
		try {
			Team t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != this.getBrain().getID()) {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getMovementPosition(this.getBrain().getID()));
				} else {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(-500, -500));
				}
			} catch (NotExistException e) {
			}
			
		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}
		
		return WarRocketLauncher.ACTION_MOVE;
	}
}