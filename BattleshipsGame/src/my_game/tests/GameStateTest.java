/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import java.util.ArrayList;
import my_game.util.Range;
import my_game.util.Vector2;
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
      //  Ship[] player1Ships = GameState.generateShips(0, );   
      /*  for (Ship s: player1Ships){
            System.out.println(s.getShipType().toString());
            ShipUnit[] shipUnits = s.getShipUnits();
            for (ShipUnit su: shipUnits){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }
        } 
     */   
        ShipUnit[] units = player1Ships[6].getShipUnits();
            for (ShipUnit su: units){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }         
        System.out.println("new positions");         
        GameState.positionShip(player1Ships[6], new Vector2(29,24));
        units = player1Ships[2].getShipUnits();
    /*    System.out.println("new positions");
            for (ShipUnit su: units){
                Vector2 v = su.getPosition();
                System.out.println(v.x + " " + v.y);
            }        
    */
    }    
}
