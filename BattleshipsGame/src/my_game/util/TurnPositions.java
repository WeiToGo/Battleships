
package my_game.util;
import java.util.ArrayList;
/**
 * This class contains 5 arraylists of positions involved when turning a ship.
 * @author wei
 */
public class TurnPositions {
    /** The ship positions if we are turning left 90 deg. */
    public ArrayList<Vector2> left;
    /** The positions the ship has to pass when turning left. They won't be 
     * highlighted on the map but Map needs to check these positions. */
    public ArrayList<Vector2> leftPath;
    public ArrayList<Vector2> right;
    public ArrayList<Vector2> rightPath;
    public ArrayList<Vector2> backward;
    /**We don't need backPath because the path the ship needs to traverse when 
    it's turning 180 deg, is the same than leftPath + rightPath. */
   
    
    public TurnPositions(ArrayList<Vector2> l, ArrayList<Vector2> lp, 
            ArrayList<Vector2> r, ArrayList<Vector2> rp, ArrayList<Vector2> back){

        this.left = l;
        this.leftPath = lp;
        this.right = r;
        this.rightPath = rp;
        this.backward = back;
    }
    
    public ArrayList<Vector2> getLeft(){
        return this.left;
    }
    public ArrayList<Vector2> getLeftPath(){
        return this.leftPath;
    }
    public ArrayList<Vector2> getRight(){
        return this.right;
    }
    public ArrayList<Vector2> getRightPath(){
        return this.rightPath;
    }
    public ArrayList<Vector2> getBackward(){
        return this.backward;
    }
    public void setLeft(ArrayList<Vector2> p){
        this.left = p;
    }
    public void setLeftPath(ArrayList<Vector2> p){
        this.leftPath = p;
    }
    public void setRight(ArrayList<Vector2> p){
        this.right = p;
    }
    public void setRightPath(ArrayList<Vector2> p){
        this.rightPath = p;
    }
    public void setBack(ArrayList<Vector2> p){
        this.backward = p;
    }
}