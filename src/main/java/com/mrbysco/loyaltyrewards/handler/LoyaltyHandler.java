package com.mrbysco.loyaltyrewards.handler;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class LoyaltyHandler {

    @SubscribeEvent
    public void serverTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START)
            return;

        World world = event.player.level;
        if(!world.isClientSide && world.getGameTime() % 20 == 0) {
            PlayerEntity player = event.player;
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

    public static void setTime(PlayerEntity player, String valueTag, int time) {
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = getTag(playerData, PlayerEntity.PERSISTED_NBT_TAG);

        data.putInt(valueTag, time);
        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }

    public static int getTime(PlayerEntity player, String valueTag) {
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = getTag(playerData, PlayerEntity.PERSISTED_NBT_TAG);
        return data.getInt(valueTag);
    }

    public static boolean hasTag(PlayerEntity player, String valueTag) {
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = getTag(playerData, PlayerEntity.PERSISTED_NBT_TAG);
        return data.contains(valueTag);
    }

    public static CompoundNBT getTag(CompoundNBT tag, String key) {
        if(tag == null || !tag.contains(key)) {
            return new CompoundNBT();
        }
        return tag.getCompound(key);
    }
}
