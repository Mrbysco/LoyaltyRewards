package com.Mrbysco.LoyaltyRewards.utils.list;

import net.minecraft.item.ItemStack;

public class RewardInfo {
	private String uniqueName;
	private ItemStack rewardItem;
	private String command;
	private int time;
	
	public RewardInfo(String name, ItemStack stack, String Command, int time) {
		this.uniqueName = name;
		this.rewardItem = stack;
		this.command = Command;
		this.time = time;
	}
	
	public RewardInfo(String name, String Command, int time) {
		this.uniqueName = name;
		this.rewardItem = ItemStack.EMPTY;
		this.command = Command;
		this.time = time;
	}
	
	public RewardInfo(String name, ItemStack stack, int time) {
		this.uniqueName = name;
		this.rewardItem = stack;
		this.command = "";
		this.time = time;
	}
	
	public ItemStack getReward() {
		return rewardItem;
	}
	
	public String getCommand() {
		return command;
	}
	
	public int getTime() {
		return time;
	}
	
	public String getUniqueName() {
		return uniqueName;
	}
}