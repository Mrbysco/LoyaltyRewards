package com.Mrbysco.LoyaltyRewards.handlers;

import com.Mrbysco.LoyaltyRewards.Reference;
import com.Mrbysco.LoyaltyRewards.utils.list.RewardInfo;
import com.Mrbysco.LoyaltyRewards.utils.list.RewardList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PlayerTimeHandler {
	public static int tick;
	
	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) 
	{
		if (event.phase == Phase.END) {
			tick++;
			if (tick >= 20) {
				tick = 0;
				FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
				triggerReward(player, player.getStatFile().readStat(StatList.PLAY_ONE_MINUTE)));
		    }
		}
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
			if(tag.contains(Reference.MOD_ID))
			{
				if(!savedData.hasKey(tag) || savedData.getBoolean(tag) != oldPlayerData.getBoolean(tag))
				{
					savedData.setBoolean(tag, oldPlayerData.getBoolean(tag));
				}
			}
		}
		
		if(event.isWasDeath())
		{
			newPlayer.getEntityData().merge(savedData);
		}
	}
	
	public void triggerReward(EntityPlayerMP player, long time) 
	{
		if(!RewardList.rewardList.isEmpty())
		{
			for(RewardInfo reward: RewardList.rewardList)
	        {
	        	if(time > reward.getTime())
	        	{	
	        		System.out.println(String.valueOf(time > reward.getTime()) + " " + reward.getUniqueName());
	        		NBTTagCompound data = player.getEntityData();
	        		boolean hasReward = data.getBoolean("loyaltyrewards:reward" + reward.getUniqueName() + "Given");
	        		
	        		if(player != null && !player.world.isRemote)
	        		{
	        			ItemStack stack = reward.getReward().copy();
	        			String command = String.valueOf(reward.getCommand());

	        			if(!stack.isEmpty() && !command.isEmpty())
	    				{
	        				dropItem(player, stack);
	        				executeCommand(player, command);
	        				data.setBoolean("inventory.full" + reward.getUniqueName() + "Given", true);
	    				}
	    				else if(!stack.isEmpty() && command.isEmpty())
	    				{
	        				dropItem(player, stack);
	        				data.setBoolean("inventory.full" + reward.getUniqueName() + "Given", true);
	    				}
	    				else if(stack.isEmpty() && !command.isEmpty())
	    				{
	    					executeCommand(player, command);
	    					data.setBoolean("inventory.full" + reward.getUniqueName() + "Given", true);
	    				}
	        		}
	        	}
	        }
		}
    }
	
	public void dropItem(EntityPlayerMP player, ItemStack stack)
	{
		if (player.addItemStackToInventory(stack))
        {
            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
		else
		{
			player.sendMessage(new TextComponentTranslation(TextFormatting.YELLOW + Reference.MOD_PREFIX + "inventory.full"));
			player.dropItem(stack, false);
		}
	}
	
	public void executeCommand(EntityPlayerMP player, String command)
	{
		MinecraftServer server = player.getServer();
		String rawCommand = command;
		if(rawCommand.contains("@PLAYERPOS"))
		{
			BlockPos pos = player.getPosition();
			rawCommand= command.replace("@PLAYERPOS", pos.getX() + " " + pos.getY() + " " + pos.getZ());
		}
		else if(rawCommand.contains("@PLAYER"))
		{
			rawCommand = command.replace("@PLAYER", player.getName());
			System.out.println(rawCommand);
		}
		
		server.commandManager.executeCommand(server, rawCommand);
	}
}
