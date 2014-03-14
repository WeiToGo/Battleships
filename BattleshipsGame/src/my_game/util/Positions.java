
package my_game.util;
import java.util.ArrayList;
/**
 *
 * @author wei
 */
public class Positions {
    public ArrayList<Vector2> backward;
    public ArrayList<Vector2> forward;
    public ArrayList<Vector2> left;
    public ArrayList<Vector2> right;
   
    
    public Positions(ArrayList<Vector2> b, ArrayList<Vector2> f, 
            ArrayList<Vector2> l, ArrayList<Vector2> r){
        this.backward = b;
        this.forward = f;
        this.left = l;
        this.right = r;
    }
    
    public ArrayList<Vector2> getAll() {
        ArrayList<Vector2> list = new ArrayList<Vector2>();
        ArrayList<Vector2> f = getForward();
        ArrayList<Vector2> b = getBackward();
        ArrayList<Vector2> l = getLeft();
        ArrayList<Vector2> r = getRight();
        if(f != null) {
            list.addAll(f);
        }
        if(b != null) {
            list.addAll(b);
        }
        if(l != null) {
            list.addAll(l);
        }
        if(r != null) {
            list.addAll(r);
        }
        return list;
    }
    
    public ArrayList<Vector2> getBackward(){
        return this.backward;
    }
    public ArrayList<Vector2> getForward(){
        return this.forward;
    }
    public ArrayList<Vector2> getLeft(){
        return this.left;
    }
    public ArrayList<Vector2> getRight(){
        return this.right;
    }
    public void setBack(ArrayList<Vector2> p){
        this.backward = p;
    }
    public void setForward(ArrayList<Vector2> p){
        this.forward = p;
    }
    public void setLeft(ArrayList<Vector2> p){
        this.left = p;
    }
    public void setRight(ArrayList<Vector2> p){
        this.right = p;
    }
}
