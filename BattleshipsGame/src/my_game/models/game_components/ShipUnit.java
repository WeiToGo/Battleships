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
    private int[] position;
    private Ship ship;
    
    public ShipUnit(Ship ship){
        
          this.ship = ship;
          this.type = GameObjectType.Ship;
          this.damageLevel = 0;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public int[] getPosition(){
        return position;
    }
    
    public void setPosition(int x, int y){
       // error checking?
        position[0] = x;
        position[1] = y;
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
