package com.Mrbysco.LoyaltyRewards.packets;

import com.Mrbysco.LoyaltyRewards.config.LoyaltyRewardConfigGen;
import com.blamejared.ctgui.reference.Reference;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LoyaltyPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	public static void registerMessages() {
		if(LoyaltyRewardConfigGen.afkCheck.antiAFK)
		{
			INSTANCE.registerMessage(LoyaltyAfkPacket.PacketHandler.class, LoyaltyAfkPacket.class, 0, Side.SERVER);
		}
	}
}
