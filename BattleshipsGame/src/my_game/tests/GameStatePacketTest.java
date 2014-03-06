/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.models.player_components.Player;
import my_game.networking.packets.impl.GameStatePacket;
import my_game.networking.server.Constants;
import my_game.util.Misc;

/**
 *
 * @author Ivo
 */
public class GameStatePacketTest {
    
    public static void main(String[] args) {
        try {
            Player p0 = new Player("Player0", "", InetAddress.getLocalHost(), Constants.SERVER_PORT, 1, 0);
            Player p1 = new Player("Player1", "", InetAddress.getLocalHost(), Constants.SERVER_PORT, 2, 0);

            CoralReef reef = new CoralReef();
            GameState gs = new GameState(new Player[] {p0, p1}, reef, 0, "TestGameState");
            //print out the generated state
            System.out.println(gs);
            //package the state into a GameStatePacket, then unpackage it
            GameStatePacket gsp = new GameStatePacket(gs);
            byte[] packedState = gsp.getData();
                    
            gsp = new GameStatePacket(packedState);
            GameState newState = gsp.getGameState();
            //output the new game state to see if it was properly conserved in transmission
            System.out.println("NEW GAME STATE:");
            System.out.println(newState);
        } catch(UnknownHostException e) {
            Misc.log("GameStatePacket test failed due to unknown host exception.");
        }
    }
}
