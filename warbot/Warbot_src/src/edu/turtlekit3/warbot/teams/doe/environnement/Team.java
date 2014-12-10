package edu.turtlekit3.warbot.teams.doe.environnement;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Team {
	private ArrayList<Integer> members;

	public Team() {
		members = new ArrayList<Integer>();
	}

	public void addMember(Integer w) {
		members.add(w);
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

	public Vector2 getMovementPosition(Integer brainId) throws NotExistException {
		int index = members.indexOf(brainId);
		Vector2 position = new Vector2(Environnement.getInstance().getStructWarBrain(getLeader()).getPosition());
		
		float alpha = (float) (2 * Math.PI / (float) getSize()) * index;
		Vector2 target = Environnement.cartFromPolaire(alpha, 30);
		System.out.println(target);
		
		return position.add(target);
	}
}
