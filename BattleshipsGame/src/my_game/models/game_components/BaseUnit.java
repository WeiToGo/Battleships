/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 * @author wei
 */
public class BaseUnit extends GameObject{

    private int damage;
    private Base base;
    
    
    public BaseUnit(Base b) {
        this.base = b;
        this.type = GameObjectType.Base;
        this.damage = 0;
        // TODO complete
    }
    
 /*   public void setDestroyed(){
        this.isDestroyed = true;
    }
 */
    public void setDamage(){
        if (damage == 0){
            damage++;
        }else{
            //already destroyed;
        }
    }
    public int getDamageLevel(){
        return damage;
    }
}
