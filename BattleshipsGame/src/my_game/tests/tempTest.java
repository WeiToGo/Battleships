package my_game.tests;

import java.util.ArrayList;

import my_game.models.ships_impl.RadarBoat;
import my_game.util.ShipDirection;
import my_game.util.Vector2;

public class tempTest {

	public static void main(String[] args) {
		ArrayList<Vector2> positions = new ArrayList<Vector2>();
	    Vector2 p1 = new Vector2(3,1);        
	    Vector2 p2 = new Vector2(4,1); 
	    Vector2 p3 = new Vector2(5,1); 
	    positions.add(p1);
	    positions.add(p2);
	    positions.add(p3);
		
	    ShipDirection sd = ShipDirection.West;
	    
		RadarBoat rb = new RadarBoat(1, positions, sd);
		
		System.out.println("size: " + rb.getCurrentSize());
		System.out.println("speed: " + rb.getCurrentSpeed());
		
		rb.getShipUnits()[0].setDamage(10);
		
		rb.hitUpdate();
		
		System.out.println("size: " + rb.getCurrentSize());
		System.out.println("speed: " + rb.getCurrentSpeed());

	}

}
