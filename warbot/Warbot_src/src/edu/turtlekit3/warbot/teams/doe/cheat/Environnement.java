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
	public HashMap<Integer, StructWarBrainAllie> listAllies = new HashMap<Integer, StructWarBrainAllie>(); 
	public HashMap<Integer, StructWarBrainEnemy> listEnnemis = new HashMap<Integer, StructWarBrainEnemy>();

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

	public void updatePositionOfEnemy(int ID, Vector2 newPosCart, int life) {
		StructWarBrainEnemy s = this.listEnnemis.get(ID);
		if (s != null) {
			s.setPosition(newPosCart);
			s.setLife(life);
		} else {
			StructWarBrainEnemy x = new StructWarBrainEnemy(ID, newPosCart, life);
			this.listEnnemis.put(ID, x);
		}
		clean();
	}

	public void updatePositionOfALlie(WarBrain e, Vector2 newPosCart) {
		StructWarBrain s = this.listAllies.get(e.getID());
		if (s != null) {
			s.setPosition(newPosCart);
		} else {
			StructWarBrainAllie x = new StructWarBrainAllie(e, newPosCart);
			this.listAllies.put(e.getID(), x);
		}
		e.setDebugString(""+newPosCart);
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
			if (!s.isAlive() || !s.positionIsUptodate()) {
				listAllies.remove(s.getID());
			}
		}
		for (StructWarBrain s : listEnnemis.values()) {
			if (!s.isAlive() || !s.positionIsUptodate()) {
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

	public Collection<StructWarBrainAllie> getListAllies() {
		clean();
		return listAllies.values();
	}
	
	public StructWarBrainEnemy getClosestEnemy(Vector2 position) throws NoTargetFoundException {
		try {
			StructWarBrainEnemy plusProche = this.listEnnemis.get(0);
			for (StructWarBrainEnemy s : this.listEnnemis.values()) {
				if (position.dst(plusProche.getPosition()) <
					plusProche.getPosition().dst(s.getPosition())) {
					plusProche = s;
				}
			}
			return plusProche;
		} catch (Exception e) {
			throw new NoTargetFoundException();
		}
	}


}
