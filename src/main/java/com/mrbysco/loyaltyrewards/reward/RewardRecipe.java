package com.mrbysco.loyaltyrewards.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import com.mrbysco.loyaltyrewards.util.RewardUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class RewardRecipe implements Recipe<RecipeInput> {
	public static final MapCodec<RewardRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
							Codec.INT.optionalFieldOf("time", 60).forGetter(recipe -> recipe.time),
							Codec.BOOL.optionalFieldOf("repeatable", false).forGetter(recipe -> recipe.repeatable),
							ItemStackTemplate.CODEC
									.listOf()
									.fieldOf("stacks")
									.forGetter(recipe -> recipe.stacks),

							Codec.STRING
									.listOf()
									.fieldOf("commands")
									.forGetter(recipe -> recipe.commands)
					)
					.apply(instance, RewardRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, RewardRecipe> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			o -> o.time,
			ByteBufCodecs.BOOL,
			o -> o.repeatable,
			ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()),
			o -> o.stacks,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
			o -> o.commands,
			RewardRecipe::new
	);
	public static final RecipeSerializer<RewardRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final int time;
	private final boolean repeatable;

	private final List<ItemStackTemplate> stacks;
	private final List<String> commands;

	public RewardRecipe(int time, boolean repeatable, List<ItemStackTemplate> stacks, List<String> commands) {
		this.time = time;
		this.repeatable = repeatable;
		this.stacks = stacks;
		this.commands = commands;
	}

	/**
	 * @return Returns the time (in seconds) it takes for the player to get the reward
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @return true if the reward is repeatable, false if not
	 */
	public Boolean isRepeatable() {
		return repeatable;
	}

	/**
	 * @return Returns the list of commands to run when the reward is given
	 */
	public List<String> getCommands() {
		return commands;
	}

	/**
	 * @return Returns the list of stacks to give when the reward is given
	 */
	public List<ItemStackTemplate> getStacks() {
		return stacks;
	}

	@Override
	public RecipeType<RewardRecipe> getType() {
		return ModRegistry.REWARD_RECIPE_TYPE.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	@Override
	public boolean matches(RecipeInput input, Level level) {
		return false;
	}

	@Override
	public ItemStack assemble(RecipeInput input) {
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<RewardRecipe> getSerializer() {
		return ModRegistry.REWARD_SERIALIZER.get();
	}

	public void triggerReward(Level level, BlockPos pos, ServerPlayer player) {
		if (!getCommands().isEmpty()) {
			for (String s : getCommands()) {
				String rawCommand = s;
				if (s.startsWith("\"") && s.endsWith("\"")) {
					rawCommand = rawCommand.substring(1, s.length() - 1);
				}
				if (rawCommand.contains("@PLAYERPOS")) {
					String command = rawCommand;
					rawCommand = command.replace("@PLAYERPOS", pos.getX() + " " + pos.getY() + " " + pos.getZ());
				} else if (rawCommand.contains("@PLAYER")) {
					String command = rawCommand;
					rawCommand = command.replace("@PLAYER", player.getName().getString());
				} else if (rawCommand.contains("@p")) {
					String command = rawCommand;
					rawCommand = command.replace("@p", player.getName().getString());
				}

				player.level().getServer().getCommands().performPrefixedCommand(this.createCommandSourceStack(player), rawCommand);
			}
		}

		for (ItemStackTemplate itemStack : getStacks()) {
			ItemStack stack = itemStack.create();
			if (player.addItem(stack)) {
				player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
						0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			} else {
				Component text = Component.translatable("loyaltyrewards.inventory.full").withStyle(ChatFormatting.YELLOW);
				player.sendSystemMessage(text);

				ItemEntity itemEntity = EntityType.ITEM.create(level, EntitySpawnReason.EVENT);
				if (itemEntity != null) {
					itemEntity.setItem(stack);
					itemEntity.setPos(pos.getX(), pos.getY() + 0.5, pos.getZ());
					level.addFreshEntity(itemEntity);
				}
			}
		}
		RewardUtil.sendRewardMessage(player, getTime());
	}

	private CommandSourceStack createCommandSourceStack(@Nullable ServerPlayer serverPlayer) {
		MinecraftServer server = serverPlayer.level().getServer();
		ServerLevel serverLevel = server.overworld();
		String s = serverPlayer == null ? "LoyaltyReward" : serverPlayer.getName().getString();
		Component component = serverPlayer == null ? Component.literal("LoyaltyReward") : serverPlayer.getDisplayName();
		return new CommandSourceStack(server, Vec3.atCenterOf(serverPlayer.blockPosition()), Vec2.ZERO, serverLevel, PermissionSet.ALL_PERMISSIONS,
				s, component, server, serverPlayer);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public String group() {
		return "";
	}
}
