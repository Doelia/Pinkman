package edu.turtlekit3.warbot.teams.doe.cheat;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.teams.doe.Tools;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

public class Team {
	private ArrayList<Integer> members;
	private Vector2 target;
	private boolean attacking;
	private int requestNumber;
	private int battleModifier;
	private int nbStopAttacking;

	public Team() {
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
		if(requestNumber > 20 * getSize()) {
			System.out.println("rotating");
			requestNumber = 0;
			battleModifier++;
			battleModifier = battleModifier % getMaxSize();
		}
		try {
			Vector2 position = new Vector2(Environnement.getInstance().getStructWarBrain(getLeader()).getPosition());
			int index = 1 + (members.indexOf(brainId) + battleModifier) % (getMaxSize() - 1);
//			int index= members.indexOf(brainId);
			System.out.println(index);
			int nbrPersonnes = members.size() - 1;
			float tick = 180/nbrPersonnes;
			float alpha = tick*index;
			Vector2 target = Tools.cartFromPolaire(alpha, 10 + new Random().nextInt(40));
			target.add(position);
			return target;
		} catch (Exception e) {
			throw new NotExistException();
		}
	}

	public Vector2 getMovementPosition(Integer brainId) throws NotExistException {
		try {
			Vector2 position = new Vector2(Environnement.getInstance().getStructWarBrain(getLeader()).getPosition());
			int index = members.indexOf(brainId);
			int nbrPersonnes = members.size() - 1;
			float tick = 360/nbrPersonnes;
			float alpha = tick*index;
			Vector2 target = Tools.cartFromPolaire(alpha, 20);
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
		System.out.println(nbStopAttacking);
		if(!attacking) {
			nbStopAttacking++;
		}
		if(nbStopAttacking > getSize() * 10) {
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
}
