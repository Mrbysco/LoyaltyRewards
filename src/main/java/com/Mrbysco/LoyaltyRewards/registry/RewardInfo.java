package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.config.LoyaltyConfig.EnumAnnounceMethod;
import com.mrbysco.loyaltyrewards.registry.actions.IAction;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RewardInfo implements IReward {
	private final String uniqueID;
	private int rewardTime;
	private final boolean repeatable;
	private IAction[] actions = new IAction[]{};

	public RewardInfo(String uniqueID, int rewardTime) {
		this.uniqueID = uniqueID;
		this.rewardTime = rewardTime;
		this.repeatable = false;
	}

	public RewardInfo(String uniqueID, int rewardTime, boolean repeatable) {
		this.uniqueID = uniqueID;
		this.rewardTime = rewardTime;
		this.repeatable = repeatable;
	}

	@Override
	public int getTime() {
		return this.rewardTime;
	}

	@Override
	public void setTime(int time) {
		this.rewardTime = time;
	}

	@Override
	public String getName() {
		return this.uniqueID;
	}

	@Override
	public boolean repeatable() {
		return this.repeatable;
	}

	@Override
	public void trigger(Level worldIn, BlockPos pos, Player playerIn) {
		for (IAction action : actions) {
			action.trigger(worldIn, pos, playerIn);
		}
		sendRewardMessage(playerIn, getTime());
	}

	public void sendRewardMessage(Player player, int totalSeconds) {
		MutableComponent text = new TranslatableComponent("loyaltyrewards.rewarded.message").withStyle(LoyaltyConfig.SERVER.messageColor.get())
				.append(secondsToString(totalSeconds));

		if (LoyaltyConfig.SERVER.announceMethod.get() == EnumAnnounceMethod.STATUS) {
			player.displayClientMessage(text, true);
		} else {
			MutableComponent chatComponent = new TextComponent("[LoyaltyRewards] ").append(text);
			player.sendMessage(chatComponent, Util.NIL_UUID);
		}
	}

	private MutableComponent secondsToString(int totalSeconds) {
		//Calculate the seconds to display:
		int seconds = totalSeconds % 60;
		totalSeconds -= seconds;
		//Calculate the minutes:
		long minutesCount = totalSeconds / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;
		//Calculate the hours:
		long hours = minutesCount / 60;
		BaseComponent hourComponent = (BaseComponent) new TextComponent(String.format("\n %02d", hours)).withStyle(ChatFormatting.YELLOW);
		BaseComponent hourExtra = (BaseComponent) new TextComponent("H").withStyle(ChatFormatting.GOLD);
		BaseComponent minuteComponent = (BaseComponent) new TextComponent(String.format(":%02d", minutes)).withStyle(ChatFormatting.YELLOW);
		BaseComponent minuteExtra = (BaseComponent) new TextComponent("M").withStyle(ChatFormatting.GOLD);
		BaseComponent secondComponent = (BaseComponent) new TextComponent(String.format(":%02d", seconds)).withStyle(ChatFormatting.YELLOW);
		BaseComponent secondExtra = (BaseComponent) new TextComponent("S").withStyle(ChatFormatting.GOLD);
		return hourComponent.append(hourExtra).append(minuteComponent).append(minuteExtra).append(secondComponent).append(secondExtra);
	}

	@Override
	public RewardInfo setActions(IAction[] actions) {
		this.actions = actions;
		return this;
	}

	@Override
	public IAction[] getActions() {
		return this.actions;
	}
}
