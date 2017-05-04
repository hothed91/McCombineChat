package net.potatohed.mccombinechat;

import static net.potatohed.mccombinechat.resources.ModConstants.ACCEPTABLE_REMOTE_VERSIONS;
import static net.potatohed.mccombinechat.resources.ModConstants.CLIENT_PROXY_CLASS;
import static net.potatohed.mccombinechat.resources.ModConstants.MODID;
import static net.potatohed.mccombinechat.resources.ModConstants.NAME;
import static net.potatohed.mccombinechat.resources.ModConstants.SERVER_PROXY_CLASS;
import static net.potatohed.mccombinechat.resources.ModConstants.VERSION;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.potatohed.mccombinechat.proxy.CommonProxy;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptableRemoteVersions = ACCEPTABLE_REMOTE_VERSIONS)
public class McCombineChat {
	
	@Mod.Instance(MODID)
	public static McCombineChat instance;

	@SidedProxy(serverSide = SERVER_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
	}
	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);	
	}

	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void preInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
}
