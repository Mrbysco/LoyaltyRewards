package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemAction extends BaseAction {
    private final ItemStack[] stacks;

    public ItemAction(ItemStack[] stacks) {
        this.stacks = stacks;
    }

    public ItemAction(ItemStack stack) {
        this.stacks = new ItemStack[] {stack};
    }

    @Override
    public void trigger(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        if(stacks.length > 0) {
            for (ItemStack itemStack : stacks) {
                ItemStack stack = itemStack.copy();
                if (!stack.isEmpty()) {
                    if (playerIn.addItem(stack)) {
                        playerIn.level.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerIn.getRandom().nextFloat() - playerIn.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        ITextComponent text = new TranslationTextComponent("loyaltyrewards.inventory.full").withStyle(TextFormatting.YELLOW);
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
