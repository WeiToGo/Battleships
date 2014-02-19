/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.util.Vector2;

/**
 *
 */
public abstract class Weapon {
    
    protected int damageLevel;
	
	public int getDamageLevel() {
		return damageLevel;
	}
	public void setDamageLevel(int damageLevel) {
		this.damageLevel = damageLevel;
	}
    
    
    
}
