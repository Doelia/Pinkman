package edu.turtlekit3.warbot.teams.doe.clean;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.teams.doe.cheat.Environnement;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class Group {
	private ArrayList<Integer> members;
	private Vector2 target;
	private boolean attacking;
	private int requestNumber;
	private int battleModifier;
	private int nbStopAttacking;
	private boolean leaderCanShoot;

	public Group() {
		members = new ArrayList<Integer>();
		attacking = false;
		target = new Vector2();
		requestNumber = 0;
		battleModifier = 0;
		nbStopAttacking = 0;
	}

	public void addMember(Integer w) {
		members.add(w);
	}
	
	public ArrayList<Integer> getMembers() {
		return this.members;
	}

	public int getLeader() throws NotExistException {
//		try {
//			return Environnement.getInstance().getStructWarBrain(members.get(0)).getID();
//		} catch (Exception e) {
//			throw new NotExistException();
//		}
		return members.get(0);
	}

	public boolean contains(Integer id) {
		return members.contains(id);
	}

	public int getSize() {
		return members.size();
	}

	public int getMaxSize() {
		return 12;
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

	public Vector2 getMovementPosition(Integer brainId) throws NotExistException {
		try {
			Vector2 position = new Vector2();
			if (Tools.CHEAT) {
				position = new Vector2(Environnement.getInstance().getStructWarBrain(getLeader()).getPosition());
			}
			
			int index = 1 + (members.indexOf(brainId) + battleModifier) % (getSize() - 1);
//			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size() - 1;
			float tick = (360/nbrPersonnes);
			float alpha = tick*index;
//			target.rotate(alpha);
//			Vector2 target = new Vector2(50, 0);
			Vector2 target = Tools.cartFromPolaire(alpha, (isAttacking())?15:20);
			target.add(position);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public void setTarget(Vector2 target) {
		this.target = target;
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
		if(nbStopAttacking > getSize() * 2) {
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
}
