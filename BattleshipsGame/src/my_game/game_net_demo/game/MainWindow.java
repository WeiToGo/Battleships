package my_game.game_net_demo.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

import my_game.networking.NetworkEntity;
import my_game.networking.client.GameClient;
import my_game.networking.server.Constants;
import my_game.networking.server.GameServer;
import my_game.models.player_components.Player;
import my_game.networking.server.packets.impl.GameStatePacket;
import my_game.networking.server.packets.impl.HelloPacket;
import my_game.util.Misc;

public class MainWindow extends JFrame implements DrawingPanelListener {

	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;
	
	
	JLabel connectionInfo;
	DrawingPanel panel;
	NetworkEntity net;
	
	public MainWindow(Player p) {
		//set up the JFrame's properties
		this.setName(p.getUsername());
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//add text label on top to display connection information
		connectionInfo = new JLabel("Waiting for connection.");
		this.add(connectionInfo, BorderLayout.NORTH);
		//add the panel in which circles can be drawn to the rest of the screen
		panel = new DrawingPanel();
		panel.addDrawingPanelListener(this);
		panel.setVisible(false);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		this.setVisible(true);
	}

	
	public void setOpponentUsername(String name) {
		connectionInfo.setText("Connected to " + name);
		panel.setVisible(true);
	}

	public void updateGameState(float x, float y, float radius, Color color) {
		panel.drawCircle(x, y, radius, color);
	}
	
	public void registerNetworkEntity(NetworkEntity net) {
		this.net = net;
	}
	
	@Override
	public void onNewCircleDrawn(float x, float y, float radius, Color color) {
		if(net != null) {
			net.sendGameState(x, y, radius, color);
		}
	}
	
	//----------------- MAIN AND STATIC STUFF --------------------------
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		boolean done = false;
		while(!done) {
			/******** MAIN MENU ************/
			System.out.println("Game Networking Demo");
			System.out.println("-----MAIN MENU------");
			System.out.println("1. Host Game");
			System.out.println("2. Join Game");
			System.out.println("3. Quit Game");
			if(in.hasNextInt()) {
				//get the user's choice
				int choice = in.nextInt();
				switch(choice) {
				case 1:
					hostGame();
					done = true;
					break;
				case 2:
					joinGame();
					//continue to case 3 to set done to true
				case 3:
					done = true;
					break;
				default:
					System.out.println("\nInvalid option selected.\n");	
				}
			} else {
				in.nextLine();
				//user did not enter an integer
				System.out.println("\nPlease enter an integer.\n");
			}
		}
		System.out.println("Bye");
	}

	private static void joinGame() {
		//Ask player for a username
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter your name: ");
		String username = in.nextLine();
		//now create a player object for the person playing
		Player player = null;
		InetAddress serverAddress = null;
		try {
			player = new Player(username, "", InetAddress.getByName("localhost"), Constants.SERVER_PORT);
	
			//get the server's IP address
			System.out.println("Please enter the server's IP address or machine name: ");
			serverAddress = InetAddress.getByName(in.nextLine());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();	//a weird exception which generally should not happen
		}
		//create a client
		GameClient client = new GameClient(player, new MainWindow(player), serverAddress);
	}

	private static void hostGame() {
		//Ask player for a username
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter your name: ");
		String username = in.nextLine();
		//now create a player object for the person playing
		Player player = null;
		try {
			player = new Player(username, "", InetAddress.getByName("localhost"), Constants.SERVER_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();	//a weird exception which generally should not happen
		}
		//create a server
		GameServer server = new GameServer(player, new MainWindow(player));
	}
}
