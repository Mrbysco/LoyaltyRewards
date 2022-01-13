package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemAction extends BaseAction {
    private final ItemStack[] stacks;

    public ItemAction(ItemStack[] stacks) {
        this.stacks = stacks;
    }

    public ItemAction(ItemStack stack) {
        this.stacks = new ItemStack[] {stack};
    }

    @Override
    public void trigger(Level worldIn, BlockPos pos, Player playerIn) {
        if(stacks.length > 0) {
            for (ItemStack itemStack : stacks) {
                ItemStack stack = itemStack.copy();
                if (!stack.isEmpty()) {
                    if (playerIn.addItem(stack)) {
                        playerIn.level.playSound((Player) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((playerIn.getRandom().nextFloat() - playerIn.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        Component text = new TranslatableComponent("loyaltyrewards.inventory.full").withStyle(ChatFormatting.YELLOW);
                        playerIn.sendMessage(text, Util.NIL_UUID);

                        ItemEntity itemEntity = EntityType.ITEM.create(worldIn);
                        if(itemEntity != null) {
                            itemEntity.setItem(stack);
                            itemEntity.setPos(pos.getX(), pos.getY() + 0.5, pos.getZ());
                            worldIn.addFreshEntity(itemEntity);
                        }
                    }
                }
            }
        }
    }
}
