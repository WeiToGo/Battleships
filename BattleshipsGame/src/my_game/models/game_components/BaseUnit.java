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
    
    
    public BaseUnit() {
        this.type = GameObjectType.Base;
        // TODO complete
    }
    
    public void setDestroyed(){
        
    }
    
    public boolean isDestroyed(){
        return isDestroyed;
    }
}
