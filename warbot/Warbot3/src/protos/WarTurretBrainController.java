package protos;

import static protos.tools.ToolsForWarAgent.*;

import java.util.List;

import edu.turtlekit3.warbot.agents.agents.WarTurret;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.brains.braincontrollers.WarTurretAbstractBrainController;

public class WarTurretBrainController extends WarTurretAbstractBrainController {
    
    public enum TurretRole {
		UNKNOWN, DEFENSER, ATTACKER;

	    public boolean isUnknown() {
			return equals(UNKNOWN);
		}
	}

	private String toReturn;
	
	private TurretRole brole;
	
	public WarTurretBrainController() {
		super();
		toReturn = null;
		brole = TurretRole.ATTACKER;
	}

	@Override
	public String action() {
		
		resetAction();
		
		choseMode();
		
		if(toReturn != null)
			return toReturn;
		
		return WarTurret.ACTION_IDLE;
	}
	
	public TurretRole getBrole() {
		return brole;
	}

	public void setBrole(TurretRole brole) {
		this.brole = brole;
	}

	public void turnAroundPlus() {
		addHeading(this, 1);
	}
	
	public void turnAroundMinus() {
		addHeading(this, -1);
	}
	
	public void choseMode() {
		
		List<WarPercept> l = getBrain().getPerceptsEnemies();
		
		if(brole == TurretRole.ATTACKER)
		{
			if(l!=null && l.size() > 0) {
				WarPercept minWap = l.get(0);
				
				for(WarPercept wp : l){
					if(wp.getType() != WarAgentType.WarRocket) {
						if(minWap.getDistance() > wp.getDistance()) {
							minWap = wp;
						}
					}
				}
			
				sameHeading(this, minWap);

				if(getBrain().isReloaded()) {
					addAction(WarTurret.ACTION_FIRE);
				}
				else {
					addAction(WarTurret.ACTION_RELOAD);
				}
			}
			else {
				addHeading(this, 1);
			}
		}
		else {
			// TODO mode défense
		}
	}
	
	public void resetAction() {
		toReturn = null;
	}
	
	public void addAction(String action) {
		toReturn = action;
	} 
}
