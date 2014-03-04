/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking.packets.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.networking.packets.Packet;

/**
 * A packet used to communicate basic information about the server to clients, 
 * such as server name, host name, ip address, status, etc.
 * @author Ivo Parvanov
 */
public class ServerInfoPacket extends Packet {
    
    String serverName;
    String playerName;
    InetAddress ipAddress;
    
    
    public ServerInfoPacket(byte[] data) {
        super(PacketTypes.SERVERINFO.getId());
        
        String message = readData(data);
        String args[] = message.split("~");
        //now the message is split into the different server info pieces
        //parse info
        serverName = args[0];
        playerName = args[1];
        try {
            ipAddress = InetAddress.getByName(args[2]);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerInfoPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ServerInfoPacket(String serverName, String playerName, InetAddress ipAddress) {
        super(PacketTypes.SERVERINFO.getId());
        
        this.serverName = serverName;
        this.playerName = playerName;
        this.ipAddress = ipAddress;
    }

    @Override
    public byte[] getData() {
        return (serverName + "~" + playerName + "~" + ipAddress.getHostAddress()).getBytes();
    }
    
}
