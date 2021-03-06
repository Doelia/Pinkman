package pinkman.teams;

import java.util.ArrayList;
import java.util.Random;

import pinkman.environement.Environnement;
import pinkman.exceptions.NoTargetException;
import pinkman.exceptions.NotExistException;
import pinkman.tools.Tools;

import com.badlogic.gdx.math.Vector2;

public class Group {
	private ArrayList<Integer> members;
	private boolean attacking;
	private int requestNumber;
	private int battleModifier;
	private int nbStopAttacking;
	private boolean leaderCanShoot;
	private int angle;
	boolean isBaseAttacked;
	boolean isTargetBase;
	int teamIndex;
	private int voteToChangeTarget;
	private Environnement e;

	private Target target;

	public Group(Environnement e) {
		members = new ArrayList<Integer>();
		attacking = false;
		requestNumber = 99999;
		battleModifier = 0;
		nbStopAttacking = 0;
		isBaseAttacked = false;
		angle = new Random().nextInt(20);
		isTargetBase = false;
		isBaseAttacked = false;
		voteToChangeTarget = 0;
		this.e = e;
		this.target = null;
	}

	public void setTargetID(int id) {
		voteToChangeTarget++;
		if(voteToChangeTarget > getSize() * 10) {
			voteToChangeTarget = 0;
			//			targetID = id;
		}
	}

	//	public int getTargetID() {
	//		return targetID;
	//	}

	public void addMember(Integer w) {
		members.add(w);
	}

	public ArrayList<Integer> getMembers() {
		return this.members;
	}

	public int getLeader() throws NotExistException {
		try {
			return this.getEnvironnement().getStructWarBrain(members.get(0)).getID();
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public boolean contains(Integer id) {
		return members.contains(id);
	}

	public int getSize() {
		return members.size();
	}

	public int getMaxSize() {
		return 5;
	}

	public Vector2 getBattlePosition(Integer brainId) throws NotExistException {
		requestNumber++;
		if(requestNumber > 15 * getSize()) {
			requestNumber = 0;
			battleModifier++;
			battleModifier = battleModifier % getSize();
		}
		return getMovementPosition(brainId);
	}

	public Vector2 getTargetPosition(Integer brainId, int isTop) throws NotExistException {
		try {
			int angle = 360;
			float orientation = 0;
			int dist = 20;

			if(this.hasTarget()) {
				Vector2 enemyPosition = getEnvironnement().getEnemy(target.brainID).getPosition();
				orientation = getEnvironnement().getTeamManager().getIndexOfTeam(this) * (180 / getMaxSize());
				int index = members.indexOf(brainId);
				int nbrPersonnes = members.size();
				float tick = (angle/nbrPersonnes);
				float alpha = tick*index + orientation;
				Vector2 target = Tools.cartFromPolaire(alpha, dist);
				target.add(enemyPosition);
				return target;
			} else {
				return getMovementPosition(brainId);
			}
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public Vector2 getLeaderPositionForWaiting(Integer brainId, Environnement ev, int isOnTop) throws NotExistException {
		try {
			int index = teamIndex;
			int nbrPersonnes = ev.getTeamManager().size();
			float tick = (180/nbrPersonnes) * isOnTop;
			float alpha = tick*index;
			Vector2 target = Tools.cartFromPolaire(alpha, 80);
			target.add(ev.getStructWarBrain(ev.getMainBase().getID()).getPosition());
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	private Environnement getEnvironnement() {
		return e;
	}

	public Vector2 getBaseAttackPosition(Integer brainId) throws NotExistException {
		try {
			Vector2 base = getEnvironnement().getPositionFirstEnemyBase();
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size();
			float tick = (360/nbrPersonnes);
			float alpha = tick*index + getEnvironnement().getIndexOfTeam(this) * (360 / getEnvironnement().getTeamManager().size()) + 20;
			Vector2 target = Tools.cartFromPolaire(alpha, 20);
			target.add(base);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public Vector2 getMovementPosition(Integer brainId) throws NotExistException {
		try {
			Vector2 position = new Vector2(getEnvironnement().getStructWarBrain(getLeader()).getPosition());

			int index = 1 + (members.indexOf(brainId) + battleModifier) % (getSize() - 1);
			int nbrPersonnes = members.size() - 1;
			float tick = (360/nbrPersonnes);
			float alpha = tick*index;
			Vector2 target = Tools.cartFromPolaire(alpha, (isAttacking())?15:20);
			target.add(position);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public Vector2 getDefensePosition(Integer brainId) throws NotExistException {
		try {
			Vector2 position = getEnvironnement().getBaseAttacked();
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size();
			float tick = (360/nbrPersonnes);
			float alpha = tick*index + angle;
			Vector2 target = Tools.cartFromPolaire(alpha, 30);
			target.add(position);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	//	public void setTarget(Vector2 target, boolean isTargetBase) {
	//		if(voteToChangeTarget > getSize() * 10) {
	//			this.t = target;
	//			this.isTargetBase = isTargetBase;
	//		}
	//	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		if(!attacking) {
			nbStopAttacking++;
		}
		if(nbStopAttacking > getSize() * 1) {
			nbStopAttacking = 0;
			this.attacking = false;
		}
		if(attacking) {
			nbStopAttacking = 0;
			this.attacking = true;
		}

	}

	public void removeMember(Integer id) {
		members.remove(id);
	}

	public boolean canShoot() {
		return leaderCanShoot;
	}

	public void setLeaderCanShoot(boolean b) {
		leaderCanShoot = b;
	}

	public void setBaseAttacked(boolean b) {
		this.isBaseAttacked = b;
	}

	public boolean isBaseAttacked() {
		return this.isBaseAttacked;
	}

	public boolean isTargetBase() {
		return isTargetBase;
	}

	public boolean isBaseAttackTeam() {
		return teamIndex % 2 == 0;
	}

	public void setTeamIndex(int index) {
		this.teamIndex = index;
	}

	public void setTarget(Target t) {
		this.target = t;
	}

	public Target getTarget() throws NoTargetException {
		if(target == null) {
			throw new NoTargetException();
		}
		return target;
	}

	public boolean hasTarget() {
		return target != null;
	}
}
