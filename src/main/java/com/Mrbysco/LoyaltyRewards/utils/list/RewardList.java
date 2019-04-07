package com.mrbysco.loyaltyrewards.utils.list;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class RewardList {

	public static ArrayList<RewardInfo> rewardList = new ArrayList<>();

	public static RewardInfo reward_info;
	
	public static void initializeRewards() {

	}
	
	public static void addRewardInfo(String name, ItemStack reward, String command, int Time, String Amount)
	{
		// Check if the info doesn't already exist
		reward_info = new RewardInfo(name, reward, command, Time, Amount);
		boolean containsName = false;
		for(RewardInfo info : rewardList)
		{
			if(info.getUniqueName().equals(name))
			{
				containsName = true;
			}
		}
		if(!containsName)
		{
			rewardList.add(reward_info);
			containsName = false;
		}
	}
	
	public static void addRewardInfo(String name, ItemStack reward, int Time, String Amount)
	{
		// Check if the info doesn't already exist
		reward_info = new RewardInfo(name, reward, Time, Amount);
		boolean containsName = false;
		for(RewardInfo info : rewardList)
		{
			if(info.getUniqueName().equals(name))
			{
				containsName = true;
				break;
			}
		}
		if(!containsName)
		{
			rewardList.add(reward_info);
			containsName = false;
		}
	}
	
	public static void addRewardInfo(String name, String command, int Time, String Amount)
	{
		// Check if the info doesn't already exist
		reward_info = new RewardInfo(name, command, Time, Amount);
		boolean containsName = false;
		for(RewardInfo info : rewardList)
		{
			if(info.getUniqueName().equals(name))
			{
				containsName = true;
				break;
			}
		}
		if(!containsName)
		{
			rewardList.add(reward_info);
			containsName = false;
		}
	}
	
	public static void removeRewardInfo(String uniqueName)
	{
		if(rewardList != null)
		{
			for(RewardInfo info : rewardList)
			{
				if(info.getUniqueName().equals(uniqueName))
				{
					rewardList.remove(info);
				}
			}
		}
	}
}
