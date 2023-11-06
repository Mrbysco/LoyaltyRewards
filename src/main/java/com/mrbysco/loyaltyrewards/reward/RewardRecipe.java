package com.mrbysco.loyaltyrewards.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mrbysco.loyaltyrewards.registry.ModRegistry;
import com.mrbysco.loyaltyrewards.util.RewardUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RewardRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String name;

	private final int time;
	private final boolean repeatable;

	private final NonNullList<ItemStack> stacks;
	private final NonNullList<String> commands;

	public RewardRecipe(ResourceLocation id, String name, int time, boolean repeatable, NonNullList<ItemStack> stacks, NonNullList<String> commands) {
		this.id = id;
		this.name = name;
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
	public NonNullList<String> getCommands() {
		return commands;
	}

	/**
	 * @return Returns the list of stacks to give when the reward is given
	 */
	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRegistry.REWARD_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inventory) {
		return getResultItem();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRegistry.REWARD_SERIALIZER.get();
	}

	public void triggerReward(Level level, BlockPos pos, ServerPlayer player) {
		if (!commands.isEmpty()) {
			for (String s : commands) {
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

				player.getServer().getCommands().performPrefixedCommand(this.createCommandSourceStack(player, rawCommand), rawCommand);
			}
		}

		for (ItemStack itemStack : stacks) {
			ItemStack stack = itemStack.copy();
			if (!stack.isEmpty()) {
				if (player.addItem(stack)) {
					player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
							0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
				} else {
					Component text = Component.translatable("loyaltyrewards.inventory.full").withStyle(ChatFormatting.YELLOW);
					player.sendSystemMessage(text);

					ItemEntity itemEntity = EntityType.ITEM.create(level);
					if (itemEntity != null) {
						itemEntity.setItem(stack);
						itemEntity.setPos(pos.getX(), pos.getY() + 0.5, pos.getZ());
						level.addFreshEntity(itemEntity);
					}
				}
			}
		}
		RewardUtil.sendRewardMessage(player, getTime());
	}

	private CommandSourceStack createCommandSourceStack(@Nullable ServerPlayer serverPlayer, String command) {
		MinecraftServer server = serverPlayer.getLevel().getServer();
		ServerLevel serverLevel = server.getLevel(Level.OVERWORLD);
		String s = serverPlayer == null ? "LoyaltyReward" : serverPlayer.getName().getString();
		Component component = (Component) (serverPlayer == null ? Component.literal("LoyaltyReward") : serverPlayer.getDisplayName());
		return new CommandSourceStack(server, Vec3.atCenterOf(serverPlayer.blockPosition()), Vec2.ZERO, serverLevel, 4,
				s, component, server, serverPlayer);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class RewardRecipeSerializer implements RecipeSerializer<RewardRecipe> {
		@Override
		public RewardRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "name", "");

			NonNullList<ItemStack> resultItems = stacksFromJson(GsonHelper.getAsJsonArray(jsonObject, "stacks"));
			NonNullList<String> resultCommands = commandsFromJson(GsonHelper.getAsJsonArray(jsonObject, "commands"));

			int time = GsonHelper.getAsInt(jsonObject, "time", 60);
			boolean repeatable = GsonHelper.getAsBoolean(jsonObject, "repeatable", false);
			return new RewardRecipe(recipeId, s, time, repeatable, resultItems, resultCommands);
		}

		private static NonNullList<ItemStack> stacksFromJson(JsonArray stacksArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for (int i = 0; i < stacksArray.size(); ++i) {
				JsonElement element = stacksArray.get(i);
				if (element.isJsonObject()) {
					JsonObject object = element.getAsJsonObject();
					ItemStack stack = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true, true);

					if (!stack.isEmpty())
						nonnulllist.add(stack);
				} else {
					throw new JsonParseException("Expected a JSON object");
				}
			}

			return nonnulllist;
		}

		private static NonNullList<String> commandsFromJson(JsonArray commandArray) {
			NonNullList<String> nonnulllist = NonNullList.create();

			for (int i = 0; i < commandArray.size(); ++i) {
				String command = commandArray.get(i).toString();
				if (!command.isEmpty()) {
					nonnulllist.add(command);
				} else {
					throw new JsonParseException("Unexpected empty command");
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public RewardRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf();

			int itemSize = buffer.readVarInt();
			NonNullList<ItemStack> resultItems = NonNullList.withSize(itemSize, ItemStack.EMPTY);
			for (int j = 0; j < resultItems.size(); ++j) {
				resultItems.set(j, buffer.readItem());
			}

			int commandSize = buffer.readVarInt();
			NonNullList<String> resultCommands = NonNullList.withSize(commandSize, "");
			for (int j = 0; j < resultCommands.size(); ++j) {
				resultCommands.set(j, buffer.readUtf());
			}

			boolean repeatable = buffer.readBoolean();
			int time = buffer.readInt();
			return new RewardRecipe(recipeId, s, time, repeatable, resultItems, resultCommands);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, RewardRecipe recipe) {
			buffer.writeUtf(recipe.name);

			buffer.writeVarInt(recipe.stacks.size());

			for (ItemStack stack : recipe.stacks) {
				buffer.writeItem(stack);
			}

			buffer.writeVarInt(recipe.commands.size());

			for (String command : recipe.commands) {
				buffer.writeUtf(command);
			}
			buffer.writeBoolean(recipe.repeatable);
			buffer.writeInt(recipe.time);
		}
	}
}
