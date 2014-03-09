/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import java.util.ArrayList;
import my_game.util.Vector2;
/**
 *
 */
public class Base implements java.io.Serializable {
    
    private final int playerID;
    private final int size = 10;
    private int sizeRemaining;
    private BaseUnit[] baseUnits;
    
    public Base(int pid) {
        this.playerID = pid;
        this.sizeRemaining = size;
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        int i,j;
        if (pid == 0){
            i = 0;
        }else{
            i = 29;
        }
        for (j = 10; j < 20; j++){
            Vector2 v = new Vector2(j,j);
            positions.add(v);
        }

        BaseUnit[] bu = new BaseUnit[this.size];
        for (i = 0; i < this.size; i++){
            BaseUnit bUnit = new BaseUnit(this, positions.get(i));
            bu[i] = bUnit;
        }
        this.baseUnits = bu;
    }
    
    public int getRemainingSize(){

        for (BaseUnit unit: baseUnits){
            if (unit.getDamageLevel() == 1){
                sizeRemaining--;
            }
        }
        return sizeRemaining;
    }
    
    /** This method is called everytime a baseUnit is destroyed.*/
    public void updateSize(){
        if (!this.isDestroyed()){
            sizeRemaining--;
        }else{
            // exception?
        }
    }
    
    public boolean isDestroyed(){
        return false;
    }
    
    public BaseUnit[] getBaseUnits(){
        return baseUnits;
    }
}
