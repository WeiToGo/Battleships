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
import my_game.util.Range;

/**
 *
 */
public class MineLayer extends Ship {
    
    private int cannonDamage;
    
    public MineLayer(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.MineLayer);
        setSize(2);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        
        setSpeed(6);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
        setCannonDamage(1);
        setDirection(direction);
        moveTo(position);        
        weapons.add("cannon");
        weapons.add("mine");
        Range cr = new Range(new Vector2(-2,-2), new Vector2(1,-2), 
            new Vector2(1,2), new Vector2(-2,2));   
        setCannonRange(cr);
        Range rr = new Range(new Vector2(-3,-2), new Vector2(2,-2), 
              new Vector2(2,2), new Vector2(-3,2));       
        setRadarRange(rr);
     }
    
    public MineLayer(int pid) {
		super(pid);
	}
   
    
    public boolean layMine(GameObject target) {
        throw new UnsupportedOperationException("Not yet implemented");
    }  
    
}
