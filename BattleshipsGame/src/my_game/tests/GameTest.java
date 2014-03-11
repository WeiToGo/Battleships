/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.controller.Game;
import my_game.models.game_components.CoralReef;
import my_game.models.player_components.Player;
import my_game.networking.server.Constants;
import my_game.networking.server.GameServer;

/**
 *
 * @author Ivo
 */
public class GameTest {

    public static void main(String[] args) {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GameTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Player player0 = new Player("player0", "", localAddress, Constants.SERVER_PORT, 0);
        Player player1 = new Player("player1", "", localAddress, Constants.SERVER_PORT, 0);
        CoralReef reef = new CoralReef();
        GameServer server = new GameServer(player0, "testServer");

        try {
            Game g = new Game(player0, player1, reef, server, Game.PlayerType.Host, "testGame");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            server.stopServer();
        }
    }
}
