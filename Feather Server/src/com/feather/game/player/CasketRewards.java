package com.feather.game.player;

import com.feather.utils.Utils;

public class CasketRewards {
	//Coins 6964
	
	public static int Money = (6964);
	
	public static void giveReward(Player player) {
		int random = Utils.getRandom(999) + 1;
		if (random >= 850) {
			player.getPackets().sendGameMessage("You open the casket and find nothing inside.");
			player.getInventory().deleteItem(405, 1);
		} else if (random <= 200 && random >= 650) {
			player.getInventory().addItem(Money, Utils.random(1, 2));
			player.getInventory().deleteItem(405, 1);
			player.getPackets().sendGameMessage("You found some Old Coins.");
		} else {
			player.getInventory().addItem(Money, Utils.random(2, 5));
			player.getInventory().deleteItem(405, 1);
			player.getPackets().sendGameMessage("You found some Old Coins.");
		}
	}
}
