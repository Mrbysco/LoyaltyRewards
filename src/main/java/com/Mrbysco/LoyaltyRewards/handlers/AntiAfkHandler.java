package com.mrbysco.loyaltyrewards.handlers;

import com.mrbysco.loyaltyrewards.Reference;
import com.mrbysco.loyaltyrewards.config.LoyaltyRewardConfigGen;
import com.mrbysco.loyaltyrewards.packets.LoyaltyAfkPacket;
import com.mrbysco.loyaltyrewards.packets.LoyaltyPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.List;

public class AntiAfkHandler {
    private int timeSinceKeyPressed;	
	public static String AFK_TAG = Reference.MOD_PREFIX + "isAFK";

	public static void changeAFK(EntityPlayer player, boolean afk) {
		if(player.world.playerEntities.size() != 1) {
			if(afk)
				player.getEntityData().setBoolean(AFK_TAG, true);
			else
				player.getEntityData().setBoolean(AFK_TAG, false);
		}
	}

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event) {
        World worldIn = event.player.world;
        List<EntityPlayer> players = worldIn.playerEntities;
        if(players.size() == 1) {
            EntityPlayer player = players.get(0);
            player.getEntityData().setBoolean(AFK_TAG, false);
        }
    }
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			timeSinceKeyPressed++;

			if(timeSinceKeyPressed == LoyaltyRewardConfigGen.afkCheck.afkTime)
				LoyaltyPacketHandler.INSTANCE.sendToServer(new LoyaltyAfkPacket(true));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeystroke(KeyInputEvent event) {
		registerKeyPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeystroke(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		registerKeyPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMousePress(MouseInputEvent event) {
		if(Mouse.getEventButtonState() && Minecraft.getMinecraft().currentScreen != null)
			registerKeyPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMousePress(GuiScreenEvent.MouseInputEvent.Pre event) {
		if(Mouse.getEventButtonState() && Minecraft.getMinecraft().currentScreen != null)
			registerKeyPress();
	}

	private void registerKeyPress() {
		if(timeSinceKeyPressed >= LoyaltyRewardConfigGen.afkCheck.afkTime)
			LoyaltyPacketHandler.INSTANCE.sendToServer(new LoyaltyAfkPacket(false));
		timeSinceKeyPressed = 0;
	}
}
