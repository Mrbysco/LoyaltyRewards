package com.Mrbysco.LoyaltyRewards.packets;

import com.Mrbysco.LoyaltyRewards.Reference;
import com.Mrbysco.LoyaltyRewards.config.LoyaltyRewardConfigGen;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LoyaltyPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	static int ID = 0;
	
	public static void registerMessages() {
		
		INSTANCE.registerMessage(LoyaltyToastPacket.PacketHandler.class, LoyaltyToastPacket.class, ID++, Side.CLIENT);
		
		if(LoyaltyRewardConfigGen.afkCheck.antiAFK)
		{
			INSTANCE.registerMessage(LoyaltyAfkPacket.PacketHandler.class, LoyaltyAfkPacket.class, ID++, Side.SERVER);
		}
	}
}
