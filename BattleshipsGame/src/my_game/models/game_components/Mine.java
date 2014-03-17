package my_game.models.game_components;

import my_game.util.Vector2;


public class Mine extends GameObject {
	
	private boolean active;
	private boolean destoryed;
	private Vector2 position;

	public Mine() {
		super();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public boolean isDestoryed() {
		return destoryed;
	}

	public void setDestoryed(boolean destoryed) {
		this.destoryed = destoryed;
	}
	
	public Vector2[] getMineZone() {
		int x = position.x;
		int y = position.y;
		
		Vector2[] zone =  {new Vector2(x, y-1), new Vector2(x, y+1), new Vector2(x-1, y), new Vector2(x+1, y)};
		
		return zone;
	}
}
