package my_game.networking.server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import my_game.models.player_components.Player;
import my_game.networking.NetEntityListener;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.PacketHandler;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.networking.packets.impl.HelloPacket;
import my_game.networking.packets.impl.ServerInfoPacket;
import my_game.util.Misc;

/**
 * This is the server code ran by the host of a game.
 */
public class GameServer implements NetworkEntity {

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
    
    private boolean serverRunning = false;
    /**
     * When a client is connected to the main server socket, this variable is
     * set to true. If for some reason the client disconnects, this variable
     * should be set to false in order for the main server thread to properly
     * work and start waiting for another client to connect, unless it is
     * stopped by setting serverRunning to false.
     */
    private boolean clientConnected;
    

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
        Thread t = new Thread(new MainServerThread());
        t.start();
        //start also a server information request thread
        Thread tInfo = new Thread(new ServerInfoThread());
        tInfo.start();
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
            infoSocket.close();
            in.close();
            out.close();
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

    public void setOpponentName(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateGameState(float x, float y, float radius, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendGameState(float x, float y, float radius, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * A runnable for the main server thread.
     */
    private class MainServerThread implements Runnable {

        public void run() {
            while (serverRunning) {
                //wait for someone to connect
                Misc.log("Server waiting for client...");
                try {
                    server = socket.accept();
                    Misc.log(server.getRemoteSocketAddress() + " has connected.");
                    clientConnected = true;
                    
                    //get the input and output streams which are used to send and receive messages from the client
                    in = new DataInputStream(server.getInputStream());
                    out = new DataOutputStream(server.getOutputStream());

                    //send host's username to the connected client by creating a hello packet with the username
                    sendData(new HelloPacket(serverHost.getUsername()).getData(), out);
                    clientConnected = true;

                    //server is listening until the client disconnects
                    while (clientConnected) {
                        //construct packet object to save received data into
                        byte[] data = new byte[1024];

                        //wait to receive a packet
                        in.read(data);
                        //handle packet
                        packetHandler.handlePacket(data);

                    }
                    //we are done and it has been requested that the server turns off
                    closeServer();

                    //end of thread
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }   //serverRunning == false, endwhile
        }
    }

    private class ServerInfoThread implements Runnable {

        public void run() {
            while (serverRunning) {
                try {
                    Socket s = infoSocket.accept();
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    //create an info packet, send it on the s socket and close socket and stream
                    ServerInfoPacket pkt = new ServerInfoPacket(serverName, serverHost.getUsername(), serverHost.getIP());
                    sendData(pkt.getData(), out);
                    out.close();
                    s.close();
                } catch (IOException e) {
                    Misc.log("Exception in info server.");
                    serverRunning = false;
                }
            }//serverRunning == false, endwhile
            //just end the thread
        }
    }
}
