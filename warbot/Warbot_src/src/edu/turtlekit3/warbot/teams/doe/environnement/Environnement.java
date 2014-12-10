package edu.turtlekit3.warbot.teams.doe.environnement;

import java.util.ArrayList;
import java.util.Collection;
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

	private TeamManager tm;
	private WarBaseBrain mainBase = null;
	public HashMap<Integer, StructWarBrain> listAllies = new HashMap<Integer, StructWarBrain>(); 

	private Environnement() {
		tm = new TeamManager();
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

	public void updatePosition(WarBrain a, Vector2 posCart) {
		StructWarBrain e = this.listAllies.get(a.getID());
		if (e != null) {
			e.setPosition(posCart);
		} else {
			this.listAllies.put(a.getID(), new StructWarBrain(a, posCart));
		}
		a.setDebugString(""+posCart);
		clean();
	}

	public StructWarBrain getStructWarBrain(int id) throws NotExistException {
		clean();
		try {
			return this.listAllies.get(id);
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public void clean() {
		for (StructWarBrain s : listAllies.values()) {
			if(!s.isAlive()) {
				System.out.println("dead");
				listAllies.remove(s.getID());
			}
		}
	}

	public TeamManager getTeamManager() {
		return this.tm;
	}

	public ArrayList<Integer> getEntitiesInRadiusOf(int brainId, int radius){
		ArrayList<Integer> entities = new ArrayList<Integer>();
		try {
			Vector2 outPosition = getStructWarBrain(brainId).getPosition();
			for (StructWarBrain s : getListAllies()) {
				try {
					if(s.getID() != brainId) {
						Vector2 targetPosition = s.getPosition();
						float distance = outPosition.dst(targetPosition);
						if(distance < radius) {
							entities.add(s.getID());
						}
					}
				} catch (Exception e) {}
			}
		} catch (NotExistException e) {
		}
		return entities;
	}

	public ArrayList<Integer> getEntitiesInRadiusOfWithAngle(int brainId, int radius, int angle, int heading) {
		ArrayList<Integer> entities = new ArrayList<Integer>();
		try {
			Vector2 position = new Vector2(getStructWarBrain(brainId).getPosition());
			for (StructWarBrain s : getListAllies()) {
				try {
					if(s.getID() != brainId) {
						Vector2 target = new Vector2(s.getPosition());
//						target.add(position);
//						
//						Vector2 pol = polaireFromCart(target);
//						int teta = (int) Math.abs(pol.x - heading);
//						if(teta < angle && pol.x < radius) {
//							entities.add(s.getID());
//						}
						
						float distance = position.dst(target);
						System.out.println("distance = "+distance);
						if(distance < radius) {
							Vector2 p1 = cartFromPolaire(heading + angle / 2, distance);
							p1.add(position);
							Vector2 p2 = cartFromPolaire(heading - angle / 2, distance);
							p2.add(position);
							Vector2 p3 = new Vector2(position);
							if(isPointInsideTriangle(p1, p2, p3, target)) {
								System.out.println("au");
								entities.add(s.getID());
							}
						}
					}
				} catch (Exception e) {}
			}
		} catch (NotExistException e) {
		}

		return entities;
	}

	public Collection<StructWarBrain> getListAllies() {
		clean();
		return listAllies.values();
	}
	
	private float computeZCoordinate(Vector2 p1, Vector2 p2, Vector2 p3) {
		return p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
	}
	
	private boolean isPointInsideTriangle(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 target) {
		float z1 = computeZCoordinate(p1, p2, target);
		float z2 = computeZCoordinate(p2, p3, target);
		float z3 = computeZCoordinate(p3, p1, target);
		
		return ((z1 > 0) && (z2 > 0) && (z3 > 0)) || ((z1 < 0) && (z2 < 0) && (z3 < 0));
	}


}
