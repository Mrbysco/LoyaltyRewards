package com.Mrbysco.LoyaltyRewards;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.LoyaltyRewards.config.LoyaltyRewardConfigGen;
import com.Mrbysco.LoyaltyRewards.handlers.AntiAfkHandler;
import com.Mrbysco.LoyaltyRewards.handlers.PlayerTimeHandler;
import com.Mrbysco.LoyaltyRewards.packets.LoyaltyPacketHandler;
import com.Mrbysco.LoyaltyRewards.proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, 
	name = Reference.MOD_NAME, 
	version = Reference.VERSION, 
	acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)

public class LoyaltyRewards {
	@Instance(Reference.MOD_ID)
	public static LoyaltyRewards instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{	
		logger.info("Registering config / checking config");
		MinecraftForge.EVENT_BUS.register(new LoyaltyRewardConfigGen());

		if(LoyaltyRewardConfigGen.afkCheck.antiAFK)
		{
			logger.info("Registering Packets");
			LoyaltyPacketHandler.registerMessages();
		}
		
		proxy.Preinit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		logger.info("Registering Event Handler");
		MinecraftForge.EVENT_BUS.register(new PlayerTimeHandler());
			
		if(LoyaltyRewardConfigGen.afkCheck.antiAFK)
		{
			MinecraftForge.EVENT_BUS.register(new AntiAfkHandler());
		}
		
		proxy.Init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.Postinit();
	}
}
