package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
    public void trigger(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(stacks.length > 0) {
            for(int i = 0; i < stacks.length; i++) {
                ItemStack stack = this.stacks[i].copy();
                if(!stack.isEmpty()) {
                    if (playerIn.addItemStackToInventory(stack)) {
                        playerIn.world.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((playerIn.getRNG().nextFloat() - playerIn.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        ITextComponent text = new TextComponentString("loyaltyrewards.inventory.full");
                        text.getStyle().setColor(TextFormatting.YELLOW);
                        playerIn.sendMessage(text);

                        EntityItem itemEntity = new EntityItem(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ());
                        itemEntity.setItem(stack);
                        worldIn.spawnEntity(itemEntity);
                    }
                }
            }
        }
    }
}
