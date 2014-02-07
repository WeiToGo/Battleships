/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 * @author wei
 */
public class ShipUnit extends GameObject {
    private int damageLevel; // (0: healthy, 1: damaged, 2: destroyed)
    
    public ShipUnit(){
          this.type = GameObjectType.Ship;
          this.damageLevel = 0;
    }
    
    public int getDamageLevel(){
        return damageLevel;
    }
    
    public void setDamage(){
        if (damageLevel < 2){
            damageLevel++;
        }
        
    }
}
