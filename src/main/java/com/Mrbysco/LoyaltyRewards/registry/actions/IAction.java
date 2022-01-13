package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IAction {
    void trigger(Level worldIn, BlockPos pos, Player playerIn);
}
