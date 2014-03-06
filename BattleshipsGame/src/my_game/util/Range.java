package my_game.util;

import my_game.models.game_components.ShipDirection;

public class Range {
	
	private int width; //perpendicular to  ship
	private int height; //parallel to ship
	private Vector2 pos; //the position of a Ship's bow
	private ShipDirection direction;
	
	
	public Range(int width, int height, Vector2 pos, ShipDirection direction) {
		super();
		this.width = width;
		this.height = height;
		this.pos = pos;
		this.direction = direction;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
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
