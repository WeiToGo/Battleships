package my_game.tests;

import java.util.ArrayList;

import my_game.models.game_components.Base;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Map;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipDirection;
import my_game.models.ships_impl.Cruiser;
import my_game.models.ships_impl.Destroyer;
import my_game.util.Vector2;

public class AttackTest {
	public static void main(String[] args) {
		ArrayList<Vector2> positions = new ArrayList<Vector2>();
	    Vector2 p1 = new Vector2(3,1);        
	    Vector2 p2 = new Vector2(4,1); 
	    Vector2 p3 = new Vector2(5,1); 
	    Vector2 p4 = new Vector2(6,1);         
	    positions.add(p1);
	    positions.add(p2);
	    positions.add(p3);
	    positions.add(p4);
	
	    ShipDirection sd = ShipDirection.West;
	    Destroyer s1 = new Destroyer(1,positions, sd);
	    
	    ArrayList<Vector2> positions2 = new ArrayList<Vector2>();
	    Vector2 p6 = new Vector2(10,1);
	    Vector2 p7 = new Vector2(11,1); 
	    Vector2 p8 = new Vector2(12,1); 
	    Vector2 p9 = new Vector2(13,1); 
	    Vector2 p10 = new Vector2(14,1);         
	    positions2.add(p6);
	    positions2.add(p7);
	    positions2.add(p8);
	    positions2.add(p9);
	    positions2.add(p10);
	
	    ShipDirection sd2 = ShipDirection.East;
	    Cruiser s2 = new Cruiser(1,positions, sd2);
	    
	    s2.fireCannon(s1.getShipUnits()[2]);
	    
	    for(int i = 0; i < 4; i++){
	    	System.out.println(s1.getShipUnits()[i].isHealthy());
	    }
	    
	    
	}
}
