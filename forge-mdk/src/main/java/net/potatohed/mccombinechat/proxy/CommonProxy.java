package net.potatohed.mccombinechat.proxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.potatohed.mccombinechat.configuration.CombineChatConfig;
import net.potatohed.mccombinechat.events.ServerChatEventHandler;
import net.potatohed.mccombinechat.routes.ChatRoutes;

public abstract class CommonProxy {
	public static ExecutorService executorService;

	public void preInit(FMLPreInitializationEvent e) {
		CombineChatConfig.preInit(e);
		executorService = Executors.newSingleThreadExecutor();
	}

	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new ServerChatEventHandler());
		ChatRoutes.initPort();
	}

	public void postInit(FMLPostInitializationEvent e) {
		ChatRoutes.initRoutes();
		CombineChatConfig.postInit(e);
	}

	/**
	 * is this a dedicated server?
	 * 
	 * @return true if this is a dedicated server, false otherwise
	 */
	abstract public boolean isDedicatedServer();
}
