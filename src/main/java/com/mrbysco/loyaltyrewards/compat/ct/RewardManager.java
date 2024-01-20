package com.mrbysco.loyaltyrewards.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.loyaltyrewards.RewardManager")
public class RewardManager implements IRecipeManager<RewardRecipe> {
	public static final RewardManager INSTANCE = new RewardManager();

	private RewardManager() {
	}

	@ZenCodeType.Method
	public void addReward(String name, int time, boolean repeatable, IItemStack[] stacks, String[] commands) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final NonNullList<ItemStack> stackList = NonNullList.create();
		stackList.addAll(Arrays.stream(stacks).map(IItemStack::getInternal).toList());
		final NonNullList<String> commandList = NonNullList.create();
		commandList.addAll(Arrays.asList(commands));

		final RewardRecipe recipe = new RewardRecipe(time, repeatable, stackList, commandList);
		final RecipeHolder<RewardRecipe> recipeHolder = new RecipeHolder<>(id, recipe);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipeHolder));
	}


	@ZenCodeType.Method
	public void addItemReward(String name, int time, boolean repeatable, IItemStack[] stacks) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final NonNullList<ItemStack> stackList = NonNullList.create();
		stackList.addAll(Arrays.stream(stacks).map(IItemStack::getInternal).toList());
		final NonNullList<String> commandList = NonNullList.create();
		final RewardRecipe recipe = new RewardRecipe(time, repeatable, stackList, commandList);
		final RecipeHolder<RewardRecipe> recipeHolder = new RecipeHolder<>(id, recipe);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipeHolder));
	}

	@ZenCodeType.Method
	public void addCommandReward(String name, int time, boolean repeatable, String[] commands) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final NonNullList<ItemStack> stackList = NonNullList.create();
		final NonNullList<String> commandList = NonNullList.create();
		commandList.addAll(Arrays.asList(commands));
		final RewardRecipe recipe = new RewardRecipe(time, repeatable, stackList, commandList);
		final RecipeHolder<RewardRecipe> recipeHolder = new RecipeHolder<>(id, recipe);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, recipeHolder));
	}

	@Override
	public RecipeType<RewardRecipe> getRecipeType() {
		return ModRegistry.REWARD_RECIPE_TYPE.get();
	}
}
