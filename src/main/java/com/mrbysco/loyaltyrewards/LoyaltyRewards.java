package com.mrbysco.loyaltyrewards;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.handler.LoyaltyHandler;
import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LoyaltyRewards.MOD_ID)
public class LoyaltyRewards {
	public static final String MOD_ID = "loyaltyrewards";
	public static final Logger LOGGER = LogManager.getLogger(LoyaltyRewards.MOD_ID);

	public LoyaltyRewards(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.SERVER, LoyaltyConfig.serverSpec);
		eventBus.register(LoyaltyConfig.class);

		ModRegistry.RECIPE_TYPES.register(eventBus);
		ModRegistry.RECIPE_SERIALIZERS.register(eventBus);

		NeoForge.EVENT_BUS.register(new LoyaltyHandler());

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}
}
