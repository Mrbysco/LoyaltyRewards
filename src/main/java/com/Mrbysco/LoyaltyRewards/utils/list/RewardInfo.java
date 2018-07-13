package com.Mrbysco.LoyaltyRewards.utils.list;

import net.minecraft.item.ItemStack;

public class RewardInfo {
	private String uniqueName;
	private ItemStack rewardItem;
	private String command;
	private int time;
	private String amount;
	
	public RewardInfo(String name, ItemStack stack, String Command, int time, String amount) {
		this.uniqueName = name;
		this.rewardItem = stack;
		this.command = Command;
		this.time = time;
		this.amount = amount;
	}
	
	public RewardInfo(String name, String Command, int time, String amount) {
		this.uniqueName = name;
		this.rewardItem = ItemStack.EMPTY;
		this.command = Command;
		this.time = time;
		this.amount = amount;
	}
	
	public RewardInfo(String name, ItemStack stack, int time, String amount) {
		this.uniqueName = name;
		this.rewardItem = stack;
		this.command = "";
		this.time = time;
		this.amount = amount;
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
	
	public String getAmount() {
		return amount;
	}
}