package net.potatohed.mccombinechat.events;

import static net.potatohed.mccombinechat.resources.Utils.goThroughServerList;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class ServerChatEventHandler {

	@SubscribeEvent
	public void onServerChatEvent(ServerChatEvent event) {
		goThroughServerList(event.getComponent());
	}

	@SubscribeEvent
	public void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		goThroughServerList(new TextComponentString(event.player.getDisplayNameString() + " joined the game"));
	}

	@SubscribeEvent
	public void onPlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		goThroughServerList(new TextComponentString(event.player.getDisplayNameString() + " left the game"));
	}
}
