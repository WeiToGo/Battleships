package my_game.models.weapons_impl;

import my_game.models.game_components.Weapon;
import my_game.models.game_components.Ship.ShipDirection;
import my_game.util.Vector2;

public class Torpedo extends Weapon {
	

	public Vector2[] getTorpedoRange(Vector2 pos, ShipDirection direction) {
		
		//default position
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[10];
		int k = 0;
		
		if (direction == ShipDirection.North || direction == ShipDirection.East) {
			
			if (direction == ShipDirection.North){
				x = pos.y;
				y = pos.x;
			}
			
			for(int i = x + 1; i <= x + 10; i++) {
				range[k] = new Vector2(i, y);
				k++;		
			}
		}
		else {
			if (direction == ShipDirection.South){
				x = pos.y;
				y = pos.x;
			}
			
			for(int i = x + 1; i <= x + 10; i++) {	
				range[k] = new Vector2(i, y);
				k++;		
			}
		}
		
		return range;
	}
}
