package com.mrbysco.loyaltyrewards.handlers;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class LoyaltyHandlers {

	@SubscribeEvent
	public void serverTick(TickEvent.PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.START)
			return;

		World world = event.player.world;
		if(!world.isRemote && world.getWorldTime() % 20 == 0) {
			EntityPlayer player = event.player;

			for (Map.Entry<String, com.mrbysco.loyaltyrewards.registry.RewardInfo> entry : RewardRegistry.INSTANCE.getInfoMap().entrySet()) {
				String infoID = entry.getKey();
				String infoTimerTag = Reference.MOD_PREFIX + infoID;
				com.mrbysco.loyaltyrewards.registry.RewardInfo info = entry.getValue();

				if(hasTag(player, infoTimerTag) && !player.getEntityData().getBoolean(AntiAfkHandler.AFK_TAG)) {
					int timer = getTime(player, infoTimerTag);

					if(timer == -1) {
						if(info.repeatable()) {
							setTime(player, infoTimerTag, 2);
						}
					} else {
						if(timer >= info.getTime()) {
							info.trigger(world, player.getPosition(), player);
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

	public static void setTime(EntityPlayer player, String valueTag, int time) {
		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);

		data.setInteger(valueTag, time);
		playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
	}

	public static int getTime(EntityPlayer player, String valueTag) {
		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);
		return data.getInteger(valueTag);
	}

	public static boolean hasTag(EntityPlayer player, String valueTag) {
		NBTTagCompound playerData = player.getEntityData();
		NBTTagCompound data = getTag(playerData, EntityPlayer.PERSISTED_NBT_TAG);
		return data.hasKey(valueTag);
	}

	public static NBTTagCompound getTag(NBTTagCompound tag, String key) {
		if(tag == null || !tag.hasKey(key)) {
			return new NBTTagCompound();
		}
		return tag.getCompoundTag(key);
	}
	
	@SubscribeEvent
	public void keepDataOnDeath(PlayerEvent.Clone event) 
	{
		EntityPlayer oldPlayer = event.getOriginal();
		EntityPlayer newPlayer = event.getEntityPlayer();
		
		NBTTagCompound oldPlayerData = oldPlayer.getEntityData();
		
		NBTTagCompound savedData = new NBTTagCompound();
		
		for(String tag : oldPlayerData.getKeySet())
		{
			if(tag.contains(Reference.MOD_ID) && !tag.contains("isAFK"))
			{
				if(!savedData.hasKey(tag) || savedData.getBoolean(tag) != oldPlayerData.getBoolean(tag))
				{
					savedData.setBoolean(tag, oldPlayerData.getBoolean(tag));
				}
			}
		}
		
		newPlayer.getEntityData().merge(savedData);
	}
}
