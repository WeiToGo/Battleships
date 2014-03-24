package my_game.tests;

import java.util.ArrayList;

import my_game.models.ships_impl.MineLayer;
import my_game.util.ShipDirection;
import my_game.util.Vector2;

public class MineTest {
	public static void main(String[] args) {
		ArrayList<Vector2> positions = new ArrayList<Vector2>();
	    Vector2 p1 = new Vector2(3,1);        
	    Vector2 p2 = new Vector2(4,1);         
	    positions.add(p1);
	    positions.add(p2);
	    
	    ShipDirection sd = ShipDirection.West;
	    MineLayer mineLayer = new MineLayer(1, positions, sd);
	    
	    //Game take instruction from user -> Map.layMine
	    //ship moves(every step it checks for the Mine and MineZone) -> Map.touchMine -> ship stops
	    //MineLayer moves -> when it stops it checks if there is Mine in the pick up zone -> Map.pickupMine
	    
	    
	    
	}
}
