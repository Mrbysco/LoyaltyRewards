package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.config.LoyaltyConfig;
import com.mrbysco.loyaltyrewards.registry.actions.IAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RewardInfo implements IReward {
    private String uniqueID;
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
        ITextComponent text = new TranslationTextComponent("loyaltyrewards.rewarded.message").applyTextStyle(TextFormatting.YELLOW).appendSibling(secondsToString(totalSeconds));

        switch (LoyaltyConfig.SERVER.announceMethod.get()) {
            default:
                ITextComponent chatComponent = new StringTextComponent("[LoyaltyRewards] ").appendSibling(text);
                player.sendMessage(chatComponent);
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
        ITextComponent hourComponent = new StringTextComponent(String.format("\n %02d", hours)).applyTextStyle(TextFormatting.YELLOW);
        ITextComponent hourExtra = new StringTextComponent("H").applyTextStyle(TextFormatting.GOLD);
        ITextComponent minuteComponent = new StringTextComponent(String.format(":%02d", minutes)).applyTextStyle(TextFormatting.YELLOW);
        ITextComponent minuteExtra = new StringTextComponent("M").applyTextStyle(TextFormatting.GOLD);
        ITextComponent secondComponent = new StringTextComponent(String.format(":%02d", seconds)).applyTextStyle(TextFormatting.YELLOW);
        ITextComponent secondExtra = new StringTextComponent("S").applyTextStyle(TextFormatting.GOLD);
        return hourComponent.appendSibling(hourExtra).appendSibling(minuteComponent).appendSibling(minuteExtra).appendSibling(secondComponent).appendSibling(secondExtra);
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
