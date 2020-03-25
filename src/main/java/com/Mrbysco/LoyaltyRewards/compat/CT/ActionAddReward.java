package com.mrbysco.loyaltyrewards.compat.ct;

import com.mrbysco.loyaltyrewards.compat.ct.impl.RewardData;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import com.mrbysco.loyaltyrewards.utils.TimeHelper;
import crafttweaker.IAction;

public class ActionAddReward implements IAction {

	private final RewardInfo rewardData;

	public ActionAddReward(RewardData data) {
		this.rewardData = data.getInternal();
	}
	
	@Override
	public void apply() {
		RewardRegistry.INSTANCE.registerReward(rewardData);
	}

	@Override
	public String describe() {
		String amount = TimeHelper.secondsToString(rewardData.getTime());

		return "Reward for " + amount + " has been added with Unique ID: " + rewardData.getName();
	}
}