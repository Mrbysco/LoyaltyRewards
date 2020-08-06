package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.registry.actions.IAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RewardInfo implements IReward {
    private final String uniqueID;
    private int rewardTime;
    private boolean repeatable;
    private IAction[] actions = new IAction[]{};

    public RewardInfo(String uniqueID, int rewardTime) {
        this.uniqueID = uniqueID;
        this.rewardTime = rewardTime;
        this.repeatable = false;
    }

    public RewardInfo(String uniqueID, int rewardTime, boolean repeatable) {
        this.uniqueID = uniqueID;
        this.rewardTime = rewardTime;
        this.repeatable = repeatable;
    }

    @Override
    public int getTime() {
        return this.rewardTime;
    }

    @Override
    public void setTime(int time) {
        this.rewardTime = time;
    }

    @Override
    public String getName() {
        return this.uniqueID;
    }

    @Override
    public boolean repeatable() {
        return this.repeatable;
    }

    @Override
    public void trigger(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        if(this.actions.length > 0) {
            for(int i = 0; i < actions.length; i++) {
                IAction action = actions[i];
                action.trigger(worldIn, pos, playerIn);
            }
        }
        sendRewardMessage(playerIn, getTime());
    }

    public void sendRewardMessage(PlayerEntity player, int totalSeconds)
    {
        ITextComponent text = new TranslationTextComponent("loyaltyrewards.rewarded.message").mergeStyle(TextFormatting.YELLOW).append(secondsToString(totalSeconds));

        switch (LoyaltyConfig.SERVER.announceMethod.get()) {
            default:
                ITextComponent chatComponent = new StringTextComponent("[LoyaltyRewards] ").append(text);
                player.sendMessage(chatComponent, Util.DUMMY_UUID);
                break;
            case STATUS:
                player.sendStatusMessage(text, true);
                break;
        }
    }

    private ITextComponent secondsToString(int totalSeconds) {
        //Calculate the seconds to display:
        int seconds = totalSeconds % 60;
        totalSeconds -= seconds;
        //Calculate the minutes:
        long minutesCount = totalSeconds / 60;
        long minutes = minutesCount % 60;
        minutesCount -= minutes;
        //Calculate the hours:
        long hours = minutesCount / 60;
        TextComponent hourComponent = (TextComponent)new StringTextComponent(String.format("\n %02d", hours)).mergeStyle(TextFormatting.YELLOW);
        TextComponent hourExtra = (TextComponent)new StringTextComponent("H").mergeStyle(TextFormatting.GOLD);
        TextComponent minuteComponent = (TextComponent)new StringTextComponent(String.format(":%02d", minutes)).mergeStyle(TextFormatting.YELLOW);
        TextComponent minuteExtra = (TextComponent)new StringTextComponent("M").mergeStyle(TextFormatting.GOLD);
        TextComponent secondComponent = (TextComponent)new StringTextComponent(String.format(":%02d", seconds)).mergeStyle(TextFormatting.YELLOW);
        TextComponent secondExtra = (TextComponent)new StringTextComponent("S").mergeStyle(TextFormatting.GOLD);
        return hourComponent.append(hourExtra).append(minuteComponent).append(minuteExtra).append(secondComponent).append(secondExtra);
    }

    @Override
    public RewardInfo setActions(IAction[] actions) {
        this.actions = actions;
        return this;
    }

    @Override
    public IAction[] getActions() {
        return this.actions;
    }
}
