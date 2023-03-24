package com.mrbysco.loyaltyrewards.registry;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe;
import com.mrbysco.loyaltyrewards.reward.RewardRecipe.RewardRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MOD_ID);

	public static final RegistryObject<RecipeType<RewardRecipe>> REWARD_RECIPE_TYPE = RECIPE_TYPES.register("reward_recipe", () -> new RecipeType<>() {
	});
	public static final RegistryObject<RewardRecipeSerializer> REWARD_SERIALIZER = RECIPE_SERIALIZERS.register("reward_recipe", RewardRecipeSerializer::new);
}
