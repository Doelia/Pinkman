package edu.turtlekit3.warbot.teams.doe.teams;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.teams.doe.cheat.Behavior;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;
import edu.turtlekit3.warbot.teams.doe.tools.Tools;

public class Group {
	private ArrayList<Integer> members;
	private Vector2 target;
	private boolean attacking;
	private int requestNumber;
	private int battleModifier;
	private int nbStopAttacking;
	private boolean leaderCanShoot;
	private int angle;
	boolean isBaseAttacked;
	boolean isTargetBase;
	int teamIndex;
	private boolean ready;
	Environnement e = null;

	public Group() {
		members = new ArrayList<Integer>();
		attacking = false;
		target = new Vector2();
		requestNumber = 0;
		battleModifier = 0;
		nbStopAttacking = 0;
		isBaseAttacked = false;
		angle = new Random().nextInt(20);
		isTargetBase = false;
		isBaseAttacked = false;
		ready = false;
	}

	public boolean isReady() {
		return this.ready;
	}
	
	public void addMember(Integer w) {
		members.add(w);
		if(members.size() == getMaxSize() && !ready) {
			ready = true;
		}
	}
	
	public ArrayList<Integer> getMembers() {
		return this.members;
	}

	public int getLeader() throws NotExistException {
		try {
			return Environnement.getInstance().getStructWarBrain(members.get(0)).getID();
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
		return 8;
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

	public Vector2 getTargetPosition(Integer brainId) throws NotExistException {
		try {
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size();
			float tick = (360/nbrPersonnes);
			float alpha = tick*index;
			Vector2 target = Tools.cartFromPolaire(alpha, 20);
			target.add(this.target);
			return target;
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
		if (Behavior.CHEAT)
			return Environnement.getInstance();
		else
			return this.e;
	}
	
	public Vector2 getBaseAttackPosition(Integer brainId) throws NotExistException {
		try {
			Vector2 base = getEnvironnement().getPositionFirstEnemyBase();
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size();
			float tick = (360/nbrPersonnes);
			float alpha = tick*index + getEnvironnement().getIndexOfTeam(this) * 20;
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
			Vector2 position = new Vector2(0, 0);
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size();
			float tick = (360/nbrPersonnes);
			float alpha = tick*index + angle;
			Vector2 target = Tools.cartFromPolaire(alpha, 20);
			target.add(position);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public void setTarget(Vector2 target, boolean isTargetBase) {
		this.target = target;
		this.isTargetBase = isTargetBase;
	}

	public Vector2 getTarget() {
		return this.target;
	}

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
}
