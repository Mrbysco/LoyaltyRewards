package com.Mrbysco.LoyaltyRewards.compat.CT.action;

import com.Mrbysco.LoyaltyRewards.utils.list.RewardList;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

public class ActionReward implements IAction {

	private final String uniqueName;
	private final ItemStack rewardItem;
	private final String command;
	private final int time;
	private final String amount;
	private final boolean removal;

	public ActionReward(String name, IItemStack stack1, String command, int Time, String Amount) {
		this.uniqueName = name;
		this.rewardItem = CraftTweakerMC.getItemStack(stack1);
		this.command = command;
		this.time = Time;
		this.amount = Amount;
		this.removal = false;
	}
	
	public ActionReward(String name, IItemStack stack1, int Time, String Amount) {
		this.uniqueName = name;
		this.rewardItem = CraftTweakerMC.getItemStack(stack1);
		this.command = "";
		this.time = Time;
		this.amount = Amount;
		this.removal = false;
	}
	public ActionReward(String name, String command, int Time, String Amount) {
		this.uniqueName = name;
		this.rewardItem = ItemStack.EMPTY;
		this.command = command;
		this.time = Time;
		this.amount = Amount;
		this.removal = false;
	}
	
	public ActionReward(String name) {
		this.uniqueName = name;
		this.rewardItem = ItemStack.EMPTY;
		this.command = "";
		this.time = -1;
		this.amount = "";
		this.removal = true;
	}
	
	@Override
	public void apply() {
		if (this.removal)
			RewardList.removeRewardInfo(uniqueName);
		else
		{
			if(this.time != -1)
			{
				if(!this.rewardItem.isEmpty() && !command.isEmpty())
				{
					RewardList.addRewardInfo(this.uniqueName, rewardItem, command, time, amount);
				}
				else if(!this.rewardItem.isEmpty() && command.isEmpty())
				{
					RewardList.addRewardInfo(this.uniqueName, rewardItem, time, amount);
				}
				else if(this.rewardItem.isEmpty() && !command.isEmpty())
				{
					RewardList.addRewardInfo(this.uniqueName, command, time, amount);
				}
			}
		}
	}

	@Override
	public String describe() {
		if (this.removal)
			return String.format("Reward for " + this.time + " " + this.amount + ": " + this.uniqueName + " has been removed");	
		else
			return String.format("Reward for " + this.time + " " + this.amount + ": " + this.uniqueName + " has been added");	
	}
}