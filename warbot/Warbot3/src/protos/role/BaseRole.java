package protos.role;

import protos.tasks.base.FactoryRoutine;
import protos.tasks.base.Command;
import protos.tasks.base.InitBase;

public abstract class BaseRole extends WarRole
{
		//Lead the armies to the win and command
		public static final String HEADQUARTERS = "HEADQUARTERS";
		
		//If HQ is fallen then take his place or be a factory
		public static final String SAFE_BRANCH = "SAFE_BRANCH";
				
		//Produce unit and wait for order of HQ
		public static final String FACTORY = "FACTORY";
		



    /**
     *
     * @param name
     * @return
     */
        public static final BaseRole getBaseRoleByName(String name)
        {
            if(HEADQUARTERS.equals(name))
                return iHEADQUARTERS;
            else if(SAFE_BRANCH.equals(name))
                return iSAFE_BRANCH;
            else if (FACTORY.equals(name))
                return iFACTORY;
            else
                return iUNKNOWN;
        }

		// EN TEST
		public static final BaseRole iHEADQUARTERS = new BaseRole(HEADQUARTERS)
		{
			@Override
			public void initRoleTasks()
			{
				addRoleTask(Command.getInstance());
			}
			
		};
		
		public static final BaseRole iSAFE_BRANCH = new BaseRole(SAFE_BRANCH)
		{
			@Override
			public void initRoleTasks()
			{
				addRoleTask(FactoryRoutine.getInstance());
				addRoleTask(Command.getInstance());
			}
			
		};
		
		public static final BaseRole iFACTORY = new BaseRole(FACTORY)
		{
			@Override
			public void initRoleTasks()
			{
				addRoleTask(FactoryRoutine.getInstance());
				//addRoleTask();
			}
			
		};
		
		public static final BaseRole iUNKNOWN = new BaseRole(UNKNOWN)
		{
			
			@Override
			protected void initRoleTasks()
			{
				addRoleTask(InitBase.getInstance());
			}
		};
		
	


		private BaseRole(String roleName)
		{
			super(roleName);
		}



		public static final String[] values()
		{
			int cp=0;
			for(int i=0;i<BaseRole.class.getFields().length;i++)
				if(BaseRole.class.getFields()[i].getType().equals(String.class))
					cp++;
			
			String tab[]=new String[cp];
			cp=0;
			for(int i=0;i<BaseRole.class.getFields().length;i++)
			{
				if(BaseRole.class.getFields()[i].getType().equals(String.class))
				{	tab[cp]=BaseRole.class.getFields()[i].getName();cp++;}
			}
			return tab;
		}



    /**
     * TODO TENTATIVE D'UTILISER LA REFLEXIVITE JAVA POUR RECUPERER LE CONTENU DES CHAMPS DE CLASS WarROle
     */
		public static final BaseRole[] valuesByObject()
		{
			int cp=0;
			for(int i=0;i<BaseRole.class.getFields().length;i++)
				if(BaseRole.class.getFields()[i].getType().equals(BaseRole.class))
					cp++;
			
			BaseRole tab[]=new BaseRole[cp];
			BaseRole r = null;
			for(int i=0;i<BaseRole.class.getFields().length;i++)
			{
				if(BaseRole.class.getFields()[i].getType().equals(WarRole.class))
					try
					{
						//NE MARCHE PAS
						tab[cp]=(BaseRole) BaseRole.class.getFields()[i].get(r);
						System.out.println("BaseRole:"+(BaseRole) BaseRole.class.getFields()[i].get(r));
						cp++;
					} catch (IllegalArgumentException | IllegalAccessException
							| SecurityException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			return tab;
		}		
		
		public static void main(String[] args)
		{
			System.out.println(values().length);
			for(int i=0;i<values().length;i++)
				System.out.println(values()[i]);
			
			//RETOURNE NULL
		System.out.println(valuesByObject()[0]);
		}

	
}
