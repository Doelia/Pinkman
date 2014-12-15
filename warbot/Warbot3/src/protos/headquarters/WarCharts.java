package protos.headquarters;

import java.util.List;

import protos.WarEngineerBrainController.EngineerRole;
import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import protos.role.BaseRole;
import protos.role.ExplorerRole;
import protos.role.RocketLauncherRole;
import edu.turtlekit3.warbot.agents.agents.WarBase;
import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordCartesian;
import edu.turtlekit3.warbot.tools.CoordPolar;

/**
 *
 * Cette classe permet de garder des statistiques :
 * Sur l'équipe Protos,
 *  - Nombre d'agents avec leur role
 * Sur les positions,
 *  - de bases enemies
 *  - de nourritures
 *
 *	Coder pour n'�tre d�pendant seulement des messages
 * @author BEUGNON
 *
 *
 */
public class WarCharts
{
	
	private static final int numberAgent = 4;


	private int[] allyBaseCounter;

	private int[] allyRocketLauncherCounter;

	private int[] allyEngineerCounter;

	private int[] allyExplorerCounter;
	
	
	private List<CoordPolar> enemyBaseLocations;
	
	private List<CoordPolar> foodLocations;

	/**
     *
     * @param hq
     */
	public WarCharts()
	{

		allyBaseCounter = new int[BaseRole.values().length];
		allyRocketLauncherCounter = new int[RocketLauncherRole.values().length];
		allyExplorerCounter = new int[ExplorerRole.values().length];
		allyEngineerCounter = new int[EngineerRole.values().length];
		//WE IGNORE TURRET

	}

    /**

     * @return le nombre de bases
     */
	public int countBases()
	{
		int c=0;
		for(int i=0;i<BaseRole.values().length;i++)
			c+=allyBaseCounter[i];
		return c;
	}
    /**
     * Compte le nombre de bases avec un rôle dans la partie
     * @param role Le role à dénombrer
     * @return le nombre de bases avec le rôle
     */
	public int countBasesWithRole(BaseRole role)
	{
		int i=0;
		while(i < BaseRole.values().length && !BaseRole.values()[i].equals(role))
			i++;
		return allyBaseCounter[i];
	}

    /**
     * Incrémente le nombre de bases avec un rôle dans la partie
     * @param role Le role à incrémenter
     */
	public void increaseBasesWithRole(BaseRole role)
	{
		int i=0;
		while(i < BaseRole.values().length && !BaseRole.values()[i].equals(role))
			i++;
		allyBaseCounter[i]++;
	}

    /**
     * Décremente le compteur de bases avec un rôle dans la partie
     * @param role Le role à décrémenter
     */
	public void decreaseBasesWithRole(BaseRole role)
	{
		int i=0;
		while(i < BaseRole.values().length && !BaseRole.values()[i].equals(role))
			i++;
		allyBaseCounter[i]--;
	}

    /**
     *
     * @return le nombre de lanceurs de missiles
     */
	public int countRocketLaunchers()
	{
		int c=0;
		for(int i=0;i<RocketLauncherRole.values().length;i++)
			c+=allyRocketLauncherCounter[i];
		return c;
	}
    /**
     * Compte le nombre d'explorateurs avec un rôle dans la partie
     * @param role Le role à dénombrer
     * @return le nombre d'explorateurs avec le rôle
     */
	public int countRocketLaunchersWithRole(RocketLauncherRole role)
	{
		int i=0;
		while(i < RocketLauncherRole.values().length && !RocketLauncherRole.values()[i].equals(role))
			i++;
		return allyRocketLauncherCounter[i];
	}

    /**
     * Incremente le compteur de lanceur de missile avec un rôle particulier
     * @param role Le rôle à incrémenter
     */
	public void increaseRocketLaunchersWithRole(RocketLauncherRole role)
	{
		int i=0;
		while(i < RocketLauncherRole.values().length && !RocketLauncherRole.values()[i].equals(role))
			i++;
		allyRocketLauncherCounter[i]++;
	}
    /**
     * Décremente le compteur de lanceur de missile
     * @param role Le rôle à décrémenter
     */
	public void decreaseRocketLaunchersWithRole(RocketLauncherRole role)
	{
		int i=0;
		while(i < RocketLauncherRole.values().length && !RocketLauncherRole.values()[i].equals(role))
			i++;
		allyRocketLauncherCounter[i]--;
	}

    /**
     *
     * @return le nombre d'explorateurs
     */
	public int countExplorers()
	{
		int c=0;
		for(int i=0;i<ExplorerRole.values().length;i++)
			c+=allyExplorerCounter[i];
		return c;
	}

    /**
     * Compte le nombre d'explorateurs avec un rôle dans la partie
     * @param role Le rôle à dénombrer
     * @return le nombre d'explorateurs avec le role
     */
	public int countExplorersWithRole(ExplorerRole role)
	{
		int i=0;
		while(i < ExplorerRole.values().length && !ExplorerRole.values()[i].equals(role))
			i++;
		return allyExplorerCounter[i];
	}
	
	/**
     * Incremente le compteur de lanceur de missile avec un rôle particulier
     * @param role Le rôle à incrémenter
     */
	public void increaseExplorersWithRole(ExplorerRole role)
	{
		int i=0;
		while(i < RocketLauncherRole.values().length && !RocketLauncherRole.values()[i].equals(role))
			i++;
		allyExplorerCounter[i]++;
	}

    /**
     *
     * @return le nombre d'ingénieurs
     */
	public int countEngineers()
	{
		int c=0;
		for(int i=0;i<EngineerRole.values().length;i++)
			c+=allyEngineerCounter[i];
		return c;
	}
	
