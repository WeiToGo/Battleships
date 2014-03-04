package my_game.networking.server;

public class Constants {

	/** Maximum amount of connections allowed on the server at a given time. */
	public static final int MAX_CONNECTIONS = 512;
	
	/** The port on which the server is listening. */
	public static final int SERVER_PORT = 9955;
        
        /** The port on which the server can receive server information
         * requests. The server will answer on this same port with its 
         * detailed information. */
        public static final int SERVER_INFO_PORT = 9956;
	
	/** A string defining the server's name. */
	public static final String SERVER_NAME = "Game Networking Demo Server";
}
