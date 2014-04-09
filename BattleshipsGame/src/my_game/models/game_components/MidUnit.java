/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.models.game_components.GameObject.GameObjectType;

/**
 * 
 * @author wei
 */
public class MidUnit extends ShipUnit {
	
	public MidUnit(Ship ship) {
		this.ship = ship;
		this.damageLevel = 0;
		this.unitArmour = ship.getArmour();
		this.type = GameObjectType.MidUnit;
	}

	public MidUnit() {
		super();
	}
}
