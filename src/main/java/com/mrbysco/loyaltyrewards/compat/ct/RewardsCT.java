package com.mrbysco.loyaltyrewards.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.mrbysco.loyaltyrewards.compat.ct.impl.RewardData;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.loyaltyrewards.Rewards")
public class RewardsCT {
    @ZenCodeType.Method
    public static void addReward(RewardData data) {
        CraftTweakerAPI.apply(new ActionAddReward(data));
    }
}
