package com.mrbysco.loyaltyrewards;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.handler.LoyaltyHandler;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class LoyaltyRewards {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public LoyaltyRewards() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, LoyaltyConfig.serverSpec);
        eventBus.register(LoyaltyConfig.class);

        MinecraftForge.EVENT_BUS.register(new LoyaltyHandler());
    }

    @SubscribeEvent
    public void serverStart(FMLServerStartingEvent event) {
        RewardRegistry.initializeRewards();
    }
}
