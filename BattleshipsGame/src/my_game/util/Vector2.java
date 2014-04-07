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
    
    /**
     * this = this - v
     * @param v 
     */
    public void sub(Vector2 v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
    }
    
    @Override
    public String toString() {
        return x + "," + y;
    }

    public float getLength() {
        return (float) Math.sqrt((x*x) + (y*y));
    }
}
