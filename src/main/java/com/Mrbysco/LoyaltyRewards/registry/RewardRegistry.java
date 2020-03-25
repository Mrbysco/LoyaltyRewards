package com.mrbysco.loyaltyrewards.registry;

import com.google.common.collect.Maps;
import com.mrbysco.loyaltyrewards.LoyaltyRewards;

import java.util.Map;

public class RewardRegistry {
    public static RewardRegistry INSTANCE = new RewardRegistry();
    private Map<String, RewardInfo> infoMap = Maps.newHashMap();

    public static void initializeRewards() {
        //Blep
    }

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
}
