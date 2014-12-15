package protos.tasks.rocketlauncher;

import java.util.List;

import protos.WarRocketLauncherBrainController;
import protos.communication.ProtosCommunication;
import protos.role.RocketLauncherRole;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;

public class InitRocketLauncher extends Task {

	@Override
	public ResultTask execute(WarBrainController wa) {
		WarRocketLauncherBrainController cont = (WarRocketLauncherBrainController) wa;
		List<WarMessage> l = cont.getBrain().getMessages();
		// look for message

		if (cont.getWarRole().getRoleName().equals(RocketLauncherRole.UNKNOWN)) {
			for (WarMessage wm : l) {
				// cont.getBrain().get
				if (wm.getMessage()
						.equals(ProtosCommunication.INFORM_GIVE_ROLE)) {
					cont.getBrain().setHeading(wm.getAngle() + 180);
					if (wm.getContent()[0].equals(RocketLauncherRole.VANGUARD)) {
						cont.setWarRole(RocketLauncherRole.iVANGUARD);

						break;
					} else if (wm.getContent()[0]
							.equals(RocketLauncherRole.MERCENARY)) {
						cont.setWarRole(RocketLauncherRole.iMERCENARY);
						break;
					} else if (wm.getContent()[0]
							.equals(RocketLauncherRole.HUNTER)) {
						cont.setWarRole(RocketLauncherRole.iHUNTER);
						break;
					}
				}
			}
		}

		return new ResultTask(cont.getWarRole().getFirstTask(),
				WarRocketLauncher.ACTION_IDLE);
	}

	private static Task instance;

	public static Task getInstance() {
		if (instance == null) {
			synchronized (InitRocketLauncher.class) {
				if (instance == null) {
					instance = new RocketLauncherRoutine();
				}
			}
		}
		return instance;
	}

}
