/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 */
public class Base {
    
    private final int playerID;
    private final int size = 10;
    private int sizeRemaining;
    private BaseUnit[] baseUnits;
    
    public Base(int pid) {
        this.playerID = pid;
        this.sizeRemaining = size;
        BaseUnit[] bu = new BaseUnit[this.size];
        for (int i = 0; i < this.size; i++){
            BaseUnit bUnit = new BaseUnit(this);
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
}
