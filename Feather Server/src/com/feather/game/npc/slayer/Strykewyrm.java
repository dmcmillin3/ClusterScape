package com.feather.game.npc.slayer;

import com.feather.game.Animation;
import com.feather.game.WorldTile;
import com.feather.game.npc.NPC;
import com.feather.game.player.Player;
import com.feather.game.tasks.WorldTask;
import com.feather.game.tasks.WorldTasksManager;
import com.feather.utils.Utils;

@SuppressWarnings("serial")
public class Strykewyrm extends NPC {

	private int stompId;

	public Strykewyrm(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
		stompId = id;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		if (getId() != stompId && !isCantInteract() && !isUnderCombat()) {
			setNextAnimation(new Animation(12796));
			setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					transformIntoNPC(9462);
					setCantInteract(false);
				}
			});
		}
	}

	@Override
	public void reset() {
		setNPC(stompId);
		super.reset();
	}

	public static void handleStomping(final Player player, final NPC npc) {
		if (npc.isCantInteract())
			return;
		if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
			if (player.getAttackedBy() != npc
					&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You are already in combat.");
				return;
			}
			if (npc.getAttackedBy() != player
					&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
				if (npc.getAttackedBy() instanceof NPC) {
					npc.setAttackedBy(player); // changes enemy to player,
					// player has priority over
					// npc on single areas
				} else {
					player.getPackets().sendGameMessage(
							"That npc is already in combat.");
					return;
				}
			}
		}
		switch (npc.getId()) {
		case 9462:
			if (player.getSkills().getLevel(18) < 93) {
				player.getPackets()
						.sendGameMessage(
								"You need at least a slayer level of 93 to fight this.");
				return;
			}
			break;
		default:
			return;
		}
		player.setNextAnimation(new Animation(4278));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.setNextAnimation(new Animation(12795));
				npc.transformIntoNPC(npc.getId() + 1);
				npc.setTarget(player);
				npc.setAttackedBy(player);
				stop();
			}

		}, 1, 2);
	}

}
