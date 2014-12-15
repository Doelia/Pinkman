package edu.turtlekit3.warbot.teams.protos.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;

public class Contract {

	
	static int num=0;
	
	Map<WarAgentType,Integer> needs;
	
	int numContract;
	
	String taskName;
	
	int priority;
	// 0 lower
	// 1 normal 
	// 2 high
	// 3 forceful
	
	List<String> contractParams;
	
	public Contract()
	{
		numContract=(++num);
		needs=new HashMap<>();
		contractParams=new ArrayList<>();
	}
	
	public Contract(int id) {
		numContract=id;
	}

	public void need(WarAgentType type,int value)
	{
		needs.put(type, value);
	}
	
	public void setLevel(int i)
	{
		if(i<0)
			priority=0;
		else if(i>3)
			priority=3;
		else
			priority=i;
	}
	
	
	public boolean isReady()
	{
		return this.taskName!=null && needs.size()!=0;
	}

	public void addContractArgument(String string) {
		this.contractParams.add(string);
	}

	public void setTaskName(String string) {
		this.taskName=string;
	}

	public String getId() {
		return this.getId();
	}

	public int needSize() {
		return this.needs.size();
	}
	
	public Map<WarAgentType,Integer> getNeeds()
	{
		return needs;
	}

	public String getTaskName() {
		return this.taskName;
	}
	
	public int getLevel()
	{
		return this.priority;
	}

	public int argSize() {
		
		return this.contractParams.size();
	}
	
	public List<String> getParams()
	{
		return this.contractParams;
	}
	
	
}
