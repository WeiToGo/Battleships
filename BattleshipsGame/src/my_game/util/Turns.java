/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.util;
import java.util.ArrayList;
/**
 *
 * @author wei
 */
public class Turns {

    public ArrayList<Vector2> positions;
    public ArrayList<Vector2> path;
    public ShipDirection newDirection;
      
    public Turns(){
        
    }

    public ArrayList<Vector2> getTurns(){
        return this.positions;
    }
    public void setTurns(ArrayList<Vector2> moves){
        this.positions = moves;
    }
    public ArrayList<Vector2> getPath(){
        return this.positions;
    }
    public void setPath(ArrayList<Vector2> p){
        this.path = p;
    }
    public ShipDirection getNewDirection(){
        return this.newDirection;
    }
    public void setNewDirection(ShipDirection d){
        this.newDirection = d;
    }     
}