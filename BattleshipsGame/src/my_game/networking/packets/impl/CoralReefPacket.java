/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking.packets.impl;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.CoralReef;
import my_game.networking.packets.Packet;
import my_game.networking.packets.PacketHandler;

/**
 * A packet used to send the contents of a coral reef within the confirmation
 * screen to the other player.
 * @author Ivo
 */
public class CoralReefPacket extends Packet {

    public boolean[][] reef;
    
    public CoralReefPacket(byte[] data) {
        super(PacketTypes.CORALREEF.getId());
        
        String message = readData(data);
        message = message.split(PacketHandler.PACKET_SEPARATOR)[0];    //clearing the ending '#' symbol
        String args[] = message.split("~");
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        
        reef = new boolean[width][height];
        //we need to go through the characters of the third string one by one
        int index = 0;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(args[2].charAt(index) == '1') {
                    reef[x][y] = true;
                } else {
                    reef[x][y] = false;
                }
                index++;
            }
        }
    }
    
    public CoralReefPacket(CoralReef reef) {
        super(PacketTypes.CORALREEF.getId());
        
        this.reef = new boolean[reef.WIDTH][reef.HEIGHT];
        
        for(int x = 0; x < reef.WIDTH; x++) {
            for(int y = 0; y < reef.HEIGHT; y++) {
                this.reef[x][y] = reef.hasObstacleIn(x, y);
            }
        }
    }
    
    @Override
    public byte[] getData() {
        //figure out the packet id
        int id = PacketTypes.CORALREEF.getId();
        String typeId = (id > 9) ? (id + "") : ("0" + id);  //make sure the id is 2 digits
        //build the coral reef array
        StringBuilder coralReef = new StringBuilder();
        for(int x = 0; x < reef.length; x++) {
            for(int y = 0; y < reef[0].length; y++) {
                if(reef[x][y]) {
                    coralReef.append('1');
                } else {
                    coralReef.append('0');
                }
            }
        }
        try {
            return (typeId + reef.length + "~" + reef[0].length + "~" + coralReef.toString() + PacketHandler.PACKET_SEPARATOR).getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CoralReefPacket.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
