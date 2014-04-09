/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.player_components.Player;
import my_game.networking.ServerInfo;
import my_game.networking.ServerListListener;
import my_game.networking.client.GameClient;
import my_game.networking.server.Constants;
import my_game.networking.server.GameServer;

/**
 *
 * @author Ivo
 */
public class ServerListPopulationTest {
        
    
    
    /**
     * SERVER LIST BUILDING TEST
     * @param args 
     */
    public static void main(String[] args) {
        GameServer s = null;
        GameClient c = null;
        try {
            //start a server on this machine
            s = new GameServer(new Player("Player1", "", InetAddress.getLocalHost(), Constants.SERVER_PORT, 0), "Server1", false);
            c = new GameClient(new Player("Client", "", InetAddress.getLocalHost(), Constants.SERVER_PORT, 0));
        } catch (UnknownHostException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        ArrayList<ServerInfo> l = new ArrayList<ServerInfo>();
        Thread t = c.getLANServersList(new ServerListListener() {

            public void addServerInfo(ServerInfo si) {                
                System.out.println("Server info retreived: ");
                System.out.println("Player name: " + si.playerName);
                System.out.println("Server name: " + si.serverName);
                System.out.println("Address: " + si.ipAddress.getHostAddress());
                System.out.println();
            }
        });
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        s.stopServer();
        System.out.println("Main done.");
    }
}
