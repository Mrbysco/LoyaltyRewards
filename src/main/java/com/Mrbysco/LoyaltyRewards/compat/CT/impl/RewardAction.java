package com.mrbysco.loyaltyrewards.compat.ct.impl;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.google.common.collect.Lists;
import com.mrbysco.loyaltyrewards.registry.actions.BaseAction;
import com.mrbysco.loyaltyrewards.registry.actions.CommandAction;
import com.mrbysco.loyaltyrewards.registry.actions.ItemAction;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.loyaltyrewards.RewardAction")
public class RewardAction {
    private final BaseAction internal;

    public RewardAction(BaseAction action) {
        this.internal = action;
    }

    @ZenCodeType.Constructor
    public RewardAction() {
        this.internal = new BaseAction();
    }

    @ZenCodeType.Method
    public RewardAction createItemReward(IItemStack stack) {
        BaseAction action = new ItemAction(stack.getInternal());
        return new RewardAction(action);
    }

    @ZenCodeType.Method
    public RewardAction createItemRewards(IItemStack[] stacks) {
        List<ItemStack> stackList = Lists.newArrayList();
        for(int i = 0; i < stacks.length; i++) {
            ItemStack newInternal = stacks[i].getInternal();
            stackList.add(newInternal);
        }
        ItemStack[] stackArray = new ItemStack[stackList.size()];
        stackArray = stackList.toArray(stackArray);

        BaseAction action = new ItemAction(stackArray);
        return new RewardAction(action);
    }

    @ZenCodeType.Method
    public RewardAction createCommandAction(String command) {
        BaseAction action = new CommandAction(command);
        return new RewardAction(action);
    }

    @ZenCodeType.Method
    public RewardAction createCommandActions(String[] commands) {
        BaseAction action = new CommandAction(commands);
        return new RewardAction(action);
    }

    public BaseAction getInternal() {
        return this.internal;
    }
}
