package com.mrbysco.loyaltyrewards.compat.ct.impl;

import com.google.common.collect.Lists;
import com.mrbysco.loyaltyrewards.registry.RewardInfo;
import com.mrbysco.loyaltyrewards.registry.actions.BaseAction;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass("mods.loyaltyrewards.RewardInfo")
@ZenRegister
public class RewardData {
    private final RewardInfo internal;

    public RewardData(RewardInfo info) {
        this.internal = info;
    }

    @ZenConstructor
    public RewardData(String uniqueID, int time) {
        this(new RewardInfo(uniqueID, time));
    }

    @ZenConstructor
    public RewardData(String uniqueID, int time, boolean repeatable) {
        this(new RewardInfo(uniqueID, time, repeatable));
    }

    @ZenMethod
    public RewardData setActions(RewardAction[] actions) {
        if(actions.length > 0) {
            List<BaseAction> baseList = Lists.newArrayList();
            for(int i = 0; i < actions.length; i++) {
                BaseAction newInternal = actions[i].getInternal();
                baseList.add(newInternal);
            }
            BaseAction[] criteriaArray = new BaseAction[baseList.size()];
            criteriaArray = baseList.toArray(criteriaArray);
            return new RewardData(this.internal.setActions(criteriaArray));
        }

        return this;
    }

    @ZenMethod
    public RewardData setAction(RewardAction action) {
        return new RewardData(this.internal.setActions(new BaseAction[]{action.getInternal()}));
    }

    public RewardInfo getInternal() {
        return this.internal;
    }
}
