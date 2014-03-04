package my_game.networking.packets.impl;

import my_game.networking.packets.Packet;

/**
 * The hello packet is used to send
 * the username to other client/server.
 * @author Ivaylo Parvanov
 */
public class HelloPacket extends Packet {

	private String username;
	
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
		//TODO Avoid using ~ as separator so that it can be used in passwords
		return ("00" + this.username).getBytes();
	}

	
}
