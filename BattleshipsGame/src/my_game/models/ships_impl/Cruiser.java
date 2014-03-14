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
import my_game.models.game_components.ShipDirection;
import my_game.models.game_components.ShipUnit;
import my_game.util.Range;
import my_game.util.Vector2;


/**
 *
 */
public class Cruiser extends Ship {
	
    public Cruiser(int pid) {
    	super(pid);    
    }

    public Cruiser(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.Cruiser);
        setSize(5);
        setSpeed(10);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
        setCannonDamage(2);
        setDirection(direction);
              
        weapons.add("cannon");
        Range cr = new Range(new Vector2(-9,-5), new Vector2(5,-5), 
            new Vector2(5,5), new Vector2(-9,5));   
        setCannonRange(cr);
        Range rr = new Range(new Vector2(-3,-1), new Vector2(6,-1), 
              new Vector2(6,1), new Vector2(-3,1));       
        setRadarRange(rr);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        moveTo(position);  
    }
    
}
