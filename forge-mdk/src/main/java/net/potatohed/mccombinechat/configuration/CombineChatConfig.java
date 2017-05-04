package net.potatohed.mccombinechat.configuration;

import static net.potatohed.mccombinechat.resources.ModConstants.CATEGORY_GENERAL;
import static net.potatohed.mccombinechat.resources.ModConstants.CATEGORY_SERVERS;
import static net.potatohed.mccombinechat.resources.ModConstants.CONFIG_FILE_NAME;
import static net.potatohed.mccombinechat.resources.ModConstants.DEFAULT_SPARK_PORT;
import static net.potatohed.mccombinechat.resources.ModConstants.LOCAL_SERVER_ADDRESS;
import static net.potatohed.mccombinechat.resources.ModConstants.SERVER_ADDRESS_PLACEHOLDER;
import static net.potatohed.mccombinechat.resources.ModConstants.SERVER_ADDRESS_PROPERTY_NAME;
import static net.potatohed.mccombinechat.resources.ModConstants.SPARK_PORT_PROPERTY_NAME;
import static net.potatohed.mccombinechat.resources.ModConstants.URI_PROTOCOL;
import static net.potatohed.mccombinechat.routes.RouteNames.SEND_MESSAGE;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import spark.Spark;

public class CombineChatConfig {

	private static Configuration config = null;

	private static String serverAddress;
	
	private static Integer sparkPort;

	private static HashMap<String, URL> serverList;

	public static void preInit(FMLPreInitializationEvent e) {
		serverList = new HashMap<String, URL>();
		config = new Configuration(new File(e.getModConfigurationDirectory().getPath(), CONFIG_FILE_NAME));
		syncFromFile();
	}

	public static void postInit(FMLPostInitializationEvent event) {
		System.out.println("Your Spark Port is " + Spark.port());
	}

	public static Configuration getConfig() {
		return config;
	}

	/**
	 * load the configuration values from the configuration file
	 */
	public static void syncFromFile() {
		syncConfig(true, true);
	}

	/**
	 * save the CombineChat variables (fields) to disk
	 */
	public static void syncFromFields() {
		syncConfig(false, false);
	}

	private static void syncConfig(boolean loadConfigFromFile, boolean readFieldsFromConfig) {
		if (loadConfigFromFile) {
			config.load();
		}

		Property propMyServerAddress = config.get(CATEGORY_GENERAL, SERVER_ADDRESS_PROPERTY_NAME, LOCAL_SERVER_ADDRESS, "Your LocalHost Server Address");
		Property propMySparkPort = config.get(CATEGORY_GENERAL, SPARK_PORT_PROPERTY_NAME, DEFAULT_SPARK_PORT, "The port that spark will run off of to receive messages");

		List<String> propOrderGeneral = new ArrayList<String>();
		propOrderGeneral.add(propMyServerAddress.getName());
		propOrderGeneral.add(propMySparkPort.getName());

		config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrderGeneral);

		if (readFieldsFromConfig) {
			serverAddress = propMyServerAddress.getString();
			sparkPort = propMySparkPort.getInt();
			readMapFromConfig();
		}

		propMyServerAddress.set(serverAddress);
		propMySparkPort.set(sparkPort);
		saveMapToConfig((serverList.isEmpty() ? getDefaultServerMap() : serverList));

		if (config.hasChanged()) {
			config.save();
		}
	}

	public static String getServerAddress() {
		return serverAddress;
	}
	
	public static Integer getSparkPort() {
		return sparkPort;
	}

	public static HashMap<String, URL> getServerList() {
		return serverList;
	}

	private static void saveMapToConfig(HashMap<String, URL> map) {
		for (Entry<String, URL> e : map.entrySet()) {
			config.get(CATEGORY_SERVERS, e.getValue().getHost(), e.getValue().getPort()).set(e.getValue().getPort());
		}
	}

	private static void readMapFromConfig() {
		ConfigCategory serverListCategory = config.getCategory(CATEGORY_SERVERS);
		serverListCategory.setComment("List of Servers that you will send messages to. \n NOTE: If you are hosting two servers on the same computer you will need to give one of the servers a different spark port. \n Also make sure to port forward your spark port otherwise no one can send you messages \n Format: I:<Server-Address>=<Spark-Port>");
		for (Entry<String, Property> server : serverListCategory.entrySet()) {
			if(!hasKey(server)) {
				addToServerList(server);
			} else {
				System.out.println("You have duplicates of " + server.getKey()+":"+server.getValue().getInt());
			}
		}
	}

	private static HashMap<String, URL> getDefaultServerMap() {
		HashMap<String, URL> defaultMap = new HashMap<String, URL>();
		try {
			defaultMap.put(SERVER_ADDRESS_PLACEHOLDER, new URL(URI_PROTOCOL + SERVER_ADDRESS_PLACEHOLDER + ":" + DEFAULT_SPARK_PORT + SEND_MESSAGE));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return defaultMap;
	}
	
	private static boolean hasKey(Entry<String, Property> server) {
		return serverList.containsKey(server.getKey()+":"+server.getValue().getInt());
	}
	
	private static void addToServerList(Entry<String, Property> server) {
		try {
			serverList.put(server.getKey()+":"+server.getValue().getInt(), new URL(URI_PROTOCOL + server.getKey() + ":" + server.getValue().getInt() + SEND_MESSAGE));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
