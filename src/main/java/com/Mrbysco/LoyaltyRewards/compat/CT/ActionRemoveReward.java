package com.mrbysco.loyaltyrewards.compat.ct;

import com.mrbysco.loyaltyrewards.registry.RewardRegistry;
import crafttweaker.IAction;

public class ActionRemoveReward implements IAction {

	private final String uniqueID;

	public ActionRemoveReward(String ID) {
		this.uniqueID = ID;
	}
	
	@Override
	public void apply() {
		RewardRegistry.INSTANCE.removeReward(uniqueID);
	}

	@Override
	public String describe() {
		return "Removed reward with Unique ID: " + uniqueID;
	}
}