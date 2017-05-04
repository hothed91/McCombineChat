package net.potatohed.mccombinechat.resources;

public interface ModConstants {

	/* MOD INFORMATION */

	public static final String NAME = "McCombineChat";

	public static final String VERSION = "1.0";

	public static final String MODID = "mccombinechat";

	public static final String SERVER_PROXY_CLASS = "net.potatohed.mccombinechat.proxy.ServerProxy";

	public static final String CLIENT_PROXY_CLASS = "net.potatohed.mccombinechat.proxy.ClientProxy";

	public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";

	/* MOD CONFIG INFORMATION */

	public static final String CATEGORY_GENERAL = "general";

	public static final String CATEGORY_SERVERS = "servers";

	public static final String CONFIG_FILE_NAME = "mccombinechat.cfg";

	public static final String SERVER_ADDRESS_PROPERTY_NAME = "localServerAddress";
	
	public static final String SPARK_PORT_PROPERTY_NAME = "sparkPort";

	/* COMMON MOD DATA */

	public static final String LOCAL_SERVER_ADDRESS = "127.0.0.1";
	
	public static final int DEFAULT_SPARK_PORT = 4567;
	
	public static final String SERVER_ADDRESS_PLACEHOLDER = "Server-address-placeholder";
	
	/* Other */
	
	public static final String URI_PROTOCOL = "http://";
	
	public static final int DEFAULT_TIMEOUT_MILLSECONDS = 10000;

}
