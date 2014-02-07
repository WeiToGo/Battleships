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
    private int sizeRemaining;
    private BaseUnit[] baseUnits;
    
    public Base(int pid, int size, BaseUnit[] units) {
        this.playerID = pid;
        this.sizeRemaining = size;
        this.baseUnits = units;
    }
    
    public int getRemainingSize(){

        for (BaseUnit unit: baseUnits){
            if (unit.isDestroyed()){
                sizeRemaining--;
            }
        }
        return sizeRemaining;
    }
}
