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
import my_game.util.Positions;
import my_game.util.Moves;
import my_game.util.Range;
import my_game.util.TurnPositions;
import my_game.util.Turns;
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
        System.out.println(map.toStringTry());
        Ship[] p0ships = gs.getShipsP0();
        Ship[] p1ships = gs.getShipsP1();
        Vector2 newPosition;
        
        /* TEST positionShip */
        
/*        System.out.println( "reposition ship[2] at (0,5). ");
        newPosition = new Vector2(0,5);
        gs.positionShip(p0ships[2], newPosition);
         
        System.out.println(map.toString());     

        System.out.println( "reposition ship[5] at (6,5). should not change anything.");        
        newPosition = new Vector2(6,5);
        gs.positionShip(p0ships[5], newPosition);       
     
        System.out.println(map.toString());          


        System.out.println( "reposition player1 ship[8] at (29,20).");              
        newPosition = new Vector2(29,20);
        gs.positionShip(p1ships[8], newPosition);       

        System.out.println(map.toString());            
  */
        /**   TEST move  */
        Ship s = p0ships[5];
        Positions p = map.prepareMoveShip(s);
        System.out.println(" size of list " + p.forward.size());
        
        Vector2 v = new Vector2(7,15);
        map.moveShip(s,v,p);
        System.out.println(map.toString());             
    }    
}
