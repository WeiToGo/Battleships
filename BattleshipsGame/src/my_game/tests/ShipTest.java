/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.tests;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.Map;
import my_game.models.game_components.ShipDirection;
import my_game.models.ships_impl.Cruiser;
import my_game.models.ships_impl.Destroyer;
import java.util.ArrayList;
import my_game.util.Range;
import my_game.util.Vector2;
import my_game.util.Positions;
import my_game.util.TurnPositions;
/**
 *
 * @author wei
 */
public class ShipTest {
    public static void main(String[] args) {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        Vector2 p1 = new Vector2(3,1);        
        Vector2 p2 = new Vector2(4,1); 
        Vector2 p3 = new Vector2(5,1); 
        Vector2 p4 = new Vector2(6,1); 
        Vector2 p5 = new Vector2(7,1);         
        positions.add(p1);
        positions.add(p2);
        positions.add(p3);
        positions.add(p4);
        positions.add(p5);
  
        ShipDirection sd = ShipDirection.West;
        Destroyer d = new Destroyer(1,positions, sd);
        
        Range r = d.getRadarRange();
        Vector2 tl = r.getTopLeft();
        Vector2 tr = r.getTopRight();
        Vector2 br = r.getBottomRight();
        Vector2 bl = r.getBottomLeft();

 /*     System.out.println("tl " + tl.x + " " + tl.y);
        System.out.println("tr" + tr.x + " " + tr.y);
        System.out.println("br " + br.x + " " + br.y);         
        System.out.println("bl " + bl.x + " " + bl.y);
 */      
        System.out.println("****  test radar range *************");
        ShipUnit[] su = d.getShipUnits();
        for (ShipUnit u: su){
            System.out.println(u.getPosition().x + "  " + u.getPosition().y);
        }
        System.out.println("**************************");
        ArrayList<Vector2> visible = d.getRadarPositions();
        for (Vector2 v: visible){
            System.out.println("visible " + v.x + "  " + v.y);
        }        
        
     //   ShipUnit[] shipUnits = c.getShipUnits();
        
        System.out.println("****  test turn  *************");
        TurnPositions allTurns = d.availableTurns();
        ArrayList<Vector2> left = allTurns.getLeft();
        ArrayList<Vector2> lp = allTurns.getLeftPath(); 
        ArrayList<Vector2> right = allTurns.getRight();   
        ArrayList<Vector2> rp = allTurns.getRightPath();    
        ArrayList<Vector2> b = allTurns.getBackward();    
        if (left != null){
            for (int i = 0; i < left.size(); i++){
                System.out.print("left" + left.get(i).x);
                System.out.print( "  ");
                System.out.println(left.get(i).y);        
            }
        }
        if (lp != null){
        for (int i = 0; i < lp.size(); i++){
            System.out.print("left path" + lp.get(i).x);
            System.out.print( "  ");
            System.out.println(lp.get(i).y);        
        }      
        }
        if (right != null){
        for (int i = 0; i < right.size(); i++){
            System.out.print("right " + right.get(i).x);
            System.out.print( "  ");
            System.out.println(right.get(i).y);        
        }
        }
        if (rp != null){
        for (int i = 0; i < rp.size(); i++){
            System.out.print("right path" + rp.get(i).x);
            System.out.print( "  ");
            System.out.println(rp.get(i).y);        
        }   
        }
        if (b != null) {
        for (int i = 0; i < b.size(); i++){
            System.out.print("back" + b.get(i).x);
            System.out.print( "  ");
            System.out.println(b.get(i).y);        
        }        
        }
    }    
}
