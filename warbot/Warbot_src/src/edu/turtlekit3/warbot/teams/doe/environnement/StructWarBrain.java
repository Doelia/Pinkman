package edu.turtlekit3.warbot.teams.doe.environnement;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.demo.Constants;

public class StructWarBrain {

	public WarBrain e;
	public Vector2 posCart;
	
	
	
	public StructWarBrain(WarBrain e, Vector2 posCart) {
		super();
		this.e = e;
		this.posCart = posCart;
	}
	
	
	
}
