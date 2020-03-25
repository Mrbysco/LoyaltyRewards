package com.mrbysco.loyaltyrewards.utils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RewardHelper {
	public static ItemStack getEgg(String entityName)
	{
		ItemStack stack = new ItemStack(Items.SPAWN_EGG);
		NBTTagCompound entityTag = new NBTTagCompound();
		entityTag.setString("id", entityName);
		NBTTagCompound eggTag = new NBTTagCompound();
		eggTag.setTag("EntityTag", entityTag);
		stack.setTagCompound(eggTag);
		return stack;
	}
}
