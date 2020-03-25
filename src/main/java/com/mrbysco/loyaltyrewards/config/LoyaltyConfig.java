package com.mrbysco.loyaltyrewards.config;

import com.mrbysco.loyaltyrewards.LoyaltyRewards;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class LoyaltyConfig {
    public enum EnumAnnounceMethod {
        CHAT,
        STATUS
    }

    public static class Server {
        public final EnumValue<EnumAnnounceMethod> announceMethod;
        //public final EnumValue<TextFormatting> messageColor;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server settings")
                    .push("Server");

            announceMethod = builder
                    .comment("Changing this value will change how a play will know they've been rewarded [Default: CHAT].")
                    .defineEnum("announceMethod", EnumAnnounceMethod.CHAT);

//            messageColor = builder
//                    .comment("Changing this value will change the color of the message the player receives when being rewarded for playing [Default: YELLOW].")
//                    .defineEnum("messageColor", TextFormatting.YELLOW);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec serverSpec;
    public static final LoyaltyConfig.Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(LoyaltyConfig.Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        LoyaltyRewards.LOGGER.debug("Loaded Loyalty Rewards config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        LoyaltyRewards.LOGGER.debug(" Loyalty Rewards config just got changed on the file system!");
    }
}
