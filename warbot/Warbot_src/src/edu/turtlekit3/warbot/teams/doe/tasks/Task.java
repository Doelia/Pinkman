package edu.turtlekit3.warbot.teams.doe.tasks;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.teams.doe.environement.Environnement;

public abstract class Task {

	protected WarBrain brain;
	protected WarAgentType type;
	protected Environnement e;
	
	public Task(WarBrainController brain, WarAgentType type, Environnement e){
		this.brain = brain.getBrain();
		this.type = type;
		this.e = e;
	}
	
	public abstract boolean exec();
	

	
}
