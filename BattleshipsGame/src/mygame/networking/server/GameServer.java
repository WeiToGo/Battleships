package mygame.networking.server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.server.SocketSecurityException;

import ca.gamenetdemo.game.MainWindow;
import mygame.networking.NetworkEntity;
import mygame.networking.packets.PacketHandler;
import mygame.networking.server.entities.Player;
import mygame.networking.server.packets.impl.GameStatePacket;
import mygame.networking.server.packets.impl.HelloPacket;
import mygame.networking.util.Misc;

/**
 * This is the server code ran by the host of a game.
 */
public class GameServer extends Thread implements NetworkEntity {

	/** Socket on which the server accepts clients. */
	private ServerSocket socket;
	private Socket server;
	
	/** Data stream used to receive messages from the connected client. */
	private DataInputStream in = null;
	/** Data stream used to send messages to the connected client. */
	private DataOutputStream out = null;
	/** Packet handler for this server. */
	private PacketHandler packetHandler = null;
	
	
	/** A reference to the player hosting the server. */
	private Player serverHost;
	/** 
	 * A reference to the main window which created the server used
	 * for directly passing information to it instead of having to 
	 * connect to the server as a client. 
	 */
	private MainWindow main;
	
	private boolean serverRunning = false;
	
	public GameServer(Player hostPlayer, MainWindow main) {
		this.serverHost = hostPlayer;
		this.main = main;
		//register with main gui window
		main.registerNetworkEntity(this);
		
		try {
			//start server
			socket = new ServerSocket(Constants.SERVER_PORT);
			//if not exception, set the running flag to true
			serverRunning = true;
			Misc.log("Server initialised on port " + Constants.SERVER_PORT);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//initialise handlers and other objects used by this class
		packetHandler = new PacketHandler(this);
		
		//start the receiving thread
		this.start();
	}
	
	
	public void run() {
		//wait for someone to connect
		Misc.log("Server waiting for client...");

		try {
			server = socket.accept();
			Misc.log(server.getRemoteSocketAddress() + " has connected.");
			
			//get the input and output streams which are used to send and receive messages from the client
			in = new DataInputStream(server.getInputStream());
			out = new DataOutputStream(server.getOutputStream());
			
			//send host's username to the connected client by creating a hello packet with the username
			sendData(new HelloPacket(serverHost.getUsername()).getData());
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//server is listening until the serverRunning flag is set to false
		while(serverRunning) {			
			//construct packet object to save received data into
			byte[] data = new byte[1024];
			
			try {
				//wait to receive a packet
				in.read(data);
				//handle packet
				packetHandler.handlePacket(data);
			} catch(Exception e) {
				Misc.log("Exception in server.");
				serverRunning = false;
			}
		}
		//we are done and it has been requested that the server turns off
		closeServer();
		
		//end of thread
	}
	
	/**
	 * Closes the socket, streams and the server socket.
	 */
	private void closeServer() {
		Misc.log("Server will now close.");
		//close the socket and server when done
		try {
			server.close();
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Sends a data array to a given address and port.
	 * @param data data to send to the client.
	 */
	public void sendData(byte[] data) {
		//send packet
		try {
			out.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Requests that this server thread closes.
	 */
	public void stopServer() {
		serverRunning = false;
	}

	@Override
	public void setOpponentName(String name) {
		//relay information to the main window GUI to be displayed
		main.setOpponentUsername(name);
	}


	@Override
	public void updateGameState(float x, float y, float radius, Color color) {
		//relay information to the main window GUI to be displayed
		main.updateGameState(x, y, radius, color);
	}

	
	@Override
	public void sendGameState(float x, float y, float radius, Color color) {
		GameStatePacket p = new GameStatePacket(x, y, radius, color);
		sendData(p.getData());
	}
}
