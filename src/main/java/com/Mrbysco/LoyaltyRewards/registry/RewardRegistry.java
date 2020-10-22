package com.mrbysco.loyaltyrewards.registry;

import com.google.common.collect.Maps;
import com.mrbysco.loyaltyrewards.LoyaltyRewards;

import java.util.Map;

public class RewardRegistry {
    public static RewardRegistry INSTANCE = new RewardRegistry();
    private Map<String, RewardInfo> infoMap = Maps.newHashMap();

    public Map<String, RewardInfo> getInfoMap() {
        return this.infoMap;
    }

    public void registerReward(RewardInfo reward)
    {
        if(!infoMap.containsKey(reward.getName())) {
            infoMap.put(reward.getName(), reward);
        } else {
            LoyaltyRewards.LOGGER.error("An attempt was made to register a reward with an ID that already exists, ID: " + reward.getName());
        }
    }

    public void removeReward(String rewardName)
    {
        if(infoMap.containsKey(rewardName)) {
            infoMap.remove(rewardName);
        } else {
            LoyaltyRewards.LOGGER.error("An attempt was made to remove a reward with an ID that doesn't exists, ID: " + rewardName);
        }
    }
}
