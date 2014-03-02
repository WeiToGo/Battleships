/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import java.util.ArrayList;

import my_game.models.game_components.BaseUnit;
import my_game.models.game_components.CoralUnit;
import my_game.models.game_components.GameObject;
import my_game.models.game_components.Mine;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.Weapon;
import my_game.util.Range;
import my_game.util.Vector2;


/**
 *
 */
public class Cruiser extends Ship {
	
    int cannonDamage;
	
    public int getCannonDamage() {
	return cannonDamage;
    }

    public void setCannonDamage(int cannonDamage) {
	this.cannonDamage = cannonDamage;
    }

    public Cruiser(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.Cruiser);
        setSize(5);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        
        setSpeed(10);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
        setDirection(direction);
        moveTo(position);
        
        weapons.add("cannon");
    }
    
    public Range getRadarRange() {
	return new Range(3, 10, new Vector2(1, 3), getDirection());
    }
	
    public Range getCannonRange() {
    	return new Range(11, 15, new Vector2(5, 9), getDirection());
    }

    public boolean fireCannon(GameObject target) {
        boolean result = false;
	   
        if(target.getClass() == new CoralUnit().getClass()) {
            result = false;
        }
	
        if(target.getClass() == new ShipUnit().getClass() ||
            target.getClass() == new BaseUnit().getClass() ||
            target.getClass() == new Mine().getClass()) {
                result = true;
        }
	   
        return result;
    }
    
}
