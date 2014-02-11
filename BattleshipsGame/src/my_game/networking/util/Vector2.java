package my_game.networking.util;

public class Vector2 {

	public int x;
	public int y;
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

    public Vector2(Vector2 position) {
        this.x = position.x;
        this.y = position.y;
    }
}
