package my_game.models.game_components;

import my_game.util.Vector2;

public class MineZone {
	private boolean active;
	private Vector2 position;
	private Mine mine;
	
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
	public Mine getMine() {
		return mine;
	}
	public void setMine(Mine mine) {
		this.mine = mine;
	}
	
	public MineZone(boolean active, Vector2 position, Mine mine) {
		super();
		this.active = active;
		this.position = position;
		this.mine = mine;
	}
	
}
