package my_game.tests;

import java.util.ArrayList;

import my_game.models.game_components.MidUnit;
import my_game.models.game_components.ShipUnit;
import my_game.models.ships_impl.Destroyer;
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
		ShipUnit[] su = {new ShipUnit(), new MidUnit(), new ShipUnit()};
		rb.setShipUnits(su);
		
		/*
		System.out.println("size: " + rb.getCurrentSize());
		System.out.println("speed: " + rb.getCurrentSpeed());
		
		rb.getShipUnits()[1].setDamage(10);
		
		rb.hitUpdate();
		
		System.out.println("size: " + rb.getCurrentSize());
		System.out.println("speed: " + rb.getCurrentSpeed());
		 */
		
		ArrayList<Vector2> position2 = new ArrayList<Vector2>();
	    Vector2 p4 = new Vector2(5,2);        
	    Vector2 p5 = new Vector2(6,2); 
	    Vector2 p6 = new Vector2(7,2); 
	    position2.add(p4);
	    position2.add(p5);
	    position2.add(p6);
		
	    ShipDirection sd2 = ShipDirection.North;
	    
		Destroyer de = new Destroyer(1, positions, sd2);
		
		de.fireTorpedo(rb.getShipUnits()[1]);
		
		System.out.println("size: " + rb.getCurrentSize());
		System.out.println("damagelevel: " + rb.getShipUnits()[1].damageLevel);
		
		
	}

}
