package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemAction extends BaseAction {
    private ItemStack[] stacks;

    public ItemAction(ItemStack[] stacks) {
        this.stacks = stacks;
    }

    public ItemAction(ItemStack stack) {
        this.stacks = new ItemStack[] {stack};
    }

    @Override
    public void trigger(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        if(stacks.length > 0) {
            for(int i = 0; i < stacks.length; i++) {
                ItemStack stack = this.stacks[i].copy();
                if(!stack.isEmpty()) {
                    if (playerIn.addItemStackToInventory(stack)) {
                        playerIn.world.playSound((PlayerEntity) null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerIn.getRNG().nextFloat() - playerIn.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        ITextComponent text = new TranslationTextComponent("loyaltyrewards.inventory.full").applyTextStyle(TextFormatting.YELLOW);
                        playerIn.sendMessage(text);

                        ItemEntity itemEntity = EntityType.ITEM.create(worldIn);
                        itemEntity.setItem(stack);
                        itemEntity.setPosition(pos.getX(), pos.getY() + 0.5, pos.getZ());
                        worldIn.addEntity(itemEntity);
                    }
                }
            }
        }
    }
}
