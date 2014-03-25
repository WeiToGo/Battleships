/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking.packets.impl;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.networking.packets.Packet;

/**
 *
 * @author Ivo
 */
public class SilentPacket extends Packet {

    public SilentPacket() {
        super(PacketTypes.HELLO.getId());
    }
    
    @Override
    public byte[] getData() {
            try {
                //TODO Avoid using ~ as separator so that it can be used in passwords
                return ("05").getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SilentPacket.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    
}