	public void increaseEngineersWithRole(EngineerRole role) {
		int i=0;
		while(i < EngineerRole.values().length && !EngineerRole.values()[i].equals(role))
			i++;
		allyEngineerCounter[i]++;
	}

    /**
     * Compte le nombre d'ingénieurs avec un role dans la partie
     * @param role Le role à dénombrer
     * @return le nombre d'ingénieur avec le role
     */
	public int countEngineersWithRole(EngineerRole role)
	{
		int i=0;
		while(i < EngineerRole.values().length && !EngineerRole.values()[i].equals(role))
			i++;
		return allyEngineerCounter[3];
	}
	
	

    public boolean haveMoreWarExplorersWithRoleThan(ExplorerRole role,int number)
    {
        return  number<=countExplorersWithRole(role);
    }
    
    public boolean haveMoreWarRocketLaunchersWithRoleThan(RocketLauncherRole role,int number)
    {
        return  number<=countRocketLaunchersWithRole(role);
    }
    
    public boolean haveMoreEngineersWithRoleThan(EngineerRole role,int number)
    {
        return  number<=countEngineersWithRole(role);
    }


    public void handleStats(List<WarMessage> list)
    {
    	for(WarMessage wm : list)
    	{
    		if(wm.equals(ProtosCommunication.PING))
    		{
    			if(wm.getSenderType().equals(WarAgentType.WarBase))
    			{
    				BaseRole role = BaseRole.getBaseRoleByName(wm.getContent()[0]);
    				increaseBasesWithRole(role);
    			}
    			else if(wm.getSenderType().equals(WarAgentType.WarEngineer))
    			{
    				EngineerRole role = EngineerRole.getBaseRoleByName(wm.getContent()[0]);
    				increaseEngineersWithRole(role);
    			}
    			else if(wm.getSenderType().equals(WarAgentType.WarRocketLauncher))
    			{
    				 RocketLauncherRole role = RocketLauncherRole.getRocketLauncherRoleByName(wm.getContent()[0]);
    				increaseRocketLaunchersWithRole(role);
    			}
                else if(wm.getSenderType().equals(WarAgentType.WarExplorer))
                {
                    ExplorerRole role = ExplorerRole.getExplorerRoleByName(wm.getContent()[0]);
                }
    			//WE DON'T COUNT TURRETS AND KAMIKAZE
    			//BECAUSE TURRETS REACTIVE AGENTS AND KAMIKAZE GO DIE ON ENEMY
    		}
    	}
    }

	

	public boolean haveEnemyBaseTargets() {
		return !this.getEnemyBaseTargets().isEmpty();
	}
	
	public void addEnemyBaseLocation(TargetMessageWrapper tmw)
	{
		
	}
	
	public void addDestroyedEnemyBaseLocation(TargetMessageWrapper tmw)
	{
		
	}

	public List<CoordPolar> getEnemyBaseTargets() {
		return this.enemyBaseLocations;
	}

	public void handleInformationTargets(List<WarMessage> messages) 
	{
		for(WarMessage wm : messages)
    	{
    		if(wm.equals(ProtosCommunication.INFORM_ENEMY_BASE))
    		{
    			//TODO ADD TARGET INFORMATION HANDLER
    		}
    		else if(wm.equals(ProtosCommunication.INFORM_FOUND_FOOD))
    		{
    			//TODO ADD FOOD INFORMATION HANDLER 
    		}
    		else if(wm.equals(ProtosCommunication.INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION))
    		{
    			//TODO REMOVE FOOD INFORMATION HANDLER
    		}
    		else if(wm.equals(ProtosCommunication.INFORM_DESTROYED_ENEMY_BASE))
    		{
    			//TODO REMOVE TARGET INFORMATION HANDLER
    		}
    	}
	}
	
	
	public void addEnemyBaseLocatisation(WarMessage wm)
	{
		
		TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
		CoordPolar cp = tmw.compute();
		CoordCartesian up = new CoordPolar(cp.getDistance(),cp.getAngle()).toCartesian();
		up.add(new CoordPolar(WarBase.HITBOX_RADIUS,270).toCartesian());
		
		
		CoordCartesian down =  new CoordPolar(cp.getDistance(),cp.getAngle()).toCartesian();
		down.add(new CoordPolar(WarBase.HITBOX_RADIUS,90).toCartesian());
		
		CoordCartesian left =  new CoordPolar(cp.getDistance(),cp.getAngle()).toCartesian();
		left.add(new CoordPolar(WarBase.HITBOX_RADIUS,180).toCartesian());
		

		CoordCartesian right =  new CoordPolar(cp.getDistance(),cp.getAngle()).toCartesian();
		right.add(new CoordPolar(WarBase.HITBOX_RADIUS,180).toCartesian());
		
		double maxDist,minDist,maxAngle,minAngle;
		
		if( cp.getAngle()  <= 270 && 90 <= cp.getAngle())
		{//left part of the world
			maxDist = left.toPolar().getDistance();
			minDist = right.toPolar().getDistance();

			maxAngle = up.toPolar().getAngle();
			minAngle = down.toPolar().getAngle();
			
		}
		else
		{
			//right part of the world
			maxDist = right.toPolar().getDistance();
			minDist = left.toPolar().getDistance();
	
			maxAngle = down.toPolar().getAngle();
			minAngle = up.toPolar().getAngle();
			
		}
		
		
		if((minDist <= cp.getDistance() && cp.getDistance() <= maxDist)
				&& (minAngle <= cp.getAngle() && cp.getAngle() <= maxAngle))
		{
			if(!this.enemyBaseLocations.contains(cp))
			this.enemyBaseLocations.add(cp)
		}
	}
	

	public boolean haveFoodTargets() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
}
