/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Map;
import my_game.models.player_components.Player;
import my_game.models.player_components.ChatLog;
import java.util.ArrayList;
import my_game.util.Range;
import my_game.util.Vector2;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author wei
 */
public class GameStateTest {
    public static void main(String[] args) {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();    
     /*   Ship[] player0Ships = GameState.generatePlayerShips(0);   
        for (Ship s: player0Ships){
            System.out.println(s.getShipType().toString());
            ShipUnit[] shipUnits = s.getShipUnits();
            for (ShipUnit su: shipUnits){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }
        }
      
       */
        InetAddress ip1 = null;
        InetAddress ip2 = null;
        try {
            ip1 = InetAddress.getLocalHost();
            ip2 = InetAddress.getLoopbackAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GameTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Player p1 = new Player("P1","abc",ip1,8001,0);
        Player p2 = new Player("P2", "def", ip2, 8001, 0);
        Player[] players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        CoralReef coral = new CoralReef();
        GameState gs = new GameState(players,coral,0,"game1");
        Map map = gs.getMap();
        System.out.println(map.toString());
        Ship[] p0ships = gs.getShipsP0();
        Ship[] p1ships = gs.getShipsP1();
        Vector2 newPosition = new Vector2(0,5);
        gs.positionShip(p0ships[2], newPosition);
        
        map = gs.getMap();     
        System.out.println(map.toString());        
      /*  for (Ship s: player1Ships){
            System.out.println(s.getShipType().toString());
            ShipUnit[] shipUnits = s.getShipUnits();
            for (ShipUnit su: shipUnits){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }
        } 
     */   
    /*    ShipUnit[] units = player1Ships[6].getShipUnits();
            for (ShipUnit su: units){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }         
        System.out.println("new positions");         
        GameState.positionShip(player1Ships[6], new Vector2(29,24));
        units = player1Ships[2].getShipUnits();
        System.out.println("new positions");
            for (ShipUnit su: units){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }        
    */
    }    
}
