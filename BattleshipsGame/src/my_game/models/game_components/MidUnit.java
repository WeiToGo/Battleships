/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 * @author wei
 */
public class MidUnit extends ShipUnit{
        public MidUnit(Ship s) {
            super();
            this.ship = s;
            this.type = GameObjectType.MidUnit;
            this.damageLevel = 0;
            this.unitArmour = s.getArmour();                 
	}
}
