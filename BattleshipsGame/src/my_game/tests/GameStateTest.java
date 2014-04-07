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
        Vector2 newPosition;       
        /* TEST positionShip */
  /*  
        System.out.println( "reposition ship[10] at (0,20). ");
        newPosition = new Vector2(0,20);
        gs.positionShip(p0ships[10], newPosition);
         
        System.out.println(map.toString());     

        System.out.println( "reposition ship[5] at (6,5). should not change anything.");        
        newPosition = new Vector2(6,5);
        gs.positionShip(p0ships[5], newPosition);       
     
        System.out.println(map.toString());          


        System.out.println( "reposition player1 ship[8] at (29,20).");              
        newPosition = new Vector2(29,20);
        gs.positionShip(p1ships[8], newPosition);       

        System.out.println(map.toString());            
  
        System.out.println("size  " + p1ships.length);
       /**   TEST move  */
    /*    Ship s = p1ships[10];
        Positions p = map.prepareMoveShip(s);
        ArrayList<Vector2> target = p.getForward();
        Vector2 v = target.get(0);
        map.moveShip(s,v,p);
        System.out.println(" move to " + v.x + " " + v.y);
        System.out.println(map.toString());   
        p = map.prepareMoveShip(s);
        target = p.getForward();
        v = target.get(0);
        map.moveShip(s,v,p);      
        System.out.println(" move to " + v.x + " " + v.y);
        System.out.println(map.toString());           
*/
    /*    System.out.println("***********************************");        
        Ship s = p1ships[9];
        Positions p = map.prepareMoveShip(s);
        ArrayList<Vector2> target = p.getForward();
        if (target != null){
            Vector2 v = target.get(target.size()-1);
            map.moveShip(s, v, p);
       //     System.out.println(" move to " + v.x + " " + v.y);
            System.out.println(map.toString());
        }
   
       boolean[][] sonar = map.getSonarVisibility(0);
       for (int i = 0; i< sonar.length; i++){
           for (int j = 0; i < sonar[i].length; j++){
//               System.out.println(sonar[i][j]);  
           }
       }
   */    
       
   
        Ship s = p0ships[8]; 
        map.layMine(s, new Vector2(8,11));
        System.out.println(map.toString());       
        Ship s2 = p0ships[1]; 
        Positions p = map.prepareMoveShip(s2);
        ArrayList<Vector2>  target = p.getForward();     
        Vector2 v = target.get(target.size()-3); 
        System.out.println(" move to " + v.x + " " + v.y);        
        map.moveShip(s2,v,p);
        System.out.println(map.toString());          
       
        System.out.println("***********************************");    
        Ship s3 = p0ships[6]; 
        p = map.prepareMoveShip(s3);
        target = p.getForward();
        v = target.get(target.size()/2); 
        map.moveShip(s3,v,p);
        System.out.println(" move to " + v.x + " " + v.y);
        System.out.println(map.toString());  
       
        /**   TEST turn  */
     /*   
        TurnPositions pTurn = map.prepareTurnShip(s2);
        ArrayList<Vector2> left = pTurn.getLeft();
        ArrayList<Vector2> right = pTurn.getRight();
        ArrayList<Vector2> back = pTurn.getBackward();
        System.out.println("left size " + left.size());
        System.out.println("right size " + right.size());
//        System.out.println("back size " + back.size());
     
        v = null;
        if(left != null){
            v = left.get(0);
        } 
   
       // if(back != null) v = back.get(0); 
        
       if(v != null){
            map.turnShip(s2, v, pTurn);
            System.out.println(" turn to " + v.x + " " + v.y);
            System.out.println(map.toString());
        }else{
            System.out.println("TURN BLOCKED");
        }
  
        System.out.println("***********************************");          
        p = map.prepareMoveShip(s);
        target = p.getForward();
        if (target != null){
            v = target.get(target.size() - 1);
            map.moveShip(s, v, p);
            System.out.println(" move to " + v.x + " " + v.y);
            System.out.println(map.toString());
        }else{
            System.out.println("MOVE BLOCKED");
        }
        System.out.println("***********************************");        
        pTurn = map.prepareTurnShip(s);        
        left = pTurn.getLeft();
        if(left != null) v = left.get(0);
        map.turnShip(s,v,pTurn);
        System.out.println(" turn to " + v.x + " " + v.y);
        System.out.println(map.toString());          
               
    */            
    
    }    
}
