package edu.turtlekit3.warbot.teams.doe.clean;

import java.util.ArrayList;

import edu.turtlekit3.warbot.teams.doe.exceptions.NoTeamFoundException;

public class TeamManager {
	private ArrayList<Group> teams;
	
	public TeamManager() {
		teams = new ArrayList<Group>();
	}
	
	public Group getTeamOf(int id) throws NoTeamFoundException {
		for (Group team : teams) {
			if(team.contains(id)) {
				return team;
			}
		}
		throw new NoTeamFoundException();
	}
	
	public void affectTeamTo(int brainId) {
		for (Group team : teams) {
			if(team.getSize() < team.getMaxSize()) {
				team.addMember(brainId);
				return;
			}
		}
		createTeam(brainId);
	}
	
	public Group createTeam(int brainId) {
		Group team = new Group();
		team.addMember(brainId);
		teams.add(team);
		return team;
	}

	public void remove(Integer id) {
		for (Group team : teams) {
			team.removeMember(id);
		}
	}

	public void setBaseAttacked(boolean b) {
//		for (int i = 0; i < teams.size() / 2 + 1; i++) {
//			teams.get(i).setBaseAttacked(b);
//		}
		
		for (Group group : teams) {
			group.setBaseAttacked(b);
		}
	}
}
