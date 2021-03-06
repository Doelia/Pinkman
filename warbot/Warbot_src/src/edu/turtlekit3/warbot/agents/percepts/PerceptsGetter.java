package edu.turtlekit3.warbot.agents.percepts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.game.Game;

public abstract class PerceptsGetter {

	private ControllableWarAgent _agent;

	boolean _arePerceptsAlreadyCatchedThisTick;

	private ArrayList<WarPercept> _enemies;
	private ArrayList<WarPercept> _allies;
	private ArrayList<WarPercept> _resources;

	public PerceptsGetter(ControllableWarAgent agent) {
		_agent = agent;
	}

	protected ControllableWarAgent getAgent() {
		return _agent;
	}

	public abstract ArrayList<WarPercept> getPercepts();

	public ArrayList<WarPercept> getAgentsPercepts(boolean ally) {
		if(! _arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		if(ally)
			return this._allies;
		else
			return this._enemies;
	}

	public ArrayList<WarPercept> getResourcesPercepts() {
		if(! _arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		return this._resources;
	}

	public ArrayList<WarPercept> getPerceptsByType(WarAgentType agentType, boolean ally){
		if(!this._arePerceptsAlreadyCatchedThisTick)
			this.getAllPerceptsAndSortThem();

		ArrayList<WarPercept> perceptRes = new ArrayList<>();
		ArrayList<WarPercept> listePerceptToParcours;

		if(ally)
			listePerceptToParcours = this._allies;
		else
			listePerceptToParcours = this._enemies;

		for (WarPercept warPercept : listePerceptToParcours) {
			if(warPercept.getType().equals(agentType)){
				perceptRes.add(warPercept);
			}
		}

		return perceptRes;
	}

	private void getAllPerceptsAndSortThem() {
		this._allies = new ArrayList<>();
		this._enemies = new ArrayList<>();
		this._resources = new ArrayList<>();

		for (WarPercept perceptCourant : this.getPercepts()) {
			if(perceptCourant.getTeamName().equals(Game.getInstance().getMotherNatureTeam().getName()))
				this._resources.add(perceptCourant);
			else if(perceptCourant.getTeamName().equals(_agent.getTeam().getName()))
				this._allies.add(perceptCourant);
			else
				this._enemies.add(perceptCourant);
		}

		//Tri les percepts
		Collections.sort(((List<WarPercept>)this._enemies));
		Collections.sort(((List<WarPercept>)this._allies));
		Collections.sort(((List<WarPercept>)this._resources));
		
		this._arePerceptsAlreadyCatchedThisTick = true;
	}

	public void setPerceptsAlreadyInit(boolean b){
		this._arePerceptsAlreadyCatchedThisTick = b;
	}

}