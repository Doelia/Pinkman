package protos.communication;

/**
 * Contains all messages headers for communication between all agents and HQ
 * 
 * @author beugnon
 *
 */
public class ProtosCommunication
{
	public static final String ASK_HQ_ID = "ASK_HQ_ID";
	
	public static final String ASK_HQ_POSITION = "ASK_HQ_POSITION";
	
	public static final String INFORM_HQ_POSITION = "INFORM_HQ_POSITION";
	
	/**
	 * From <b>base</b> to <b>All agents</b> : to declare the Main HeadQuarter
	 * for bases
	 */
	public static final String INFORM_HQ_DECLARED = "INFORM_HQ_DECLARED";
	
	public static final String INFORM_SAFE_BRANCH = "INFORM_SAFE_BRANCH";
	
	public static final String INFORM_ENEMY_BASE = "INFORM_ENEMY_BASE";
	
	public static final String INFORM_DESTROYED_ENEMY_BASE = "INFORM_DESTROY_ENEMY_BASE";
	
	/**
	 * From <b>HQ</b> to <b>All agents</b> :</br> Send a contract to some agents
	 */
	public static String INFORM_NEW_CONTRACT = "INFORM_NEW_CONTRACT";
	
	public static String INFORM_TAKE_CONTRACT = "INFORM_TAKE_CONTRACT";
	
	public static String REFUSE_CONTRACTOR = "REFUSE_CONTRACTOR";
	
	public static String ACCEPT_CONTRACTOR = "ACCEPT_CONTRACTOR";
	
	/**
	 * From <b>All agents</b> to <b>HQ</b> :</br> To compute battle's needs
	 */
	public static String INFORM_MY_TYPE = "INFORM_MY_TYPE";
	
	/**
	 * From <b>HQ</b> to <b>All Agents</b> :</br> Send a request to know who do
	 * nothing
	 */
	public static final String ASK_WITHOUT_ORDER = "ASK_WITHOUT_ORDER";
	public static final String INFORM_WITHOUT_ORDER = "INFORM_WITHOUT_ORDER";
	/**
	 * From <b>HQ</b> to <b>All agents</b> :</br> Send a role to an agent to
	 * obtain a life's purpose
	 */
	public static final String INFORM_GIVE_ROLE = "INFORM_GIVE_ROLE";
	public static final String INFORM_WITHOUT_ROLE = "INFORM_WITHOUT_ROLE";
	
	/**
	 * From <b>All agents</b> to <b>All agents</b> :</br> Send a message to
	 * signal spotted foods
	 */
	public static final String INFORM_FOUND_FOOD = "INFORM_FOUND_FOOD";
	
	/**
	 * From <b>All agents</b> to <b>All agents</b> :</br> Send a message to
	 * signal spotted enemies
	 */
	public static final String INFORM_FOUND_ENEMY = "INFORM_FOUND_ENEMY";
	
	/**
	 * From <b>All agents</b> to <b>All agents</b> :</br> Send a message to
	 * signal spotted base
	 */
	public static final String INFORM_FOUND_ENEMY_BASE = "FOUND_ENEMY_BASE";
	
	/**
	 * From <b>All agents</b> to <b>HQ</b> :</br> 
	 * Send a message to signal the agent don't found at the location given by HQ
	 */
	public static final String INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION = "INFORM_NO_MORE_FOOD_AT_GIVEN_POSITION";

	public static final String REQUEST_DO_CONTRACT = "REQUEST_DO_CONTRACT";

	public static final String REQUEST_ATTACK = "REQUEST_ATTACK";

	public static final String ASK_ENEMY_BASE_POSITION = "ASK_ENEMY_BASE";

	public static final String INFORM_NO_MORE_FOUND_HERE = "INFORM_NO_MORE_FOUND_HERE";
	
	public static final String PING = "PING";

	public static final String ASK_FOR_FOOD = "ASK_FOR_FOOD";

	public static final String REQUEST_FOOD_IMMEDIATLY = "REQUEST_FOOD_IMMEDIATLY";

	
	
}
