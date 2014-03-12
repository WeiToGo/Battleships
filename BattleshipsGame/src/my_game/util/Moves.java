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
public class Moves {

    private ArrayList<Vector2> positions;
    private MoveDirection md;
    // to keep track of which way we are moving, initially set to All.
    public enum MoveDirection {
        L, R, F, B
    };
    
    public Moves(){
        
    }
    public MoveDirection getMoveDirection(){
        return this.md;
    }
    public void setMoveDirection(MoveDirection moveDirection){
        this.md = moveDirection;
    }
    public ArrayList<Vector2> getPositions(){
        return this.positions;
    }
    public void setMoves(ArrayList<Vector2> moves){
        this.positions = moves;
    }
}
