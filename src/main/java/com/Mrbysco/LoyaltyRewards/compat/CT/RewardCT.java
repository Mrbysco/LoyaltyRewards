package com.mrbysco.loyaltyrewards.compat.ct;

import com.mrbysco.loyaltyrewards.compat.ct.impl.RewardData;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.loyaltyrewards.Rewards")
public class RewardCT {
	@ZenMethod
	public static void addReward(RewardData data) {
		CraftTweakerAPI.apply(new ActionAddReward(data));
	}
	
	@ZenMethod
	public static void removeReward(String uniqueID) {
		CraftTweakerAPI.apply(new ActionRemoveReward(uniqueID));
	}
}
