package com.mrbysco.loyaltyrewards.config;

import com.mrbysco.loyaltyrewards.Reference;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey(Reference.MOD_ID + ".config.title")
public class LoyaltyRewardConfigGen {
	@Config.Comment({"General Settings"})
	public static General general = new General();
	
	@Config.Comment({"AFK Checking Settings"})
	public static afkCheck afkCheck = new afkCheck();

	public enum EnumAnnounceMethod {
		CHAT,
		STATUS,
		TOAST
	}

	public static class General{
		@Config.Comment("Changing this value will change the color of the message the player receives when being rewarded for playing [Default: YELLOW]")
		public TextFormatting messageColor = TextFormatting.YELLOW;
		
		@Config.Comment("Changing this value will change how a play will know they've been rewarded [Default: CHAT]")
		public EnumAnnounceMethod announceMethod = EnumAnnounceMethod.CHAT;
	}
	
	public static class afkCheck{
		@Config.RequiresMcRestart
		@Config.Comment("When enabled the mod implements it's own play-time stats that don't trigger when afk.")
		public boolean antiAFK = false;
		
		@Config.Comment("How many ticks before player is considered afk.")
		public int afkTime = 4800;
	}
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
