package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.registry.actions.IAction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IReward {
    /*
     * @return Returns the time (in seconds) it takes for the player to get the reward
     */
    int getTime();

    /*
     * @return Set the time it takes for the player to get the reward
     */
    void setTime(int time);

    /*
     * @return Unique name
     */
    String getName();

    /*
     * @return Returns if the reward repeats
     */
    boolean repeatable();

    /*
     * What happens when the reward is triggered
     *
     * @param playerIn The player who gets the reward
     */
    void trigger(Level worldIn, BlockPos pos, Player playerIn);

    RewardInfo setActions(IAction[] actions);

    IAction[] getActions();
}
