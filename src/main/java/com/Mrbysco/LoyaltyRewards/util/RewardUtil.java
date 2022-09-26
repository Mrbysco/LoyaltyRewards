package com.mrbysco.loyaltyrewards.util;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.config.LoyaltyConfig.EnumAnnounceMethod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class RewardUtil {

	public static void sendRewardMessage(Player player, int totalSeconds) {
		MutableComponent text = Component.translatable("loyaltyrewards.rewarded.message").withStyle(LoyaltyConfig.SERVER.messageColor.get())
				.append(secondsToString(totalSeconds));

		if (LoyaltyConfig.SERVER.announceMethod.get() == EnumAnnounceMethod.STATUS) {
			player.displayClientMessage(text, true);
		} else {
			MutableComponent chatComponent = Component.literal("[LoyaltyRewards] ").append(text);
			player.sendSystemMessage(chatComponent);
		}
	}

	private static MutableComponent secondsToString(int totalSeconds) {
		//Calculate the seconds to display:
		int seconds = totalSeconds % 60;
		totalSeconds -= seconds;
		//Calculate the minutes:
		long minutesCount = totalSeconds / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;
		//Calculate the hours:
		long hours = minutesCount / 60;
		MutableComponent hourComponent = Component.literal(String.format("\n %02d", hours)).withStyle(ChatFormatting.YELLOW);
		MutableComponent hourExtra = Component.literal("H").withStyle(ChatFormatting.GOLD);
		MutableComponent minuteComponent = Component.literal(String.format(":%02d", minutes)).withStyle(ChatFormatting.YELLOW);
		MutableComponent minuteExtra = Component.literal("M").withStyle(ChatFormatting.GOLD);
		MutableComponent secondComponent = Component.literal(String.format(":%02d", seconds)).withStyle(ChatFormatting.YELLOW);
		MutableComponent secondExtra = Component.literal("S").withStyle(ChatFormatting.GOLD);
		return hourComponent.append(hourExtra).append(minuteComponent).append(minuteExtra).append(secondComponent).append(secondExtra);
	}
}
