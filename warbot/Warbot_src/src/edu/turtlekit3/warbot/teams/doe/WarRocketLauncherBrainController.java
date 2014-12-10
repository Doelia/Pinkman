package edu.turtlekit3.warbot.teams.doe;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.brains.braincontrollers.WarRocketLauncherAbstractBrainController;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.cheat.Team;
import edu.turtlekit3.warbot.teams.doe.cheat.WarBrainUtils;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTeamFoundException;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class WarRocketLauncherBrainController extends WarRocketLauncherAbstractBrainController {

	int x, y;
	int angleModifier;
	boolean justTurned = false;
	public WarRocketLauncherBrainController() {
		super();
		x = - new Random().nextInt() * 1500;
		y = - new Random().nextInt() * 1500;
		angleModifier = new Random().nextInt() * 180 - 90;
	}
	@Override
	public String action() {
		if(justTurned) {
			getBrain().setHeading(getBrain().getHeading() - angleModifier);
		}
		justTurned = false;
		WarBrainUtils.doStuff(this.getBrain());
		Environnement ev = Environnement.getInstance();
		try {
			Team t = ev.getTeamManager().getTeamOf(this.getBrain().getID());
			try {
				int leaderId = t.getLeader();
				if(leaderId != this.getBrain().getID()) {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							t.getMovementPosition(getBrain().getID()));
					if(ev.getEntitiesInRadiusOfWithAngle(this.getBrain().getID(), 20, 60, (int) getBrain().getHeading()).size() > 0) {
						System.out.println("ouch");
						justTurned = true;
						getBrain().setHeading(angleModifier + getBrain().getHeading());
					}
				} else {
					WarBrainUtils.setHeadingOn(
							getBrain(), 
							ev.getStructWarBrain(getBrain().getID()).getPosition(),
							new Vector2(-200, -200));
					if(new Random().nextInt() * 10 < 2) {
						return WarRocketLauncher.ACTION_IDLE;
					}
				}
			} catch (NotExistException e) {
			}

		} catch (NoTeamFoundException e) {
			ev.getTeamManager().affectTeamTo(getBrain().getID());
		}

		return WarRocketLauncher.ACTION_MOVE;
	}
}