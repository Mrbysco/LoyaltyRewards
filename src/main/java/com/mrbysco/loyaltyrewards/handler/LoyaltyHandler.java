package com.mrbysco.loyaltyrewards.handler;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class LoyaltyHandler {

    @SubscribeEvent
    public void serverTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START)
            return;

        Level world = event.player.level;
        if(!world.isClientSide && world.getGameTime() % 20 == 0) {
            Player player = event.player;
            for (Map.Entry<String, RewardInfo> entry : RewardRegistry.INSTANCE.getInfoMap().entrySet()) {
                String infoID = entry.getKey();
                String infoTimerTag = Reference.MOD_PREFIX + infoID;
                RewardInfo info = entry.getValue();

                if(hasTag(player, infoTimerTag)) {
                    int timer = getTime(player, infoTimerTag);

                    if(timer == -1) {
                        if(info.repeatable()) {
                            setTime(player, infoTimerTag, 2);
                        }
                    } else {
                        if(timer >= info.getTime()) {
                            info.trigger(world, player.blockPosition(), player);
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
        if(tag == null || !tag.contains(key)) {
            return new CompoundTag();
        }
        return tag.getCompound(key);
    }
}
