package my_game.models.weapons_impl;

import my_game.models.game_components.Weapon;
import my_game.models.game_components.Ship.ShipDirection;
import my_game.util.Vector2;

public class Cannon extends Weapon {
	
	public Cannon() {
		damageLevel = 1;
	}
	
	public Cannon(int damage) {
		damageLevel = damage;
	}
	
	public Vector2[] getCruiserRange(Vector2 pos, ShipDirection direction) {
		//default
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[160];
		int k = 0;
		
		if (direction == ShipDirection.North){
			x = pos.y;
			y = pos.x;
		}
		
		if (direction == ShipDirection.West) {
			x = pos.x + 4;
		}
		
		if (direction == ShipDirection.South) {
			x = pos.y;
			y = pos.x + 4;
		}
		
		for(int i = x - 9; i <= x + 5; i++) {
			for(int j = y - 5; j <= y + 5; j++) {
				if ((i==x-4 && j==y) || (i==x-3 && j==y) || (i==x-2 && j==y) || (i==x-1 && j==y) || (i==x && j==y))
					continue;
				
				range[k] = new Vector2(i, j);
				k++;		
			}
		}
		return range;
	}
	
	public Vector2[] getDestroyerRange(Vector2 pos, ShipDirection direction) {
		//default
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[104];
		int k = 0;
		
		if (direction == ShipDirection.North){
			x = pos.y;
			y = pos.x;
		}
		
		if (direction == ShipDirection.West) {
			x = pos.x + 3;
		}
		
		if (direction == ShipDirection.South) {
			x = pos.y;
			y = pos.x + 3;
		}
		
		for(int i = x - 7; i <= x + 4; i++) {
			for(int j = y - 4; j <= y + 4; j++) {
				if ((i==x-3 && j==y) || (i==x-2 && j==y) || (i==x-1 && j==y) || (i==x && j==y))
					continue;
				
				range[k] = new Vector2(i, j);
				k++;		
			}
		}
		return range;
	}
	
	public Vector2[] getTorpedoRange(Vector2 pos, ShipDirection direction) {
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[22];
		int k = 0;
		
		if (direction == ShipDirection.North){
			x = pos.y;
			y = pos.x;
		}
		
		if (direction == ShipDirection.West) {
			x = pos.x + 2;
		}
		
		if (direction == ShipDirection.South) {
			x = pos.y;
			y = pos.x + 2;
		}
		
		for(int i = x - 2; i <= x + 2; i++) {
			for(int j = y - 2; j <= y + 2; j++) {
				if ((i==x-2 && j==y) || (i==x-1 && j==y) || (i==x && j==y))
					continue;
				
				range[k] = new Vector2(i, j);
				k++;		
				}
			}
		return range;
	}
	
	public Vector2[] getMinelayerRange(Vector2 pos, ShipDirection direction) {
		//default
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[18];
		int k = 0;
		
		if (direction == ShipDirection.North){
			x = pos.y;
			y = pos.x;
		}
		
		if (direction == ShipDirection.West) {
			x = pos.x + 1;
		}
		
		if (direction == ShipDirection.South) {
			x = pos.y;
			y = pos.x + 1;
		}
		
		for(int i = x - 2; i <= x + 1; i++) {
			for(int j = y - 2; j <= y + 2; j++) {
				if ((i==x-1 && j==y) || (i==x && j==y))
					continue;
				
				range[k] = new Vector2(i, j);
				k++;		
			}
		}
		return range;
	}
	
	public Vector2[] getRadarboatRange(Vector2 pos, ShipDirection direction) {
		//default
		int x = pos.x;
		int y = pos.y;
		
		Vector2[] range = new Vector2[12];
		int k = 0;
		
		if (direction == ShipDirection.North){
			x = pos.y;
			y = pos.x;
		}
		
		if (direction == ShipDirection.West) {
			x = pos.x + 2;
		}
		
		if (direction == ShipDirection.South) {
			x = pos.y;
			y = pos.x + 2;
		}
		
		for(int i = x - 3; i <= x + 1; i++) {
			for(int j = y - 1; j <= y + 1; j++) {
				if ((i==x-2 && j==y) || (i==x-1 && j==y) || (i==x && j==y))
					continue;
				
				range[k] = new Vector2(i, j);
				k++;		
			}
		}
		return range;
	}
}
