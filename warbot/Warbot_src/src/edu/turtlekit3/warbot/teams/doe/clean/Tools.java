package edu.turtlekit3.warbot.teams.doe.clean;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTargetFoundException;

public class Tools {

	public static float computeZCoordinate(Vector2 p1, Vector2 p2, Vector2 p3) {
		return p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
	}
	
	public static boolean isPointInsideTriangle(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 target) {
		float z1 = computeZCoordinate(p1, p2, target);
		float z2 = computeZCoordinate(p2, p3, target);
		float z3 = computeZCoordinate(p3, p1, target);
		return ((z1 > 0) && (z2 > 0) && (z3 > 0)) || ((z1 < 0) && (z2 < 0) && (z3 < 0));
	}
	
	/**
	 * 
	 * @param angle en degres
	 * @param dist
	 * @return
	 */
	public static Vector2 cartFromPolaire(double angle, double dist) {
		double rad = Math.toRadians(angle);
		return new Vector2((float) (-dist*Math.cos(rad)), (float) (dist*Math.sin(rad)));
	}
	
	/**
	 * 
	 * @param vec retourn teta en radians
	 * @return
	 */
	public static Vector2 polaireFromCart(Vector2 vec) {
		float teta = (float) Math.atan2(vec.y, vec.x);
		int distance = (int) Math.hypot(vec.x, vec.y);
		return new Vector2((float) Math.toDegrees(teta), (float) distance);
	}
	
	/**
	 * Fait pointer brain vers la position voulue
	 */
	public static void setHeadingOn(WarBrain brain, Vector2 pos, Vector2 target) {
		Vector2 sortie = new Vector2(0,0);
		sortie.sub(pos);
		sortie.add(target);
		brain.setHeading(-sortie.angle());
	}
	
	public static Vector2 getPositionOfEntityFromMine(Vector2 myPosition, double angle, double distance) {
		Vector2 posCart = Tools.cartFromPolaire(angle + 180, distance);
		posCart = posCart.add(myPosition);
		return posCart;
	}
	
	public static boolean isNextTo(Vector2 me, Vector2 target, double dist) {
		return (me.dst(target) < dist);
	}
	
	public static boolean isSame(Vector2 v1, Vector2 v2) {
		return (v1.dst(v2) < 1);
	}
	
	public static Vector2 getRandomPositionOnMap() {
		int x = - (int) (Math.random()*500);
		int y = - (int) (Math.random()*500);
		return new Vector2(x,y);
	}
	
	public static StructWarBrainAllie getClosest(ArrayList<StructWarBrainAllie> list, Vector2 position) throws NoTargetFoundException {
		double minDistance = 10000000;
		try {
			StructWarBrainAllie id = list.get(0);
			for (StructWarBrainAllie s : list) {
				double dst = position.dst(s.getPosition());
				if (dst < minDistance) {
					minDistance = position.dst(s.getPosition());
					id = s;
				}
			}
			return id;
		} catch (Exception e) {
			throw new NoTargetFoundException();
		}
	}


	
}
