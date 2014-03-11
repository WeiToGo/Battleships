package my_game.networking.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.models.player_components.Player;
import my_game.networking.NetEntityListener;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.PacketHandler;
import my_game.networking.packets.impl.CoralReefPacket;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.networking.packets.impl.HelloPacket;
import my_game.networking.packets.impl.ServerInfoPacket;
import my_game.networking.packets.impl.VotePacket;
import my_game.util.Misc;

/**
 * This is the server code ran by the host of a game.
 */
public class GameServer implements NetworkEntity {

    private final int CONNECTION_TIMEOUT = 1000;
            
    /**
     * Socket on which the server accepts clients. */
    private ServerSocket socket;
    /**
     * Socket on which the server receives info requests and replies to them
     * with the server info. */
    private ServerSocket infoSocket;
    /**
     * Assigned on connection with a client. */
    private Socket server;
    /**
     * Data stream used to receive messages from the connected client. */
    private DataInputStream in = null;
    /**
     * Data stream used to send messages to the connected client. */
    private DataOutputStream out = null;
    /**
     * Packet handler for this server. */
    private PacketHandler packetHandler = null;
    /**
     * A reference to the player hosting the server. */
    private Player serverHost;
    /**
     * Name of the server. */
    private final String serverName;
    
    private ArrayList<NetEntityListener> listeners;
    /** A reference to the main thread is necessary to stop it in case the server
     * is stopped while it is waiting for a client to connect. */
    private Thread mainThread;
    
    private boolean serverRunning = false;
    /**
     * When a client is connected to the main server socket, this variable is
     * set to true. If for some reason the client disconnects, this variable
     * should be set to false in order for the main server thread to properly
     * work and start waiting for another client to connect, unless it is
     * stopped by setting serverRunning to false.
     */
    private boolean clientConnected;
    
    private Player connectedPlayer;
    

    public GameServer(Player hostPlayer, String serverName) {
        this.serverHost = hostPlayer;
        this.serverName = serverName;

        //init. listeners list
        listeners = new ArrayList<NetEntityListener>(1);

        try {
            //start server
            socket = new ServerSocket(Constants.SERVER_PORT);
            //start the server info request server too
            infoSocket = new ServerSocket(Constants.SERVER_INFO_PORT);
            //if not exception, set the running flag to true
            serverRunning = true;
            Misc.log("Server initialised on port " + Constants.SERVER_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialise handlers and other objects used by this class
        packetHandler = new PacketHandler(this);

        //start a new main thread
        mainThread = new Thread(new MainServerThread());
        mainThread.start();
        //start also a server information request thread
        Thread tInfo = new Thread(new ServerInfoThread());
        tInfo.start();
    }
    
    public void addNetListener(NetEntityListener l) {
        listeners.add(l);
    }
    
    public void removeNetListener(NetEntityListener l) {
        listeners.remove(l);
    }

    /**
     * Closes the socket, streams and the server socket.
     */
    private void closeServer() {
        Misc.log("Server will now close.");
        //close the socket and server when done
        try {
            if(server != null) {
                server.close();
            }
            socket.close();
            infoSocket.close();
            if(in != null) {
                in.close();
            }
            if(out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a data array to a given address and port.
     *
     * @param data data to send to the client.
     */
    public void sendData(byte[] data, DataOutputStream output) {
        //send packet
        try {
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests that this server thread closes.
     */
    public void stopServer() {
        serverRunning = false;
    }

    public void setOpponent(Player p) {
        System.out.println("Player connected to server: " + p);
        this.connectedPlayer = p;
    }

    public void sendGameStateToListeners(GameState gs) {
        for(NetEntityListener l: listeners) {
            l.onGameStateReceive(gs);
        }
    }

    public void sendGameState(GameState gs) {
        GameStatePacket p = new GameStatePacket(gs);
        this.sendData(p.getData(), out);
    }

    public Player getConnectedPlayer() {
        return connectedPlayer;
    }
    
    public InetAddress getRemote() {
        return server.getInetAddress();
    }
    
    public boolean clientIsConnected() {
        return clientConnected;
    }

    /**
     * Sends a CoralReef object to the connected client to this server.
     * Doesn't send anything if the server is not running or there is no 
     * connected client.
     * @param reef The coral reef to send.
     */
    public void sendCoralReefToListeners(CoralReef reef) {
        CoralReefPacket packet = new CoralReefPacket(reef);
        this.sendData(packet.getData(), out);
    }

    public void sendVoteToListeners(boolean vote) {
        for(NetEntityListener l: listeners) {
            l.onVoteReceive(vote);
        }
    }

    public void sendVote(boolean vote) {
        VotePacket v = new VotePacket(vote);
        this.sendData(v.getData(), out);
    }

    /**
     * @return The name of this server.
     */
    public String getName() {
        return this.serverName;
    }
    
    /**
     * A runnable for the main server thread.
     */
    private class MainServerThread implements Runnable {

        public void run() {
            while (serverRunning) {
                //wait for someone to connect
                try {
                    socket.setSoTimeout(CONNECTION_TIMEOUT);
                    server = socket.accept();
                    Misc.log(server.getRemoteSocketAddress() + " has connected.");
                    clientConnected = true;
                    
                    //get the input and output streams which are used to send and receive messages from the client
                    in = new DataInputStream(server.getInputStream());
                    out = new DataOutputStream(server.getOutputStream());

                    //send host's username to the connected client by creating a hello packet with the username
                    sendData(new HelloPacket(serverHost.getUsername()).getData(), out);
                    clientConnected = true;
                    //notify all listeners that a client has connected
                    for(NetEntityListener l: listeners) {
                        l.onConnected();
                    }

                    //server is listening until the client disconnects
                    while (clientConnected) {
                        //construct packet object to save received data into
                        byte[] data = new byte[8192];

                        //wait to receive a packet
                        in.read(data);
                        //handle packet
                        packetHandler.handlePacket(data);
                    }
                    clientConnected = false;
                    connectedPlayer = null;
                    //end of thread
                } catch (IOException ignore) {}
            }   //serverRunning == false, endwhile
            closeServer();  //call this to finalize the closing of all sockets and
            //data streams once the main loop has finished => no one is still listening 
            //on the sockets.
        }
    }

    private class ServerInfoThread implements Runnable {

        public void run() {
            while (serverRunning) {
                try {
                    infoSocket.setSoTimeout(CONNECTION_TIMEOUT);
                    
                    Socket s = infoSocket.accept();
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    //create an info packet, send it on the s socket and close socket and stream
                    ServerInfoPacket pkt = new ServerInfoPacket(serverName, serverHost.getUsername(), serverHost.getIP());
                    sendData(pkt.getData(), out);
                    out.close();
                    s.close();
                } catch (IOException e) { }
            }//serverRunning == false, endwhile
            //just end the thread
        }
    }
}
