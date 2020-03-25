package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.config.LoyaltyRewardConfigGen;
import com.mrbysco.loyaltyrewards.packets.LoyaltyPacketHandler;
import com.mrbysco.loyaltyrewards.packets.LoyaltyToastPacket;
import com.mrbysco.loyaltyrewards.registry.actions.IAction;
import com.mrbysco.loyaltyrewards.utils.TimeHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
    public void trigger(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(this.actions.length > 0) {
            for(int i = 0; i < actions.length; i++) {
                IAction action = actions[i];
                action.trigger(worldIn, pos, playerIn);
            }
        }
        sendRewardMessage(playerIn, getTime());
    }

    public void sendRewardMessage(EntityPlayer player, int totalSeconds)
    {
        ITextComponent text = new TextComponentTranslation("loyaltyrewards.rewarded.message");
        text.getStyle().setColor(TextFormatting.YELLOW);
        text.appendSibling(TimeHelper.secondsToTextComponent(totalSeconds));

        switch (LoyaltyRewardConfigGen.general.announceMethod) {
            default:
                ITextComponent chatComponent = new TextComponentString("[LoyaltyRewards] ").appendSibling(text);
                player.sendMessage(chatComponent);
                break;
            case STATUS:
                player.sendStatusMessage(text, true);
                break;
            case TOAST:
                String toastMessage = I18n.format("loyaltyrewards.rewarded.toast.message", new Object[] {TimeHelper.secondsToString(totalSeconds)});
                LoyaltyPacketHandler.INSTANCE.sendTo(new LoyaltyToastPacket(new ItemStack(Items.GOLD_INGOT), toastMessage), (EntityPlayerMP)player);
                break;
        }
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
