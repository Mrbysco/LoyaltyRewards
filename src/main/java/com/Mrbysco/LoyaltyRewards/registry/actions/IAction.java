package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAction {
    void trigger(World worldIn, BlockPos pos, PlayerEntity playerIn);
}
