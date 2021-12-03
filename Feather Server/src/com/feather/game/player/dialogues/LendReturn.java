package com.feather.game.player.dialogues;

import com.feather.game.player.LendingManager;
import com.feather.game.player.content.Lend;

public class LendReturn extends Dialogue {

	private Lend lend;

	@Override
	public void start() {
		lend = (Lend) parameters[0];
		player.getInterfaceManager().sendChatBoxInterface(1189);
		player.getPackets().sendItemOnIComponent(1189, 1, lend.getItem().getId(), 1);
		int hours = LendingManager.getHoursLeft(lend.getTime());
		int minutes = LendingManager.getMinutesLeft(lend.getTime());
		player.getPackets().sendIComponentText(1189, 4, "<col=00007f>~ Loan expires in " + (hours > 0 ? hours + " hour" + (hours > 1 ? "s" : "") : "") + " " + (minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") : "") + " ~</col><br>" + "If you give this item back, it will disappear.<br>" + "You won't be able to get it back.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == 10) {
				sendOptionsDialogue("Really give back?", "Yes, give back. I won't need it again.", "No, I'll keep hold of it.");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1)
				LendingManager.unLend(lend);
			end();
		}
	}

	@Override
	public void finish() {
	}

}
