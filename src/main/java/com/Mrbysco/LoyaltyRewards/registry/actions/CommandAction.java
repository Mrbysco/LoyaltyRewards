package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CommandAction extends BaseAction {
	private final String[] commands;

	public CommandAction(String[] commands) {
		this.commands = commands;
	}

	public CommandAction(String command) {
		this.commands = new String[]{command};
	}

	@Override
	public void trigger(Level worldIn, BlockPos pos, Player playerIn) {
		for (String s : commands) {
			MinecraftServer server = playerIn.getServer();
			String rawCommand = s;
			if (rawCommand.contains("@PLAYERPOS")) {
				String command = rawCommand;
				rawCommand = command.replace("@PLAYERPOS", pos.getX() + " " + pos.getY() + " " + pos.getZ());
			} else if (rawCommand.contains("@PLAYER")) {
				String command = rawCommand;
				rawCommand = command.replace("@PLAYER", playerIn.getName().getContents());
			} else if (rawCommand.contains("@p")) {
				String command = rawCommand;
				rawCommand = command.replace("@p", playerIn.getName().getContents());
			}

			server.getCommands().performCommand(server.createCommandSourceStack(), rawCommand);
		}
	}
}
