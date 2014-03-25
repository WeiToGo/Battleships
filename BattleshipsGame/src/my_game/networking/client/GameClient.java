package my_game.networking.client;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.PacketHandler;
import my_game.networking.server.Constants;
import my_game.models.player_components.Player;
import my_game.networking.NetEntityListener;
import my_game.networking.ServerInfo;
import my_game.networking.ServerListListener;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.networking.packets.impl.HelloPacket;
import my_game.networking.packets.impl.ServerInfoPacket;
import my_game.networking.packets.impl.SilentPacket;
import my_game.networking.packets.impl.VotePacket;
import my_game.util.GameException;
import my_game.util.Misc;

/**
 * This is the client code allowing a player to connect to the host/server of a
 * game and communicate with him.
 *
 * @author Ivaylo Parvanov
 *
 */
public class GameClient extends Thread implements NetworkEntity {

    /**
     * Contains information about the client player including username, INET
     * address and port.
     */
    private Player client;
    private ArrayList<NetEntityListener> listeners;
    /**
     * Data stream used to send messages to the connected server.
     */
    private DataOutputStream out = null;
    /**
     * Data stream used to receive messages from the connected server.
     */
    private DataInputStream in = null;
    /**
     * The address of the server.
     */
    private InetAddress serverAddress = null;
    /**
     * A flag for stopping the client thread.
     */
    private boolean clientRunning = false, invalidReceived;
    /**
     * A packet handler handling the packets received by the client.
     */
    private PacketHandler packetHandler;
    /**
     * The socket used to connect to the server.
     */
    private Socket clientSocket;
    private Player connectedPlayer;
    /* The thread on which this client is running. */
    private Thread mainThread;

    public GameClient(Player clientPlayer) {
        this.client = clientPlayer;

        //init. listeners list
        listeners = new ArrayList<NetEntityListener>(1);

        //initialise handlers and other objects used by this class
        packetHandler = new PacketHandler(this);
        //Ready to connect!
    }

    /**
     * Connects the client to the specified server and starts the client 
     * thread.
     * @param serverAddress 
     */
    public void connect(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
        
        //start the client thread
        clientRunning = true;
        mainThread = new Thread(new ClientThread());
        mainThread.start();
    }
    
    /**
     * Gathers information about all servers on the LAN network. Since
     * this process is slow, and the servers are retrieved one at a time,
     * this method executes in a separate thread and every time it finds a server,
     * it notifies the ServerListListener about it. This thread can be interrupted
     * by the Thread reference returned.
     * @return The thread created for the server searching is returned.
     */
    public static Thread getLANServersList(final ServerListListener sll) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                boolean stop = false;
                while(!stop) {  //scan the whole LAN IP range until told to stop
                    InetAddress localhost;
                    try {
                        localhost = InetAddress.getLocalHost();
                        // this code assumes IPv4 is used

                        byte[] ip = localhost.getAddress();

                        //if the thread is interrupted the for loop terminates
                        for (int i = 1; i < 255; i++) {
                            if(Thread.currentThread().isInterrupted()) {    //check if interrupted
                                stop = true;
                                break;
                            }
                            ip[3] = (byte)i;
                            InetAddress address = InetAddress.getByAddress(ip);
                            //make connection to try to reach a server
                            try {
                                Socket s = new Socket();
                                s.connect(new InetSocketAddress(address, Constants.SERVER_INFO_PORT), 75);
                                DataInputStream di = new DataInputStream(s.getInputStream());

                                byte[] data = new byte[1024];
                                //wait to receive a packet
                                di.read(data);
                                //the packet should be a server info packet
                                ServerInfoPacket sip = new ServerInfoPacket(data);
                                //convert received packet into ServerInfo object
                                ServerInfo si = new ServerInfo(sip.serverName, sip.playerName, sip.ipAddress);

                                sll.addServerInfo(si);
                                //close socket and stream
                                di.close();
                                s.close();
                            } catch (GameException ex) {
                                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch(SocketTimeoutException ignore) {}

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
        return t;
    }

    public void addNetListener(NetEntityListener l) {
        listeners.add(l);
    }
    
    public void removeNetListener(NetEntityListener l) {
        listeners.remove(l);
    }
    
    public void setOpponent(Player p) {
        this.connectedPlayer = p;
    }

    public void sendGameStateToListeners(GameState gs) {
        for(NetEntityListener l: listeners) {
            l.onGameStateReceive(gs);
        }
    }

    public void sendGameState(GameState gs) {
        GameStatePacket p = new GameStatePacket(gs);
        try {
            this.sendData(p.getData());
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public InetAddress getRemote() {
        return clientSocket.getInetAddress();
    }
        
    public Player getConnectedPlayer() {
        return connectedPlayer;
    }

    public void sendCoralReefToListeners(CoralReef reef) {
        while(listeners.size() == 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(NetEntityListener l: listeners) {
            l.onReefReceive(reef);
        }
    }

    public void sendVoteToListeners(boolean vote) {
        for(NetEntityListener l: listeners) {
            l.onVoteReceive(vote);
        }
    }
    
    public void sendVote(boolean vote) {
        VotePacket v = new VotePacket(vote);
        try {
            sendData(v.getData());
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void invalidPacket() {
        invalidReceived = true;
    }
        
    private class ClientThread implements Runnable {
        public void run() {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            //notify all listeners that the client has connected to server
            for(NetEntityListener l: listeners) {
                l.onConnected();
            }
            
            //client is listening until the clientRunning flag is set to false
            while (clientRunning) {
                //construct packet object to save received data into
                byte[] data = new byte[24576];

                try {
                    //wait to receive a packet
                    in.read(data);

                    if(invalidReceived) {
                        //test if connection is still alive by sending silent
                        try {
                            sendData(new SilentPacket().getData());
                        } catch(IOException e) {
                            clientRunning = false;
                            Misc.log("Server disconnected. Will now close client.");
                        } finally {
                            invalidReceived = false;
                        }
                    }
                                            
                    //handle packet
                    packetHandler.handlePacket(data);
                } catch (Exception e) {
                    Misc.log("Exception in client.");
                    clientRunning = false;
                }
            }
            //end of thread
        }
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
        //we are done and it has been requested that the client turns off
        closeClient();
    }

    /**
     * @param data Data to send to the server.
     */
    public void sendData(byte[] data) throws IOException {
        //send packet
        out.write(data);
    }
}
