package protos.ownfms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.turtlekit3.warbot.brains.WarBrainController;

public abstract class FMS<T extends WarBrainController> {

	private List<AbstractBehaviour<T>> behaviours;

	private T controller;

	private String action;

	public T getController() {
		return controller;
	}

	public List<AbstractBehaviour<T>> getWarBehaviours() {
		return this.behaviours;
	}

	public FMS(T controller) {
		this.controller = controller;
		this.behaviours = new ArrayList<AbstractBehaviour<T>>();
	}

	public final void addWarBehaviour(AbstractBehaviour<T> wb) {
		this.behaviours.add(wb);
	}

	public final String execute() {
		this.resetAction();
		
		//Reflexes Handling
		//Gestion des réflexes ou Behaviour/Comportement
		for (AbstractBehaviour<T> wb : getWarBehaviours()) {
			if (wb.isActive(getController()))
				setAction(wb.act(getController()));

			if (haveAction())
				break;
		}
		if (haveAction())
			return getAction();
		
		//Gestion personnelle et non-accessible par WarBrainController
		//ça serait bien d'avoir la possibilité de réaliser sa propre couche
		//entre WarBrainController et WarBaseBrainController par exemple
		setAction(applyCustomAgentReasoning());
		
		//On doit définir une action de base pour ne pas tout faire planter
		if(!haveAction())
			setAction(defaultAction());
		
		return getAction();
	}
	
	protected abstract String applyCustomAgentReasoning();
	
	protected abstract String defaultAction();

	private final void resetAction() {
		this.action = null;
	}

	protected final boolean haveAction() {
		return this.action == null;
	}

	protected void setAction(String action) {
		this.action = action;
	}

	protected String getAction() {
		return this.action;
	}

	public void addWarBehaviours(
			Collection<AbstractBehaviour<T>> behaviours) {
		getWarBehaviours().addAll(behaviours);
	}

}
