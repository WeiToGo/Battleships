package my_game.util;

import my_game.models.game_components.ShipDirection;

public class Range {
	
	private Vector2 topLeft; 
	private Vector2 topRight; 
        private Vector2 bottomRight;        
	private Vector2 bottomLeft; 

        
        public Range(Vector2 tl, Vector2 tr, Vector2 br, Vector2 bl){
            this.topLeft = tl;
            this.topRight = tr;
            this.bottomRight = br;
            this.bottomLeft = bl;
        }
        
        public Vector2 getTopLeft(){
            return this.topLeft;
        }
        public Vector2 getTopRight(){
            return this.topRight;
        }
        public Vector2 getBottomRight(){
            return this.bottomRight;
        }           
        public Vector2 getBottomLeft(){
            return this.bottomLeft;
        }
     
}






/*
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
*/