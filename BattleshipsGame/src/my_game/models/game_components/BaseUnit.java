/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import my_game.util.Vector2;


/**
 *
 * @author wei
 */
public class BaseUnit extends GameObject implements java.io.Serializable {

    private int damageLevel; //0: healthy, 1: destroyed
    private Base base;
    private Vector2 position;
    
    public BaseUnit() {
		super();
	}

	public BaseUnit(Base b, Vector2 p) {
        this.base = b;
        this.type = GameObjectType.Base;
        this.damageLevel = 0;
        this.position = p;
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
    
    public boolean isHealthy() {
    	return damageLevel == 0;
    }
    
    public boolean isDestoryed() {
    	return damageLevel == 1;
    }
    
    public Vector2 getPosition(){
        return this.position;
    }
    
    public Base getBase() {
        return this.base;
    }
}
