package net.potatohed.mccombinechat.routes;

import static net.potatohed.mccombinechat.resources.ModConstants.DEFAULT_SPARK_PORT;
import static net.potatohed.mccombinechat.resources.Utils.toComponentUsing;
import static net.potatohed.mccombinechat.routes.RouteNames.SEND_MESSAGE;
import static net.potatohed.mccombinechat.routes.RouteNames.SUCCESS_MESSAGE;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.potatohed.mccombinechat.configuration.CombineChatConfig;
import spark.Spark;

/**
 * Central location of ALL Spark routes
 * @author hothe_000
 *
 */
public class ChatRoutes {

	/**
	 * Initializes Spark HTTP Routes
	 */
	public static void initRoutes() {
		postMessageRoute();
	}
	
	/**
	 * Changes the Spark port if one is present else will default on port 4567
	 */
	public static void initPort(){
		Integer port = CombineChatConfig.getSparkPort();
		Spark.port((port.toString().trim().isEmpty() ? DEFAULT_SPARK_PORT : port));
	}

	/**
	 * This creates a post on route "/send" that will receive JSON of a
	 * {@link ITextComponent} object and will send that message to the server
	 * receiving the request.
	 */
	private static void postMessageRoute() {
		Spark.post(SEND_MESSAGE, (request, response) -> {
			sendMcChatMsg(request.body());
			return SUCCESS_MESSAGE;
		});
	}

	/**
	 * Sends Minecraft Message using component
	 * 
	 * @param json
	 */
	private static void sendMcChatMsg(String json) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(toComponentUsing(json));
	}
}
