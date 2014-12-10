package edu.turtlekit3.warbot.teams.doe.cheat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;
import edu.turtlekit3.warbot.teams.doe.exceptions.NoTargetFoundException;
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
	public HashMap<Integer, StructWarBrainEnemy> listEnemies = new HashMap<Integer, StructWarBrainEnemy>();

	private Environnement() {
		tm = new TeamManager();
	}

	public boolean isMainBase(WarBaseBrain b) {
		return (mainBaseIsDefined() && b.getID() == this.mainBase.getID());
	}
	
	public ArrayList<Integer> getEnemyBases() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (StructWarBrainEnemy s : this.getEnemies()) {
			if (s.isBase()) {
				list.add(s.getID());
			}
		}
		return list;
	}

	public boolean mainBaseIsDefined() {
		return (this.mainBase != null);
	}

	public void setMainBase(WarBaseBrain mainBase) {
		if (!this.mainBaseIsDefined())
			this.mainBase = mainBase;
	}
	
	public Collection<StructWarBrainEnemy> getEnemies() {
		return listEnemies.values();
	}
	
	public StructWarBrain getEnemy(int enemyId) throws NotExistException {
		this.clean();
		try {
			return listEnemies.get(enemyId);
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public void updatePositionOfEnemy(int ID, Vector2 newPosCart, int life, WarAgentType type) {
		this.clean();
		StructWarBrainEnemy s = this.listEnemies.get(ID);
		if (s != null) {
			s.setPosition(newPosCart);
			s.setLife(life);
		} else {
			StructWarBrainEnemy x = new StructWarBrainEnemy(ID, newPosCart, life, type);
			this.listEnemies.put(ID, x);
		}
		this.clean();
	}

	public void updatePositionOfALlie(WarBrain e, Vector2 newPosCart, WarAgentType type) {
		this.clean();
		StructWarBrain s = this.listAllies.get(e.getID());
		if (s != null) {
			s.setPosition(newPosCart);
		} else {
			StructWarBrainAllie x = new StructWarBrainAllie(e, newPosCart, type);
			this.listAllies.put(e.getID(), x);
		}
		e.setDebugString(""+newPosCart);
		this.clean();
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
		for (StructWarBrainEnemy s : listEnemies.values()) {
			if (!s.isAlive()) {
				listAllies.remove(s.getID());
			} else {
				if (!s.isBase() && !s.positionIsUptodate()) {
					listAllies.remove(s.getID());
				}
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
		this.clean();
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
		this.clean();
		return listAllies.values();
	}
	
	public int getClosestEnemy(Vector2 position) throws NoTargetFoundException {
		this.clean();
		try {
			StructWarBrainEnemy plusProche = this.listEnemies.get(0);
			for (StructWarBrainEnemy s : this.listEnemies.values()) {
				if (position.dst(s.getPosition()) <
					plusProche.getPosition().dst(s.getPosition())) {
					plusProche = s;
				}
			}
			return plusProche.getID();
		} catch (Exception e) {
			throw new NoTargetFoundException();
		}
	}


}
