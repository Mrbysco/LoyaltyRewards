package com.mrbysco.loyaltyrewards.compat.ct.impl;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.google.common.collect.Lists;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.actions.BaseAction;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.loyaltyrewards.RewardInfo")
public class RewardData {
    private final RewardInfo internal;

    public RewardData(RewardInfo info) {
        this.internal = info;
    }

    @ZenCodeType.Constructor
    public RewardData(String uniqueID, int time) {
        this(new RewardInfo(uniqueID, time));
    }

    @ZenCodeType.Constructor
    public RewardData(String uniqueID, int time, boolean repeatable) {
        this(new RewardInfo(uniqueID, time, repeatable));
    }

    @ZenCodeType.Method
    public RewardData setActions(RewardAction[] actions) {
        if(actions.length > 0) {
            List<BaseAction> baseList = Lists.newArrayList();
            for (RewardAction action : actions) {
                BaseAction newInternal = action.getInternal();
                baseList.add(newInternal);
            }
            BaseAction[] criteriaArray = new BaseAction[baseList.size()];
            criteriaArray = baseList.toArray(criteriaArray);
            return new RewardData(this.internal.setActions(criteriaArray));
        }

        return this;
    }

    @ZenCodeType.Method
    public RewardData setAction(RewardAction action) {
        return new RewardData(this.internal.setActions(new BaseAction[]{action.getInternal()}));
    }

    public RewardInfo getInternal() {
        return this.internal;
    }
}
