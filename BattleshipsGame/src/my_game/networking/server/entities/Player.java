package my_game.networking.server.entities;

import java.net.InetAddress;

import my_game.game_net_demo.game.MainWindow;

public class Player {

	/* Address information of player. */
	private InetAddress ipAddress;
	private int playerPort;
	
	/* Player's identification. */
	private String username, password;	//password is not used at the moment
	
	public Player(String username, String password, InetAddress ipAddress, int port) {
		this.username = username;
		this.password = password;
		this.ipAddress = ipAddress;
		this.playerPort = port;
	}
	
	
	//TODO Security?
	
	public InetAddress getIP() {
		return ipAddress;
	}
	
	public int getPort() {
		return playerPort;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}
