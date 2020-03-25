package com.mrbysco.loyaltyrewards;

import com.mrbysco.loyaltyrewards.config.LoyaltyRewardConfigGen;
import com.mrbysco.loyaltyrewards.handlers.AntiAfkHandler;
import com.mrbysco.loyaltyrewards.handlers.LoyaltyHandlers;
import com.mrbysco.loyaltyrewards.packets.LoyaltyPacketHandler;
import com.mrbysco.loyaltyrewards.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID,
	name = Reference.MOD_NAME, 
	version = Reference.VERSION,
	dependencies = Reference.DEPENDENCIES,
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
		MinecraftForge.EVENT_BUS.register(new LoyaltyHandlers());
			
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
