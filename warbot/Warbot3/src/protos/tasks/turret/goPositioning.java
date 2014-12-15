package protos.tasks.turret;

import java.util.List;

import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.WarBrainController;

public class goPositioning extends Task{
	


	@Override
	public ResultTask execute(WarBrainController wa) {
		
		List<WarPercept> basePercepts = wa.getBrain().getPerceptsAlliesByType(WarAgentType.WarBase);
		
		if(basePercepts == null | basePercepts.size() == 0){ // si je n vois pas de base
			
		}
		else {
			
		}
		// TODO Auto-generated method stub
		return null;
	}

}
