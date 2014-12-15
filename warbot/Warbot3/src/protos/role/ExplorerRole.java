package protos.role;

public abstract class ExplorerRole extends WarRole
{
	protected ExplorerRole(String role)
	{
		super(role);
	}

	public static final String EXPLORATOR = "EXPLORATOR";
	public static final String LEADER = "LEADER";
	public static final String COLLECTOR = "COLLECTOR";
	
	public static final String[] values()
	{
		String tab[]=new String[ExplorerRole.class.getFields().length];
		for(int i=0;i<ExplorerRole.class.getFields().length;i++)
		{
			tab[i]=ExplorerRole.class.getFields()[0].getName();
		}
		return tab;
	}

	public static ExplorerRole getExplorerRoleByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
