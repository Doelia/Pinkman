package edu.turtlekit3.warbot.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;

import edu.turtlekit3.warbot.agents.ControllableWarAgent;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.agents.percepts.InRadiusPerceptsGetter;
import edu.turtlekit3.warbot.agents.percepts.PerceptsGetter;
import edu.turtlekit3.warbot.game.Team;
import edu.turtlekit3.warbot.game.WarGameMode;

public class Simulation {
	
	private static Simulation _instance;
	
	private HashMap<WarAgentType, Integer> _nbAgentOfEachType;
	private HashMap<String, Team> _availableTeams;
	private WarGameMode _gameMode;
	private Level _defaultLogLevel;
	private int _foodAppearanceRate;
	private double _maxHitboxRadius;
	private boolean _isOpenWorld;
	private Class<? extends PerceptsGetter> _perceptsGetter;
	private boolean _isEnabledEnhancedGraphism;
	private File _xmlSituationFileToLoad;

	private Simulation() {
		this._availableTeams = new HashMap<>();
		this._nbAgentOfEachType = new HashMap<>();
		
		restartParameters();
	}
	
	public static Simulation getInstance() {
		if (_instance == null) {
			_instance = new Simulation();
		}
		return _instance;
	}
	
	private void restartParameters() {
		for (WarAgentType a : WarAgentType.values()) {
			_nbAgentOfEachType.put(a, WarConfig.getNbAgentsAtStartOfType(a.toString()));
		}
		_gameMode = WarGameMode.Duel;
		_defaultLogLevel = WarConfig.getLoggerLevel();
		_foodAppearanceRate = WarConfig.getFoodAppearanceRate();
		_maxHitboxRadius = WarConfig.getMaxHitBoxRadius();
		_perceptsGetter = WarConfig.getDefaultPerception();
		_isOpenWorld = WarConfig.isOpenWorld();
		_isEnabledEnhancedGraphism = false; // TODO add it to config file
		_xmlSituationFileToLoad = null;
	}
	
	public void setNbAgentOfType(WarAgentType agent, int nombre) {
		_nbAgentOfEachType.put(agent, nombre);
	}
	
	public int getNbAgentOfType(WarAgentType agent) {
		return _nbAgentOfEachType.get(agent);
	}

	public WarGameMode getGameMode() {
		return _gameMode;
	}

	public void setGameMode(WarGameMode gameMode) {
		_gameMode = gameMode;
	}

	public int getFoodAppearanceRate() {
		return this._foodAppearanceRate;
	}
	
	public void setFoodAppearanceRate(int rate) {
		_foodAppearanceRate = rate;
	}
	
	public void addAvailableTeam(Team team) {
		_availableTeams.put(team.getName(), team);
	}
	
	public Team getTeam(String teamName) {
		return _availableTeams.get(teamName);
	}
	
	public int getNbAvailableTeams() {
		return _availableTeams.size();
	}
	
	public HashMap<String, Team> getAvailableTeams() {
		return _availableTeams;
	}
	
	public void setDefaultLogLevel(Level level) {
		_defaultLogLevel = level;
	}
	
	public Level getDefaultLogLevel() {
		return _defaultLogLevel;
	}
	
	public double getMaxHitboxRadius() {
		return _maxHitboxRadius;
	}
	
	public Class<? extends PerceptsGetter> getPerceptsGetterClass() {
		return _perceptsGetter;
	}
	
	public void setPerceptsGetterClass(Class<? extends PerceptsGetter> perceptsGetter) {
		_perceptsGetter = perceptsGetter;
	}
	
	public PerceptsGetter getPerceptsGetterNewInstance(ControllableWarAgent agent) {
		try {
			return _perceptsGetter.getConstructor(ControllableWarAgent.class).newInstance(agent);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("La classe " + _perceptsGetter.getName() + " ne peut pas �tre instanci�e. InRadiusPerceptsGetter pris � la place.");
			e.printStackTrace();
			return new InRadiusPerceptsGetter(agent);
		}
	}
	
	public boolean isOpenWorld() {
		return _isOpenWorld;
	}
	
	public void setOpenWorld(boolean bool) {
		_isOpenWorld = bool;
	}
	
	public boolean isEnabledEnhancedGraphism() {
		return _isEnabledEnhancedGraphism;
	}
	
	public void setEnabledEnhancedGraphism(boolean bool) {
		_isEnabledEnhancedGraphism = bool;
	}
	
	public File getXmlSituationFileToLoad() {
		return _xmlSituationFileToLoad;
	}
	
	public void setXmlSituationFileToLoad(File file) {
		_xmlSituationFileToLoad = file;
	}
}
