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

    private boolean isDestroyed;
    private Base base;
    
    
    public BaseUnit(Base b) {
        this.base = b;
        this.type = GameObjectType.Base;
        this.isDestroyed = false;
        // TODO complete
    }
    
    public void setDestroyed(){
        this.isDestroyed = true;
    }
    
    public boolean isDestroyed(){
        return isDestroyed;
    }
}
