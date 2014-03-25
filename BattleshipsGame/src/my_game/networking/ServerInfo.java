/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking;

import java.net.InetAddress;
import javafx.beans.property.SimpleStringProperty;

/**
 * A simple placeholder for the information about a server used to build up the
 * server list.
 * @author Ivo Parvanov
 */
public class ServerInfo {
    
    public String serverName;
    public String playerName;
    public InetAddress ipAddress;
    
    public final SimpleStringProperty serverNameString;
    public final SimpleStringProperty playerNameString;
    public final SimpleStringProperty ipAddressString;
    
    public ServerInfo(String serverName, String playerName, InetAddress ipAddress) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.ipAddress = ipAddress;        
        
        this.serverNameString = new SimpleStringProperty(serverName);
        this.playerNameString = new SimpleStringProperty(playerName);
        this.ipAddressString = new SimpleStringProperty(ipAddress.toString());
    }
    
    public String getServerNameString() {
        return serverNameString.get();
    }
    
    public String getPlayerNameString() {
        return playerNameString.get();
    }
    
    public String getIpAddressString() {
        return ipAddressString.get();
    }
    
    @Override
    public String toString() {
        return ("server: " + serverName + "| " + playerName + ":" + ipAddress.toString());
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != this.getClass()) {
            return false;
        }
        ServerInfo other = (ServerInfo) o;
        if(serverName.equals(other.serverName) &&
                playerName.equals(other.playerName) &&
                ipAddress.equals(other.ipAddress)) {
            return true;
        } else {
            return false;
        }
    }
}
