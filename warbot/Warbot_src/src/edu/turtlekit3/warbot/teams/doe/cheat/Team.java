package edu.turtlekit3.warbot.teams.doe.cheat;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.teams.doe.Tools;
import edu.turtlekit3.warbot.teams.doe.exceptions.NotExistException;

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
		Vector2 position = new Vector2(Environnement.getInstance().getStructWarBrain(getLeader()).getPosition());
		int index = members.indexOf(brainId);
		int nbrPersonnes = 8;
		float tick = 360/nbrPersonnes;
		float alpha = tick*index;
		Vector2 target = Tools.cartFromPolaire(alpha, 30);
		target.add(position);
		return target;
	}
}
