package com.mrbysco.loyaltyrewards.compat.ct;

import com.blamejared.crafttweaker.api.actions.IAction;
import com.mrbysco.loyaltyrewards.compat.ct.impl.RewardData;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.RewardRegistry;

import java.time.LocalTime;

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
        LocalTime time = LocalTime.now().plusSeconds(rewardData.getTime());
        String amount = time.toString();

        return "Reward for " + amount + " has been added with Unique ID: " + rewardData.getName();
    }
}
