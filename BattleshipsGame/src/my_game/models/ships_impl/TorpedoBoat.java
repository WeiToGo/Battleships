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
public class TorpedoBoat extends Ship {
    private int cannonDamage;
    private int torpedoDamage;
    
    public TorpedoBoat(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.TorpedoBoat);
        setSize(3);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        
        setSpeed(9);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(1);
        setCannonDamage(1);
        setTorpedoDamage(1);
        setDirection(direction);
        moveTo(position);        
        weapons.add("cannon");
        weapons.add("torpedo");
        
        Range cr = new Range(new Vector2(-2,-2), new Vector2(2,-2), 
            new Vector2(2,2), new Vector2(-2,2));   
        setCannonRange(cr);
        
        Range tr = new Range(new Vector2(0,1), new Vector2(0,11),
        		new Vector2(0,11), new Vector2(0,1));
        setTorpedoRange(tr);
        
        Range rr = new Range(new Vector2(-1,-1), new Vector2(4,-1), 
              new Vector2(4,1), new Vector2(-1,1));       
        setRadarRange(rr);
     }

    public TorpedoBoat(int pid) {
		super(pid);
	}

	public int getCannonDamage() {
    	return cannonDamage;
    }

    void setCannonDamage(int cannonDamage) {
    	this.cannonDamage = cannonDamage;
    }
   
    public int getTorpedoDamage() {
    	return torpedoDamage;
    }
	
	void setTorpedoDamage(int torpedoDamage) {
    	this.torpedoDamage = torpedoDamage;
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
    
    public boolean fireTorpedo(GameObject target) {
        throw new UnsupportedOperationException("Not yet implemented");
    }    


    
}
