package my_game.networking.packets;

import my_game.models.player_components.Player;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.Packet.PacketTypes;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.util.Misc;

/**
 * A handler for different types of packets.
 */
public class PacketHandler {
	
	NetworkEntity net;
	
	
	public PacketHandler(NetworkEntity entity) {
		net = entity;
	}
	
	public void handlePacket(byte[] data) {
		new HandlerThread(data).start();
	}

	class HandlerThread extends Thread {
		
		private byte[] data;
		
		HandlerThread(byte[] data) {
			this.data = new byte[data.length];
			for(int i = 0; i < data.length; i++) {
				this.data[i] = data[i];
			}
		}
		
		
		@Override
		public void run() {
			//extract packet message into a String
			String message = new String(data).trim();
			//get the packet type using the lookupPacket method on 
			//the first 2 characters of the message String (the packet id)
			String typeCode = message.substring(0,2);
			PacketTypes type = Packet.lookupPacket(typeCode);
			//now we can remove the packet id code from the message string (first 2 characters)
			message = message.substring(2);
			
			switch(type) {
			case GAMESTATE:
				//use the methods inside the game state packet to extract the game state from the data[]
				//GameStatePacket packet = new GameStatePacket(data);
				
				//net.updateGameState(packet.x, packet.y, packet.radius, packet.color);
				break;
			case HELLO:
				//send the username to the server
                            //just set a player with the username and no other information
                            net.setOpponent(new Player(message, "", net.getRemote(), 0, 0));
				break;
			case DISCONNECT:
				// TODO Implement a disconnect packet.
				break;
			case SILENT:
				// TODO Implement a silent packet. Silent packets are used to check whether a client/server connection is live.
				Misc.log("[PKT_HAND]: Silent packet received.");
				break;
			default:
			case INVALID:
				Misc.log("[PKT_HAND]: Invalid packet received - code: " + typeCode + ", data: " + message);
			}
		}
	}
}
