package edu.turtlekit3.warbot.teams.doe;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.brains.braincontrollers.WarBaseAbstractBrainController;
import edu.turtlekit3.warbot.teams.demo.Constants;
import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;

public class WarBaseBrainController extends WarBaseAbstractBrainController {

	public WarBaseBrainController() {
		super();
	}
	
	private void broadcastPosition() {
		getBrain().broadcastMessageToAll(Constants.here, "");
	}
	
	@Override
	public String action() {
		
		//System.out.println("coucou");
		Vector2 v = new Vector2(1,1);
		//System.out.println(Environnement.polaireFromCart(v));
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Environnement.getInstance().setMainBase(this.getBrain());
		
		// Develop behaviour here
		
		if (Environnement.getInstance().isMainBase(this.getBrain()))
				this.broadcastPosition();
		
		return WarBase.ACTION_IDLE;
	}
}
 