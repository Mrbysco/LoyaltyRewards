package com.mrbysco.loyaltyrewards.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.mrbysco.loyaltyrewards.compat.ct.impl.RewardData;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;

public class ActionAddReward implements IUndoableAction {
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
        return "Reward for " + rewardData.getTime() + " seconds has been added with Unique ID: " + rewardData.getName();
    }

    @Override
    public void undo() {
        RewardRegistry.INSTANCE.removeReward(rewardData.getName());
    }

    @Override
    public String describeUndo() {
        return "Reward with ID " + rewardData.getName() + " has been removed again";
    }
}
