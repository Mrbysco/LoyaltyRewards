package com.mrbysco.loyaltyrewards;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.handler.LoyaltyHandler;
import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class LoyaltyRewards {
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	public LoyaltyRewards() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LoyaltyConfig.commonSpec);
		eventBus.register(LoyaltyConfig.class);

		ModRegistry.RECIPE_TYPES.register(eventBus);
		ModRegistry.RECIPE_SERIALIZERS.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new LoyaltyHandler());
	}
}
