package com.mrbysco.loyaltyrewards.handler;

import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Collection;

public class LoyaltyHandler {

	@SubscribeEvent
	public void serverTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (player instanceof ServerPlayer serverPlayer && level.getGameTime() % 20 == 0) {
			Collection<RecipeHolder<RewardRecipe>> rewards = serverPlayer.serverLevel().recipeAccess().recipeMap().byType(ModRegistry.REWARD_RECIPE_TYPE.get());
			for (RecipeHolder<RewardRecipe> rewardHolder : rewards) {
				String infoTimerTag = rewardHolder.id().toString();
				if (hasTag(serverPlayer, infoTimerTag)) {
					RewardRecipe reward = rewardHolder.value();
					int timer = getTime(serverPlayer, infoTimerTag);

					if (timer == -1) {
						if (reward.isRepeatable()) {
							setTime(serverPlayer, infoTimerTag, 2);
						}
					} else {
						if (timer >= reward.getTime()) {
							reward.triggerReward(level, serverPlayer.blockPosition(), serverPlayer);
							setTime(serverPlayer, infoTimerTag, -1);
						} else {
							int newTime = timer;
							newTime++;
							setTime(serverPlayer, infoTimerTag, newTime);
						}
					}
				} else {
					setTime(serverPlayer, infoTimerTag, 1);
				}
			}
		}
	}

	public static void setTime(Player player, String valueTag, int time) {
		CompoundTag playerData = player.getPersistentData();
		CompoundTag data = getTag(playerData, Player.PERSISTED_NBT_TAG);

		data.putInt(valueTag, time);
		playerData.put(Player.PERSISTED_NBT_TAG, data);
	}

	public static int getTime(Player player, String valueTag) {
		CompoundTag playerData = player.getPersistentData();
		CompoundTag data = getTag(playerData, Player.PERSISTED_NBT_TAG);
		return data.getIntOr(valueTag, 0);
	}

	public static boolean hasTag(Player player, String valueTag) {
		CompoundTag playerData = player.getPersistentData();
		CompoundTag data = getTag(playerData, Player.PERSISTED_NBT_TAG);
		return data.contains(valueTag);
	}

	public static CompoundTag getTag(CompoundTag tag, String key) {
		if (tag == null || !tag.contains(key)) {
			return new CompoundTag();
		}
		return tag.getCompoundOrEmpty(key);
	}
}
