package edu.turtlekit3.warbot.teams.doe.cheat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;
import edu.turtlekit3.warbot.teams.doe.Tools;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

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
						float distance = position.dst(target);
						System.out.println("distance = "+distance);
						if(distance < radius) {
							Vector2 selfPos = listAllies.get(brainId).getPosition();
							Vector2 otherPos = listAllies.get(s.getID()).getPosition();
							float a = selfPos.sub(otherPos).angle();
							
							if(heading - a > angle / 2 || heading + a < angle / 2) {
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
	


}
