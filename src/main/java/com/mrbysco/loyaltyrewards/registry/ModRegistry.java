package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.LoyaltyRewards;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRegistry {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, LoyaltyRewards.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, LoyaltyRewards.MOD_ID);

	public static final Supplier<RecipeType<RewardRecipe>> REWARD_RECIPE_TYPE = RECIPE_TYPES.register("reward_recipe", () -> new RecipeType<>() {
	});
	public static final Supplier<RewardRecipe.Serializer> REWARD_SERIALIZER = RECIPE_SERIALIZERS.register("reward_recipe", RewardRecipe.Serializer::new);
}
