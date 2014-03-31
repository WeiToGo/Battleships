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
public class ShipUnit extends GameObject implements java.io.Serializable {
    public int damageLevel;
    public int unitArmour;
    private Vector2 position;
    private Ship ship;
    
    public ShipUnit() {
		super();
	}

	public ShipUnit(Ship ship){
            super();
            
          this.ship = ship;
          this.type = GameObjectType.Ship;
          this.damageLevel = 0;
          this.unitArmour = ship.getArmour(); 
    }
    
    public Ship getShip() {
        return ship;
    }
    
    /**
     * @return True if this ShipUnit is the bow of the ship that it belongs to,
     * otherwise returns false.
     */
    public boolean isBow() {
        return (this.ship.getShipUnits()[0] == this);
    }
    
    public Vector2 getPosition(){
        Vector2 posCopy = new Vector2(this.position);
        return posCopy;
    }
    
    public void setPosition(Vector2 newPosition){
//        position.x = newPosition.x;
//        position.y = newPosition.y;
        position = newPosition;
    }
    
    public int getDamageLevel(){
        return damageLevel;
    }
    
    public void setDamage(int damage){
            this.damageLevel = this.damageLevel + damage;
    }
    
    public boolean isHealthy() {
    	return damageLevel == 0;
    }
    
    public boolean isDamaged() {
    	return (unitArmour - damageLevel) == 1 && damageLevel > 0;
    }
    
    public boolean isDestroyed() {
    	return unitArmour <= damageLevel;
    }
}
