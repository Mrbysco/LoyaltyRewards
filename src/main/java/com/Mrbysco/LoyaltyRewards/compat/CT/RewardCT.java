package com.Mrbysco.LoyaltyRewards.compat.CT;

import com.Mrbysco.LoyaltyRewards.compat.CT.action.ActionReward;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.loyaltyrewards.reward")
public class RewardCT {
	@ZenMethod
    public static void addReward(String uniqueName, int Time, String Amount, IItemStack stack, String command) {
        CraftTweakerAPI.apply(new ActionReward(uniqueName, stack, command, Time, Amount));
	}
	
	@ZenMethod
    public static void addReward(String uniqueName, int Time, String Amount, IItemStack stack) {
        CraftTweakerAPI.apply(new ActionReward(uniqueName, stack, Time, Amount));
	}
	
	@ZenMethod
	public static void addReward(String uniqueName, int Time, String Amount, String command) {
		CraftTweakerAPI.apply(new ActionReward(uniqueName, command, Time, Amount));
	}
	
	@ZenMethod
	public static void removeReward(String uniqueName) {
		CraftTweakerAPI.apply(new ActionReward(uniqueName));
	}
}
