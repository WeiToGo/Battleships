package my_game.networking.packets;

import de.lessvoid.nifty.loaderv2.types.apply.Convert;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.models.player_components.Player;
import my_game.networking.NetworkEntity;
import my_game.networking.packets.Packet.PacketTypes;
import my_game.networking.packets.impl.CoralReefPacket;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.networking.packets.impl.VotePacket;
import my_game.util.Misc;

/**
 * A handler for different types of packets.
 */
public class PacketHandler {
	
	NetworkEntity net;
        public static final String PACKET_SEPARATOR = "#end-of-packet#";
	
	
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
                    try {
                        //extract packet message into a String
                        String message = new String(data, "ISO-8859-1");
                        //if more than one packets were received, separate them
                        String[] args = message.split(PACKET_SEPARATOR);

                        //process every packet
                        for(int i = 0; i < args.length; i++) {
                            //get the packet type using the lookupPacket method on 
                            //the first 2 characters of the message String (the packet id)
                            String typeCode = args[i].substring(0,2);
                            PacketTypes type = Packet.lookupPacket(typeCode);
                            //now we can remove the packet id code from the message string (first 2 characters)
                            String noTypeMessage = args[i].substring(2);

                            switch(type) {
                            case VOTE:
                                    VotePacket vote = new VotePacket(args[i].getBytes("ISO-8859-1"));
                                    net.sendVoteToListeners(vote.getVote());
                            case HELLO:
                                    //send the username to the server
                                //just set a player with the username and no other information
                                net.setOpponent(new Player(noTypeMessage, "", net.getRemote(), 0, 0));
                                break;
                            case CORALREEF:
                                CoralReefPacket p = new CoralReefPacket(args[i].getBytes("ISO-8859-1"));
                                CoralReef reef = new CoralReef();
                                reef.setReef(p.reef);
                                net.sendCoralReefToListeners(reef);
                                break;
                            case GAMESTATE:
                                GameStatePacket g = new GameStatePacket(args[i].getBytes("ISO-8859-1"));
                                net.sendGameStateToListeners(g.getGameState());
                                break;
                            case SILENT:
                                break;
                            default:
                            case INVALID:
                                    Misc.log("[PKT_HAND]: Invalid packet received - code: " + typeCode + ", length: " + data.length);
                                    net.invalidPacket();
                                    break;
                            }
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(PacketHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}
}
