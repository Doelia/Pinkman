package edu.turtlekit3.warbot.teams.doe.environnement;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;

public class Environnement {

	private static Environnement instance;
	public static Environnement getInstance() {
		if (instance == null) {
			instance = new Environnement();
		}
		return instance;
	}
	
	private WarBaseBrain mainBase = null;
	public HashMap<Integer, StructWarBrain> listAllies = new HashMap<Integer, StructWarBrain>(); 
	
	private Environnement() {
		
	}
	
	public boolean isMainBase(WarBaseBrain b) {
		return (mainBaseIsDefined() && b.getID() == this.mainBase.getID());
	}
	
	public boolean mainBaseIsDefined() {
		return (this.mainBase != null);
	}
	
	public void setMainBase(WarBaseBrain mainBase) {
		if (!this.mainBaseIsDefined())
			this.mainBase = mainBase;
	}
	
	public static void cartFromPolaire(Vector2 vect, double angle, double dist) {
		double rad = Math.toRadians(angle);
		vect.set((float) (-dist*Math.cos(rad)), (float) (dist*Math.sin(rad)));
	}
	
	public void updatePosition(WarBrain a, Vector2 posCart) {
		StructWarBrain e = this.listAllies.get(a.getID());
		if (e != null) {
			e.posCart = posCart;
		} else {
			this.listAllies.put(a.getID(), new StructWarBrain(a, posCart));
		}
		//a.setDebugString(""+posCart);
		
		
	}
	
	
	
	
	
	
	
}
