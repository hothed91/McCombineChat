package net.potatohed.mccombinechat.resources;

import static java.net.HttpURLConnection.HTTP_OK;
import static net.potatohed.mccombinechat.configuration.CombineChatConfig.getServerAddress;
import static net.potatohed.mccombinechat.configuration.CombineChatConfig.getSparkPort;
import static net.potatohed.mccombinechat.proxy.CommonProxy.executorService;
import static net.potatohed.mccombinechat.resources.ModConstants.DEFAULT_TIMEOUT_MILLSECONDS;
import static net.potatohed.mccombinechat.resources.ModConstants.SERVER_ADDRESS_PLACEHOLDER;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.util.text.ITextComponent;
import net.potatohed.mccombinechat.configuration.CombineChatConfig;

public class Utils {
	
	/**
	 * Takes a {@link ITextComponent} object and Serializes it into JSON
	 * @param component
	 * @return JSON
	 */
	public static String toJsonUsing(ITextComponent component) {
		return ITextComponent.Serializer.componentToJson(component);
	}

	/**
	 * Takes a JSON String and converts it back into a {@link ITextComponent} object
	 * @param JSON string
	 * @return a {@link ITextComponent}
	 */
	public static ITextComponent toComponentUsing(String json) {
		return ITextComponent.Serializer.jsonToComponent(json);
	}
	
	/**
	 * Creates a new thread and Iterates through the HashMap of Servers and sends a {@link ITextComponent} message
	 * @param component
	 */
	public static void goThroughServerList(ITextComponent component) {
		executorService.submit(() -> {
			HashMap<String, URL> serverList = CombineChatConfig.getServerList();
			for(Entry<String, URL> server : serverList.entrySet()) {
				if(isValid(server.getValue())){
					postRequest(server.getValue(), toJsonUsing(component));
				}
			}
		});
	}

	/**
	 * Sends a POST request with {@link ITextComponent} message to the specified url
	 * @param url
	 * @param message
	 */
	public static void postRequest(URL url, String message) {
		try {
			if(pingHost(url)){
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setDoOutput(true);
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestMethod("POST");
				con.connect();

				OutputStream os = con.getOutputStream();
				os.write(message.getBytes());
				os.flush();

				if (con.getResponseCode() != HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
				}

				con.disconnect();
			}else {
				System.out.println("Can't reach server: " + url.toString());
			}

		}  catch (IOException e) {
			e.printStackTrace();

		}
	}
	
	/**
	 * Ping the URL to see if it's available.
	 * @param url
	 * @return
	 */
	private static boolean pingHost(URL url) {
	    try (Socket socket = new Socket()) {
	        socket.connect(new InetSocketAddress(url.getHost(), url.getPort()), DEFAULT_TIMEOUT_MILLSECONDS);
	        return true;
	    } catch (IOException e) {
	        return false;
	    }
	}
	
	/**
	 * Checks to see if the server host is the server placeholder.
	 * NOTE: The placeholder is present whenever a config file is generated or if the user didn't update the config
	 * @param server
	 * @return
	 */
	private static boolean isPlaceholder(String server) {
		return server.equals(SERVER_ADDRESS_PLACEHOLDER);
	}
	
	/**
	 * Checks to see if the server parameter is the same as this server instance.
	 * @param server
	 * @return
	 */
	private static boolean isSameMinecraftServer(URL server) {
		return (server.getHost().equals(getServerAddress()) && server.getPort() == getSparkPort());
	}
	
	/**
	 * This validates that the server is not a placeholder and not the current server instance.
	 * @param server
	 * @return
	 */
	private static boolean isValid(URL server) {
		return (!isSameMinecraftServer(server) || !isPlaceholder(server.getHost()));
	}
}
