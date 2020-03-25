package com.mrbysco.loyaltyrewards.registry.actions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandAction extends BaseAction {
    private String[] commands;

    public CommandAction(String[] commands) {
        this.commands = commands;
    }

    public CommandAction(String command) {
        this.commands = new String[] {command};
    }

    @Override
    public void trigger(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(commands.length > 0) {
            for(int i = 0; i < commands.length; i++) {
                MinecraftServer server = playerIn.getServer();
                String rawCommand = new String(this.commands[i]);
                if(rawCommand.contains("@PLAYERPOS")) {
                    String command = new String(rawCommand);
                    rawCommand = command.replace("@PLAYERPOS", pos.getX() + " " + pos.getY() + " " + pos.getZ());
                }
                else if(rawCommand.contains("@PLAYER")) {
                    String command = new String(rawCommand);
                    rawCommand = command.replace("@PLAYER", playerIn.getName());
                }

                server.getCommandManager().executeCommand(server, rawCommand);
            }
        }
    }
}
