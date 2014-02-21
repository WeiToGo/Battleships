/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import java.util.ArrayList;

import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.Weapon;
import my_game.util.Vector2;


/**
 *
 */
public class Cruiser extends Ship {
	
    public Cruiser(int pid, Vector2[] position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.Cruiser);
        setSize(5);
        
        ShipUnit[] tempShipUnits = new ShipUnit[this.size];
        for (int i = 0; i < this.size; i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        
        setSpeed(10);
        setCurrentSize(size);
        setCurrentSpeed(speed);
        setArmour(2);
        setDirection(direction);
        moveTo(position);
        
        weapons.add("cannon");
        
    }
    
    

   
    

}
