package my_game.networking.client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import my_game.game_net_demo.game.MainWindow;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.PacketHandler;
import my_game.networking.server.Constants;
import my_game.networking.server.entities.Player;
import my_game.networking.server.packets.impl.GameStatePacket;
import my_game.networking.server.packets.impl.HelloPacket;
import my_game.util.Misc;

/**
 * This is the client code allowing a player to
 * connect to the host/server of a game and 
 * communicate with him.
 * @author Ivaylo Parvanov
 *
 */
public class GameClient extends Thread implements NetworkEntity {

	/** Contains information about the client player including username, INET address and port. */
	private Player client;
	/** 
	 * A reference to the main window interface used by the client. 
	 * This is used to be able to communicate directly with the GUI 
	 * instead of through message passing. 
	 */
	private MainWindow main;
	
	/** Data stream used to send messages to the connected server. */
	private DataOutputStream out = null;
	/** Data stream used to receive messages from the connected server. */
	private DataInputStream in = null;
	/** The address of the server. */
	private InetAddress serverAddress = null;
	/** A flag for stopping the client thread. */
	private boolean clientRunning = false;
	/** A packet handler handling the packets received by the client. */
	private PacketHandler packetHandler;
	/** The socket used to connect to the server. */
	private Socket clientSocket;
	
	
	public GameClient(Player clientPlayer, MainWindow mainWindow, InetAddress serverAddress) {
		this.client = clientPlayer;
		this.main = mainWindow;
		this.serverAddress = serverAddress;
		//register with main gui window
		main.registerNetworkEntity(this);
		
		//initialise handlers and other objects used by this class
		packetHandler = new PacketHandler(this);
		
		//start the client thread
		clientRunning = true;
		this.start();
	}
	
	
	public void run() {
		//wait for someone to connect
		Misc.log("Server waiting for client...");

		try {
			//open a socket to the server and wait for it to accept the connection
			Misc.log("Client awaiting server to accept connection...");
			clientSocket = new Socket(serverAddress, Constants.SERVER_PORT);
			Misc.log("Client successfully connected to server.");
			//get the input and output streams for communication with the server
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			
			//send client's username to the server by creating a hello packet with the username
			sendData(new HelloPacket(client.getUsername()).getData());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//server is listening until the serverRunning flag is set to false
		while(clientRunning) {			
			//construct packet object to save received data into
			byte[] data = new byte[1024];
			
			try {
				//wait to receive a packet
				in.read(data);
				//handle packet
				packetHandler.handlePacket(data);
			} catch(Exception e) {
				Misc.log("Exception in client.");
				clientRunning = false;
			}
		}
		//we are done and it has been requested that the client turns off
		closeClient();
		
		//end of thread
	}
	
	/**
	 * Closes the socket and streams.
	 */
	private void closeClient() {
		Misc.log("Client will now close.");
		try {
			clientSocket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the client and stops its thread.
	 */
	public void stopClient() {
		clientRunning = false;
	}
	
	
	/**
	 * @param data Data to send to the server.
	 */
	public void sendData(byte[] data) {
		//send packet
		try {
			out.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
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
