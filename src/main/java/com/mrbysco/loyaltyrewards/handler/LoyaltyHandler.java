package com.mrbysco.loyaltyrewards.handler;

import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;

import java.util.List;

public class LoyaltyHandler {

	@SubscribeEvent
	public void serverTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START)
			return;

		Level level = event.player.level();
		if (!level.isClientSide && level.getGameTime() % 20 == 0) {
			ServerPlayer player = (ServerPlayer) event.player;
			List<RecipeHolder<RewardRecipe>> rewards = level.getRecipeManager().getAllRecipesFor(ModRegistry.REWARD_RECIPE_TYPE.get());
			for (RecipeHolder<RewardRecipe> rewardHolder : rewards) {
				String infoTimerTag = rewardHolder.id().toString();
				if (hasTag(player, infoTimerTag)) {
					RewardRecipe reward = rewardHolder.value();
					int timer = getTime(player, infoTimerTag);

					if (timer == -1) {
						if (reward.isRepeatable()) {
							setTime(player, infoTimerTag, 2);
						}
					} else {
						if (timer >= reward.getTime()) {
							reward.triggerReward(level, player.blockPosition(), player);
							setTime(player, infoTimerTag, -1);
						} else {
							int newTime = timer;
							newTime++;
							setTime(player, infoTimerTag, newTime);
						}
					}
				} else {
					setTime(player, infoTimerTag, 1);
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
		return data.getInt(valueTag);
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
		return tag.getCompound(key);
	}
}
