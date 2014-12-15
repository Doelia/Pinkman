package edu.turtlekit3.warbot.teams.protos.role;

import edu.turtlekit3.warbot.agents.agents.WarKamikaze;

/**
 * 
 * @author beugnon
 *
 */
public class WarRole {
	public static final String Unknown = "Unknown";

	public static final boolean isUnknown(String role) {
		return Unknown.equals(role);
	}

	public class ExplorerRole extends WarRole 
	{
		public static final String Explorator = "Explorator";
		public static final String Leader = "Leader";
		public static final String Collector = "Collector";
	}
}
