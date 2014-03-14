package my_game.networking.packets.impl;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.GameState;
import my_game.networking.packets.Packet;
import my_game.networking.packets.PacketHandler;
import my_game.util.GameException;
import my_game.util.Misc;

/**
 * A packet which can serialize a GameState object into a byte array to send
 * over a network and can also deserialize an incoming GameStatePacket to retreive
 * the GameState object sent.
 * @author Ivo Parvanov
 */
public class GameStatePacket extends Packet {
    GameState gs;
    
    public GameStatePacket(GameState gs) {
        super(PacketTypes.GAMESTATE.getId());
    
        this.gs = new GameState(gs);
    }
    
    /**
     * Deserializes the data array into a game state object.
     * @param data 
     */
    public GameStatePacket(byte[] data) {
        super(PacketTypes.GAMESTATE.getId());
        try {
            //packet type checking
            String message = new String(data, "ISO-8859-1");
            if(message.contains(PacketHandler.PACKET_SEPARATOR)) {
                 Logger.getLogger(GameStatePacket.class.getName()).log(Level.INFO, null, 
                         new GameException("Packet separator received in game state but should have been treated in packet handler."));
            }
            //get the packet type using the lookupPacket method on
            //the first 2 characters of the message String (the packet id)
            String typeCode = message.substring(0,2);
            PacketTypes type = Packet.lookupPacket(typeCode);
            //remove the typeCode from the data, to be able to deserialize the
            int id = PacketTypes.GAMESTATE.getId();
            String typeId = (id > 9) ? (id + "") : ("0" + id);  //make sure the id is 2 digits
            byte[] code = typeId.getBytes("ISO-8859-1");
            byte[] gameStateData = new byte[data.length - code.length];
            System.arraycopy(data, code.length, gameStateData, 0, gameStateData.length);
            //GameState object
            ByteArrayInputStream bis = new ByteArrayInputStream(gameStateData);
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(bis);
                this.gs = (GameState) in.readObject();
            } catch(IOException ex) {
                ex.printStackTrace();
            } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
                Misc.log("Class was not found in GameStatePacket.");
                this.gs = null;
            } finally {
              try {
                    bis.close();
                } catch (IOException ignore) {}
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ignore) {}
            }
        } catch(UnsupportedEncodingException ex) {
            Logger.getLogger(GameStatePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte[] getData() {
        if(this.gs == null) {
            return null;
        } else {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = null;
                
                byte[] data = null;
                try {
                    out = new ObjectOutputStream(bos);
                    out.writeObject(this.gs);
                    data = bos.toByteArray();
                } catch(IOException e) {
                    e.printStackTrace();
                    data = null;
                }finally {
                    try {
                        if(out != null) {
                            out.close();
                        }
                    } catch (IOException ignore) {}
                    try {
                        bos.close();
                    } catch (IOException ignore) {}
                }
                
                //add the packet type code to the beginning of the byte array
                int id = PacketTypes.GAMESTATE.getId();
                String typeId = (id > 9) ? (id + "") : ("0" + id);  //make sure the id is 2 digits
                byte[] typeCode = typeId.getBytes("ISO-8859-1");
                
                
                byte[] typeAndData = concat(typeCode, data);
                
                return concat(typeAndData, PacketHandler.PACKET_SEPARATOR.getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GameStatePacket.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
    
    /**
     * Concatenates two byte arrays into one byte array {A:B}
     * @param A
     * @param B
     * @return The concatenated array.
     */
    private byte[] concat(byte[] A, byte[] B) {
        int aLen = A.length;
        int bLen = B.length;
        //create an array which can hold both A and B arrays
        byte[] C = new byte[aLen + bLen];
        //copy A in the beginning of the new array
        System.arraycopy(A, 0, C, 0, aLen);
        //copy B starting from the first element after the last element of A
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
    
    public GameState getGameState() {
        return this.gs;
    }
}
