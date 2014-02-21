package my_game.util;

import my_game.models.game_components.Ship.ShipDirection;

public class Range {
	
	private int width; //perpendicular to  ship
	private int length; //parallel to ship
	private Vector2 pos; //the position of a Ship's bow
	private ShipDirection direction;
	
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Vector2 getPos() {
		return pos;
	}
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	public ShipDirection getDirection() {
		return direction;
	}
	public void setDirection(ShipDirection direction) {
		this.direction = direction;
	}
	
}
