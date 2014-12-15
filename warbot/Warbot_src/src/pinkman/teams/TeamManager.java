package pinkman.teams;

import java.util.ArrayList;

import pinkman.environement.Environnement;
import pinkman.environement.StructWarBrain;
import pinkman.exceptions.BaseNotFoundException;
import pinkman.exceptions.NoTargetException;
import pinkman.exceptions.NoTeamFoundException;

import com.badlogic.gdx.math.Vector2;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;



public class TeamManager {

	private ArrayList<Group> teams;
	private Environnement e;

	public TeamManager(Environnement e) {
		teams = new ArrayList<Group>();
		this.e = e;
	}

	public Group getTeamOf(int id) throws NoTeamFoundException {
		for (Group team : teams) {
			if(team.contains(id)) {
				return team;
			}
		}
		throw new NoTeamFoundException();
	}

	public int size() {
		return teams.size();
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
		Group team = new Group(e);
		team.addMember(brainId);
		teams.add(team);
		team.setTeamIndex(teams.indexOf(team));
		return team;
	}

	public void remove(Integer id) {
		for (Group team : teams) {
			team.removeMember(id);
		}
	}

	public void setBaseAttacked(boolean b) {
		for (Group group : teams) {
			group.setBaseAttacked(b);
		}
	}

	public int getIndexOfTeam(Group t) {
		return teams.indexOf(t);
	}
	
	public boolean isAlreadyTargeted(int brainId) {
		for (Group team : this.teams) {
			try {
				if (team.getTarget().brainID == brainId)
					return true;
			} catch (NoTargetException e) {
			}
		}
		return false;
	}
	

	public void assignTarget() {
		for (Group team : teams) {
			if(team.getSize() > 0) {
				if(e.inRush()) {
					try {
						team.setTarget(new Target(e.getFirstEnemyBase()));
					} catch (BaseNotFoundException e) {
					}
				} else {
					
					try {
						Target t = team.getTarget();
						
						// Il y a déjà une target
						
						// Reset si base (elle sera redéfinie plus tard)
						if (e.getEnemy(t.brainID).getType() == WarAgentType.WarBase) {
							team.setTarget(null);
						}
						
						// On la désactive si elle est devenue trop loin
						Vector2 base = e.getPositionFirstEnemyBase();
						e.getEnemy(t.brainID);
						if (e.getEnemy(t.brainID).getPosition().dst(base) > 200) {
							team.setTarget(null);
						}
						
					} catch (Exception e) {
						team.setTarget(null);
					}
					
					// Pas d'énemie déjà en target
					if (!team.hasTarget()) {
						
						try {
							// On prend l'enemie le plus proche comme nouvelle target
							StructWarBrain s = e.getEnemy(e.getBestTargetFromBase(e.getPositionFirstEnemyBase()));
							team.setTarget(new Target(s.getID()));
						} catch (Exception e1) {
							
							// S'il y en a pas, on focus la base, si pas de base ça reste à null
							try {
								team.setTarget(new Target(e.getFirstEnemyBase()));
							} catch (BaseNotFoundException e) {
							}
						}
					}
				}
			}
		}
	}
}
