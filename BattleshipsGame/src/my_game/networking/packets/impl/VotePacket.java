/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking.packets.impl;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.networking.packets.Packet;
import my_game.networking.packets.PacketHandler;
import my_game.util.GameException;

/**
 *
 * @author Ivo
 */
public class VotePacket extends Packet {

    boolean vote;
    
    public VotePacket(boolean vote) {
        super(PacketTypes.VOTE.getId());
        
        this.vote = vote;
    }
    
    public VotePacket(byte[] data) {
        super(PacketTypes.VOTE.getId());
        try {
            //packet type checking
            String message = new String(data, "ISO-8859-1");
            
            //get the packet type using the lookupPacket method on 
            //the first 2 characters of the message String (the packet id)
            String typeCode = message.substring(0,2);
            PacketTypes type = Packet.lookupPacket(typeCode);
            if(type.getId() != this.packetId) {
                Logger.getLogger(VotePacket.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Wrong packet type found in ServerInfoPacket constructor: " + typeCode));
            }
            
            message = readData(data);
            message = message.split(PacketHandler.PACKET_SEPARATOR)[0];    //clearing the ending '#' symbol
            if(message.charAt(0) == '1') {
                vote = true;
            } else {
                vote = false;
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VotePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public byte[] getData() {
        try {
            String voteString = vote ? "1" : "0";
            int id = PacketTypes.VOTE.getId();
            String typeId = (id > 9) ? (id + "") : ("0" + id);  //make sure the id is 2 digits

            return (typeId + voteString + PacketHandler.PACKET_SEPARATOR).getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VotePacket.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean getVote() {
        return vote;
    }
}
