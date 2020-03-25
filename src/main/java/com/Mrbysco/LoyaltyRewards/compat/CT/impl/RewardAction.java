package com.mrbysco.loyaltyrewards.compat.ct.impl;

import com.google.common.collect.Lists;
import com.mrbysco.loyaltyrewards.registry.actions.BaseAction;
import com.mrbysco.loyaltyrewards.registry.actions.CommandAction;
import com.mrbysco.loyaltyrewards.registry.actions.ItemAction;
import com.mrbysco.loyaltyrewards.utils.RewardHelper;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass("mods.loyaltyrewards.RewardAction")
@ZenRegister
public class RewardAction {
    private final BaseAction internal;

    public RewardAction(BaseAction action) {
        this.internal = action;
    }

    @ZenConstructor
    public RewardAction() {
        this.internal = new BaseAction();
    }

    @ZenMethod
    public RewardAction createItemReward(IItemStack stack) {
        BaseAction action = new ItemAction(CraftTweakerMC.getItemStack(stack));
        return new RewardAction(action);
    }

    @ZenMethod
    public RewardAction createItemRewards(IItemStack[] stacks) {
        List<ItemStack> stackList = Lists.newArrayList();
        for(int i = 0; i < stacks.length; i++) {
            ItemStack newInternal = CraftTweakerMC.getItemStack(stacks[i]);
            stackList.add(newInternal);
        }
        ItemStack[] stackArray = new ItemStack[stackList.size()];
        stackArray = stackList.toArray(stackArray);

        BaseAction action = new ItemAction(stackArray);
        return new RewardAction(action);
    }

    @ZenMethod
    public RewardAction createEggReward(String entityName) {
        BaseAction action = new ItemAction(RewardHelper.getEgg(entityName));
        return new RewardAction(action);
    }

    @ZenMethod
    public RewardAction createEggRewars(String[] entityNames) {
        List<ItemStack> stackList = Lists.newArrayList();
        for(int i = 0; i < entityNames.length; i++) {
            ItemStack newInternal = RewardHelper.getEgg(entityNames[i]);
            stackList.add(newInternal);
        }
        ItemStack[] stackArray = new ItemStack[stackList.size()];
        stackArray = stackList.toArray(stackArray);

        BaseAction action = new ItemAction(stackArray);
        return new RewardAction(action);
    }

    @ZenMethod
    public RewardAction createCommandAction(String command) {
        BaseAction action = new CommandAction(command);
        return new RewardAction(action);
    }

    @ZenMethod
    public RewardAction createCommandActions(String[] commands) {
        BaseAction action = new CommandAction(commands);
        return new RewardAction(action);
    }

    public BaseAction getInternal() {
        return this.internal;
    }
}
