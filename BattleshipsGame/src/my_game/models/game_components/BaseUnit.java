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

    private int damageLevel; //0: healthy, 1: destroyed
    private Base base;
    
    
    public BaseUnit(Base b) {
        this.base = b;
        this.type = GameObjectType.Base;
        this.damageLevel = 0;
        // TODO complete
    }
    
 /*   public void setDestroyed(){
        this.isDestroyed = true;
    }
 */
    public void setDamage(){
        if (damageLevel == 0)
        	damageLevel++;
    }
    public int getDamageLevel(){
        return damageLevel;
    }
    
    public boolean isHealth() {
    	return damageLevel == 0;
    }
    
    public boolean isDestoryed() {
    	return damageLevel == 1;
    }
}
