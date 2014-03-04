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
import my_game.util.GameException;

/**
 * A packet used to communicate basic information about the server to clients, 
 * such as server name, host name, ip address, status, etc.
 * @author Ivo Parvanov
 */
public class ServerInfoPacket extends Packet {
    
    public String serverName;
    public String playerName;
    public InetAddress ipAddress;
    
    
    public ServerInfoPacket(byte[] data) throws GameException {
        super(PacketTypes.SERVERINFO.getId());
        //packet type checking
        String message = new String(data).trim();
        //get the packet type using the lookupPacket method on 
        //the first 2 characters of the message String (the packet id)
        String typeCode = message.substring(0,2);
        PacketTypes type = Packet.lookupPacket(typeCode);
        if(type.getId() != this.packetId) {
            throw new GameException("Wrong packet type found in ServerInfoPacket constructor: " + typeCode);
        }
        //now treat the data
        message = readData(data);
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
        int id = PacketTypes.SERVERINFO.getId();
        String typeId = (id > 9) ? (id + "") : ("0" + id);  //make sure the id is 2 digits
        String ret = (typeId + serverName + "~" + playerName + "~" + ipAddress.getHostAddress());
        return ret.getBytes();
    }
    
}
