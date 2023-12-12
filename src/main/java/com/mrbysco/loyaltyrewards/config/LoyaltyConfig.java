package com.mrbysco.loyaltyrewards.config;

import com.mrbysco.loyaltyrewards.LoyaltyRewards;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class LoyaltyConfig {
	public enum EnumAnnounceMethod {
		CHAT,
		STATUS
	}

	public static class Common {
		public final ModConfigSpec.EnumValue<EnumAnnounceMethod> announceMethod;
		public final ModConfigSpec.EnumValue<ChatFormatting> messageColor;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("Server settings")
					.push("Server");

			announceMethod = builder
					.comment("Changing this value will change how a player will know they've been rewarded [Default: CHAT]")
					.defineEnum("announceMethod", EnumAnnounceMethod.CHAT);

			messageColor = builder
					.comment("Changing this value will change the color of the message the player receives when being rewarded for playing [Default: YELLOW].")
					.defineEnum("messageColor", ChatFormatting.YELLOW);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		LoyaltyRewards.LOGGER.debug("Loaded Loyalty Rewards config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		LoyaltyRewards.LOGGER.debug("Loyalty Rewards config just got changed on the file system!");
	}
}
