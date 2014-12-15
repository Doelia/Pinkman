package edu.turtlekit3.warbot.teams.protos.communication.messages;

import edu.turtlekit3.warbot.agents.enums.WarAgentType;
import edu.turtlekit3.warbot.brains.WarBrain;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.teams.protos.communication.ProtosCommunication;
import edu.turtlekit3.warbot.teams.protos.contract.Contract;

public class ContractWrapper {

	private String message;
	private Contract contract;

	private int ID;

	private ContractWrapper() {

	}

	public static ContractWrapper unwrap(WarMessage wm) {

		ContractWrapper tmw = new ContractWrapper();
		tmw.setID(wm.getSenderID());
		tmw.setMessage(wm.getMessage());

		int id = Integer.parseInt(wm.getContent()[0]);

		tmw.contract = new Contract(id);
		tmw.contract.setTaskName(wm.getContent()[1]);
		tmw.contract.setLevel(Integer.parseInt(wm.getContent()[2]));
		int i = 3;
		try {
			while (i < wm.getContent().length
					&& WarAgentType.valueOf(wm.getContent()[i].split(";")[0]) != null) {
				WarAgentType type = WarAgentType.valueOf(wm.getContent()[i]
						.split(";")[0]);
				int number = Integer.parseInt(wm.getContent()[i].split(";")[1]);
				tmw.contract.need(type, number);
				i++;
			}
		} catch (IllegalArgumentException e) 
		{
			// Quand on ne tombe pas sur un mot WarAgentType
			// Ceci soulève une exception WarAgentType.valueOf()
			// Dans ce cas on ne lira plus aucun besoin de WarAgentType
		}

		if (i < wm.getContent().length) {
			for (; i < wm.getContent().length; i++) {
				tmw.contract.addContractArgument(wm.getContent()[i]);
			}
		}

		return tmw;
	}

	private void setID(int senderID) {
		this.ID = senderID;
	}

	public int getID() {
		return this.ID;
	}

	public static void wrapAndSendMessageToAll(Contract contract, WarBrain wb) {
		String[] master = new String[3 + contract.needSize()
				+ contract.argSize()];
		int i = 0;
		master[i++] = "" + contract.getId();
		// Taskname
		master[i++] = "" + contract.getLevel();

		for (WarAgentType t : contract.getNeeds().keySet()) {

			master[i++] = t.toString().concat(
					"" + contract.getNeeds().get(t).intValue());

		}
		for (WarAgentType t : contract.getNeeds().keySet()) {
			wb.broadcastMessageToAgentType(t,
					ProtosCommunication.INFORM_NEW_CONTRACT, master);
		}
	}

	
	public static void wrapAndSendMessageToID(Contract contract, WarBrain wb,int ID) {
		String[] master = new String[3 + contract.needSize()
				+ contract.argSize()];
		int i = 0;
		master[i++] = "" + contract.getId();
		// Taskname
		master[i++] = "" + contract.getLevel();

		for (WarAgentType t : contract.getNeeds().keySet()) {

			master[i++] = t.toString().concat(
					"" + contract.getNeeds().get(t).intValue());

		}
		wb.sendMessage(ID,
					ProtosCommunication.REQUEST_DO_CONTRACT, master);
		
	}
	
	private void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void accept(WarBrain wb) {
		wb.sendMessage(getID(), ProtosCommunication.INFORM_TAKE_CONTRACT,
				contract.getId());
	}

	public static void main(String[] args) {
		
	}
}
