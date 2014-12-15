package doe.environement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.OrderedMap;

import doe.exceptions.BaseNotFoundException;
import doe.exceptions.NoTargetFoundException;
import doe.exceptions.NotExistException;
import doe.messages.EnvironnementUpdaterInterface;
import doe.teams.Group;
import doe.teams.TeamManager;
import doe.tools.Tools;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.brains.WarBaseBrain;

public class Environnement implements EnvironnementUpdaterInterface {

	private TeamManager tm;
	private WarBaseBrain mainBase;
	private Stack<Integer> takenFood;
	private HashMap<Integer, StructWarBrainAllie> listAllies;
	private OrderedMap<Integer, StructWarBrainEnemy> listEnemies;
	private Boolean weAreInTop; // En haut à droite, null si on sait pas encore
	private ArrayList<Integer> explorers;
	private HashMap<Integer, Integer> mainBases;
	private Vector2 baseAttacked;

	public Environnement() {
		tm = new TeamManager(this);
		mainBase = null;
		takenFood = new Stack<Integer>();
		listAllies = new HashMap<Integer, StructWarBrainAllie>(); 
		listEnemies = new OrderedMap<Integer, StructWarBrainEnemy>();
		weAreInTop = null;
		explorers = new ArrayList<Integer>();
		mainBases = new HashMap<Integer, Integer>();
	}

	/*** ECRITURE **/

	public void setWeAreInTop(boolean weAreInTop) {
		this.weAreInTop = weAreInTop;
	}
	
	public void setPositionBaseAttacked(Vector2 pos) {
		baseAttacked = pos;
	}

	public void registerExplorer(Integer id) {
		if (!explorers.contains(id)) {
			explorers.add(id);
		}
	}

	public int getExplorerIndex(Integer id) {
		return explorers.indexOf(id);
	}


	public void addFreeFood(Vector2 lastFood, int ID) {
		if (!this.takenFood.contains(ID)) {
			try {
				StructWarBrainAllie s = Tools.getClosest(this.getExplorersCanTakeFood(), lastFood);
				s.addTargetFood(lastFood);
				this.takenFood.add(ID);
			} catch (NoTargetFoundException e) {
			}
		}
	}

	public void setMainBase(WarBaseBrain mainBase) {
		cleanMainBases();
		mainBases.put(mainBase.getID(), 2 * mainBases.size());

		boolean mainBaseAlive = true;
		try{
			mainBaseAlive = (getMainBase().getHealth() > 0);
		} catch (Exception e){
			mainBaseAlive = false;
			this.mainBase = null;
		}
		if (!this.mainBaseIsDefined() || !mainBaseAlive) {
			this.mainBase = mainBase;
		}
	}

	public void updatePositionOfEnemy(int ID, Vector2 newPosCart, int life, WarAgentType type) {
		this.clean();
		StructWarBrainEnemy s = this.listEnemies.get(ID);
		if (s != null) {
			s.setPosition(newPosCart);
			s.setLife(life);
			s.resetTtl();
		} else {
			StructWarBrainEnemy x = new StructWarBrainEnemy(ID, newPosCart, life, type);
			this.listEnemies.put(ID, x);
		}
		this.clean();
	}

	public void updatePositionOfAlly(WarBrain e, Vector2 newPosCart, WarAgentType type) {
		this.clean();
		StructWarBrain s = this.listAllies.get(e.getID());
		if (s != null) {
			s.setPosition(newPosCart);
		} else {
			StructWarBrainAllie x = new StructWarBrainAllie(e, newPosCart, type);
			this.listAllies.put(e.getID(), x);
		}
		this.clean();
	}

	public void decrementTtlOfAll() {
		for (StructWarBrainEnemy e : this.getEnemies()) {
			e.decrementTtl();
		}
	}

	/** LECTURE **/
	
	private void cleanMainBases() {
		for (Integer integer : mainBases.keySet()) {
			if(mainBases.get(integer) == 0 && integer == mainBase.getID()) {
				mainBase = null;
			}
			mainBases.put(integer, mainBases.get(integer) - 1);
		}
	}
	
	private void clean() {
		try {
			for (StructWarBrain s : listAllies.values()) {
				if (!s.isAlive() || !s.positionIsUptodate()) {
					listAllies.remove(s.getID());
					tm.remove(s.getID());
				}
			}
		} catch(Exception e) {}
		try {
			for (StructWarBrainEnemy s : listEnemies.values()) {
				if (!s.isAlive() || s.getTtl() <= 0) {
					listEnemies.remove(s.getID());
				}
			}
		} catch(Exception e) { }
	}

