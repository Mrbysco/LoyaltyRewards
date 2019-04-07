package com.mrbysco.loyaltyrewards.handlers;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.config.LoyaltyRewardConfigGen;
import com.mrbysco.loyaltyrewards.packets.LoyaltyPacketHandler;
import com.mrbysco.loyaltyrewards.packets.LoyaltyToastPacket;
import com.mrbysco.loyaltyrewards.utils.TimeHelper;
import com.mrbysco.loyaltyrewards.utils.list.RewardInfo;
import com.mrbysco.loyaltyrewards.utils.list.RewardList;

import net.minecraft.client.resources.I18n;
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
				if(LoyaltyRewardConfigGen.afkCheck.antiAFK)
				{
					FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
					triggerReward(player, player.getStatFile().readStat(AntiAfkHandler.PLAY_NOT_IDLE)));
				}
				else
				{
					FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
					triggerReward(player, player.getStatFile().readStat(StatList.PLAY_ONE_MINUTE)));
				}
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
	
	public void triggerReward(EntityPlayerMP player, long time) 
	{
		if(!RewardList.rewardList.isEmpty())
		{
			for(RewardInfo reward: RewardList.rewardList)
	        {
				int ProperTime = TimeHelper.getProperTime(reward.getTime(), reward.getAmount());
	        	if(time > ProperTime)
	        	{	
	        		NBTTagCompound data = player.getEntityData();
	        		boolean hasReward = data.getBoolean("loyaltyrewards:reward" + reward.getUniqueName() + "Given");
	        		if(player != null && !player.world.isRemote && !hasReward)
	        		{
	        			ItemStack stack = reward.getReward().copy();
	        			String command = String.valueOf(reward.getCommand());

	        			if(LoyaltyRewardConfigGen.general.rewardToast)
	        			{
	        				String toastMessage = getToastMessage(player, reward.getTime(), reward.getAmount());
		        			LoyaltyPacketHandler.INSTANCE.sendTo(new LoyaltyToastPacket(reward.getReward().copy(), toastMessage), player);
	        			}
	        			else
	        			{
	    					sendRewardMessage(player, reward.getTime(), reward.getAmount());
	        			}
	        			
	        			if(!stack.isEmpty() && !command.isEmpty())
	    				{
	        				dropItem(player, stack);
	        				executeCommand(player, command);
	        				data.setBoolean("loyaltyrewards:reward" + reward.getUniqueName() + "Given", true);
	    				}
	    				else if(!stack.isEmpty() && command.isEmpty())
	    				{
	        				dropItem(player, stack);
	        				data.setBoolean("loyaltyrewards:reward" + reward.getUniqueName() + "Given", true);
	    				}
	    				else if(stack.isEmpty() && !command.isEmpty())
	    				{
	    					executeCommand(player, command);
	    					data.setBoolean("loyaltyrewards:reward" + reward.getUniqueName() + "Given", true);
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
			TextComponentTranslation text = new TextComponentTranslation("loyaltyrewards.inventory.full");
			text.getStyle().setColor(TextFormatting.YELLOW);
			player.sendMessage(text);
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
		}
		
		server.commandManager.executeCommand(server, rawCommand);
	}
	
	public void sendRewardMessage(EntityPlayerMP player, int time, String amount)
	{
		String minuteAmount = "";
		if(amount.contains("sec"))
		{
			if(time > 1)
			{
				minuteAmount = "seconds";
			}
			else
			{
				minuteAmount = "second";
			}
		}
		if(amount.contains("min"))
		{
			if(time > 1)
			{
				minuteAmount = "minutes";
			}
			else
			{
				minuteAmount = "minute";
			}
		}
		if(amount.contains("hour"))
		{
			if(time > 1)
			{
				minuteAmount = "hours";
			}
			else
			{
				minuteAmount = "hour";
			}
		}
		
		TextComponentTranslation text = new TextComponentTranslation("loyaltyrewards.rewarded.message", new Object[] {time, minuteAmount});
		text.getStyle().setColor(LoyaltyRewardConfigGen.general.messageColor);
		
		player.sendStatusMessage(text, true);
	}
	
	public static String getToastMessage(EntityPlayerMP player, int time, String amount)
	{
		String minuteAmount = "";
		if(amount.contains("sec"))
		{
			if(time > 1)
			{
				minuteAmount = "seconds";
			}
			else
			{
				minuteAmount = "second";
			}
		}
		if(amount.contains("min"))
		{
			if(time > 1)
			{
				minuteAmount = "minutes";
			}
			else
			{
				minuteAmount = "minute";
			}
		}
		if(amount.contains("hour"))
		{
			if(time > 1)
			{
				minuteAmount = "hours";
			}
			else
			{
				minuteAmount = "hour";
			}
		}
		
		return I18n.format("loyaltyrewards.rewarded.toast.message", new Object[] {time, minuteAmount});
	}
}
