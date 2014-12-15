package protos.role;

import protos.tasks.engineer.DoStar;

public abstract class EngineerRole extends WarRole {
	
	protected EngineerRole(String role) {
		super(role);
	}

	public static final String PROTECTOR="PROTECTOR";
	
	public static final EngineerRole iPROTECTOR = new EngineerRole(PROTECTOR) {
		
		@Override
		protected void initRoleTasks() {
			addRoleTask(DoStar.getInstance());
		}
	};
	
	public static final String COLOSSUS="COLOSSUS";
	
	
	
	public static final EngineerRole iUNKNOWN = new EngineerRole(UNKNOWN) {
		
		@Override
		protected void initRoleTasks() {
			// TODO Auto-generated method stub
			
		}
	};

	private static final EngineerRole iCOLOSSUS = new EngineerRole(COLOSSUS) {
		
		@Override
		protected void initRoleTasks() {
			//addRoleTask(instance);
		}
	};

	public static EngineerRole getEngineerRoleByName(String name) {
		switch(name)
		{
		case PROTECTOR:
			return iPROTECTOR;
		case COLOSSUS:
			return iCOLOSSUS;
		default:
			return iUNKNOWN;
		}
	}
}