	public int getNumberOfBases() {
		return mainBases.size(); 
	}
	
	public boolean inRush() {
		return (this.getNumberOfBases() <= 1);
	}

	public boolean ourBaseIsFound() {
		try {
			this.getWeAreInTop();
			return true;
		} catch (BaseNotFoundException e) {
			return false;
		}
	}

	// TODO Vérifier que les coordonées sont bien dans la carte, au cas ou si la carte change
	public Vector2 getApproxEnemyBasePosition() throws BaseNotFoundException {
		boolean top;
		try {
			top = getWeAreInTop();
			if(top) {
				return new Vector2(-750, -400);
			} else {
				return new Vector2(750, 400);
			}
		} catch (BaseNotFoundException e) {
			throw new BaseNotFoundException();
		}

	}

	public boolean isMainBase(WarBaseBrain b) {
		return (mainBaseIsDefined() && b.getID() == this.mainBase.getID());
	}

	public WarBaseBrain getMainBase() {
		return mainBase;
	}

	public boolean getWeAreInTop() throws BaseNotFoundException {
		if (weAreInTop == null)
			throw new BaseNotFoundException();
		return weAreInTop;
	}

	public int getIndexOfTeam(Group t) {
		return tm.getIndexOfTeam(t);
	}

	public ArrayList<StructWarBrainAllie> getExplorersCanTakeFood() {
		this.clean();
		ArrayList<StructWarBrainAllie> list = new ArrayList<StructWarBrainAllie>();
		for (StructWarBrainAllie s : this.getListAllies()) {
			try {
				if (s.getType() == WarAgentType.WarExplorer && s.canTargetNewFood()) {
					list.add(s);
				}
			} catch (Exception e) {}
		}
		return list;
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

	public Vector2 getPositionFirstEnemyBase() throws NotExistException {
		this.clean();
		try {
			int id = this.getEnemyBases().get(0);
			return this.getEnemy(id).getPosition();
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public int getFirstEnemyBase() {
		return this.getEnemyBases().get(0);
	}

	public boolean oneBaseIsFound() {
		return (this.getEnemyBases().size() > 0);
	}

	public boolean mainBaseIsDefined() {
		return (this.mainBase != null);
	}

	public Values<StructWarBrainEnemy> getEnemies() {
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

	public StructWarBrainAllie getStructWarBrain(int id) throws NotExistException {
		clean();
		try {
			if ( this.listAllies.get(id) == null)
				throw new NotExistException();
			return this.listAllies.get(id);
		} catch (Exception e) {
			throw new NotExistException();
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
		double minDistance = 30;
		int id = -1;
		try {
			for (StructWarBrainEnemy s : this.listEnemies.values()) {
				if(s.getType() == WarAgentType.WarBase 
						|| s.getType() == WarAgentType.WarRocketLauncher
						|| s.getType() == WarAgentType.WarTurret
						|| s.getType() == WarAgentType.WarKamikaze) {
					double dst = position.dst(s.getPosition());
					if (dst < minDistance) {
						minDistance = position.dst(s.getPosition());
						id = s.getID();
					}
				}
			}
			return id;
		} catch (Exception e) {
			System.out.println("error");
			throw new NoTargetFoundException();
		}
	}

	public int getNumberOfType(WarAgentType type) {
		int cpt = 0;
		for (StructWarBrain s : this.getListAllies()) {
			if (s.getType() == type)
				cpt++;
		}
		return cpt;
	}

	public Vector2 getPositionAllieBaseWithLowLife() {
		Vector2 base = new Vector2(0, 0);
		int life = 12000;
		for (StructWarBrainAllie s : this.getListAllies()) {
			if (s.isBase() && s.getHealth() < life) {
				try {
					base = s.getPosition();
					life = s.getHealth();
				} catch (NotExistException e) {
				}
			}
		}
		return base;
	}

	public int getBiggestBaseId() {
		int big = 0;
		for (StructWarBrainAllie s : this.getListAllies()) {
			if (s.getType() == WarAgentType.WarBase)
				if (s.getID() > big) {
					big = s.getID();
				}
		}
		return big;
	}

	public Vector2 getBaseAttacked() {
		return baseAttacked;
	}
}
