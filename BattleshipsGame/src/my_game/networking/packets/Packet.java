package my_game.networking.packets;

import my_game.networking.server.GameServer;

public abstract class Packet {

	public static enum PacketTypes {
		//assign an ID to every packet type
		INVALID(-1), HELLO(00), VOTE(01), CORALREEF(02), GAMESTATE(03), SERVERINFO(04);
		
		/** 
		 * The ID determining the packet type. It should be 
		 * represented as 2-characters when used in a String. 
		 */
		private int packetId;
		
		/**
		 * Constructor used to assign a single id to every 
		 * packet type created.
		 * @param packetId
		 */
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}
		
		/**
		 * @return The ID describing this packet type.
		 */
		public int getId() {
			return packetId;
		}
	};
	
	/** The ID describing the type of this packet. */
	protected byte packetId;
	
	/**
	 * Create a packet of the provided type.
	 * @param packetId Type of the packet.
	 */
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}
	
	/**
	 * @return The data of a packet in the form of a 
	 * byte array.
	 */
	public abstract byte[] getData();
	
	/**
	 * Converts the data array of a packet
	 * into a String and removes the two leading
	 * packet id characters of the String.
	 * @param data Data array of a packet.
	 * @return A String message representing the data.
	 */
	public static String readData(byte[] data) {
		String message = new String(data).trim();
		//trim out the beginning 2-digit packet type code
		return message.substring(2);
	}
	
	/**
	 * Returns the packet type for a packet with 
	 * the provided packetId.
	 * @param packetId The id of the packet in String form.
	 * @return
	 */
	public static PacketTypes lookupPacket(String packetId) {
		try {
			/* Call the integer parameter version of this method
			 * with the parsed integer code in the beginning of
			 * the packetId string.
			 */
			return lookupPacket(Integer.parseInt(packetId));
		} catch(NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
	
	/**
	 * Returns the packet type for a packet with 
	 * the provided packetId.
	 * @param packetId The id of the packet in as a number.
	 * @return
	 */
	public static PacketTypes lookupPacket(int packetId) {
		for(PacketTypes p: PacketTypes.values()) {
			//check every packet type in the enum and if the
			//IDs match, then we have found the corresponding packet type
			if(p.getId() == packetId) {
				return p;
			}
		}
		//could not match the provided packet type ID number, return invalid type
		return PacketTypes.INVALID;
	}
}
