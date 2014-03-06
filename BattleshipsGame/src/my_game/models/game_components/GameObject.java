/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 * Every object belonging to/shown onto the game map is 
 * a game object.
 */
public abstract class GameObject implements java.io.Serializable {
    public enum GameObjectType {
        Ship, Mine, CoralReef, Base
    }
    
    /** The type of this game object. */
    GameObjectType type;
    
    /**
     * @return The type of this game object.
     */
    public GameObjectType getObjectType() {
        return this.type;
    }
    
    // may not be necessary if each gameobject implement it differently. 
 /*   public abstract void setDamage();
    
    public abstract int getDamageLevel();
 */   
}
