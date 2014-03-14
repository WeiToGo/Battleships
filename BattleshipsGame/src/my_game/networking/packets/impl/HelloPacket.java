package my_game.networking.packets.impl;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.networking.packets.Packet;
import my_game.networking.packets.PacketHandler;

/**
 * The hello packet is used to send
 * the username to other client/server.
 * @author Ivaylo Parvanov
 */
public class HelloPacket extends Packet {

	private String username;
	
        /**
         * Build a Hello Packet from the byte[] data, but make sure to
         * remove the usual packet id (2 chars) from the beginning of the byte[] data.
         * @param data 
         */
	public HelloPacket(byte[] data) {
		this(readData(data));
	}
	
	public HelloPacket(String username) {
		super(PacketTypes.HELLO.getId());	//since we an int is used, no need to explicitely write 00
		//get the data into a String form
		this.username = username;
	}

	@Override
	public byte[] getData() {
            try {
                //TODO Avoid using ~ as separator so that it can be used in passwords
                return ("00" + this.username + PacketHandler.PACKET_SEPARATOR).getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(HelloPacket.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
	}

	
}
