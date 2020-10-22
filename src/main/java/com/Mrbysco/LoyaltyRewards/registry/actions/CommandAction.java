package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandAction extends BaseAction {
    private final String[] commands;

    public CommandAction(String[] commands) {
        this.commands = commands;
    }

    public CommandAction(String command) {
        this.commands = new String[] {command};
    }

    @Override
    public void trigger(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        if(commands.length > 0) {
            for (String s : commands) {
                MinecraftServer server = playerIn.getServer();
                String rawCommand = s;
                if (rawCommand.contains("@PLAYERPOS")) {
                    String command = rawCommand;
                    rawCommand = command.replace("@PLAYERPOS", pos.getX() + " " + pos.getY() + " " + pos.getZ());
                } else if (rawCommand.contains("@PLAYER")) {
                    String command = rawCommand;
                    rawCommand = command.replace("@PLAYER", playerIn.getName().getUnformattedComponentText());
                } else if (rawCommand.contains("@p")) {
                    String command = rawCommand;
                    rawCommand = command.replace("@p", playerIn.getName().getUnformattedComponentText());
                }

                server.getCommandManager().handleCommand(server.getCommandSource(), rawCommand);
            }
        }
    }
}
