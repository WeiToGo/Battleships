package my_game.util;

public class Vector2 implements java.io.Serializable {

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
    
    public boolean equals(Vector2 v){
        if (this.x == v.x && this.y == v.y){
            return true;
        }else{
            return false;
        }
    }
}
