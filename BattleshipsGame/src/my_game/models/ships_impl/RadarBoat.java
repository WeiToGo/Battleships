/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import java.util.ArrayList;

import my_game.models.game_components.BaseUnit;
import my_game.models.game_components.CoralUnit;
import my_game.models.game_components.GameObject;
import my_game.models.game_components.MidUnit;
import my_game.models.game_components.Mine;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.util.Range;
import my_game.util.ShipDirection;
import my_game.util.Vector2;
import my_game.util.Range;


/**
 *
 */
public class RadarBoat extends Ship {
    private int cannonDamage;
    private boolean longRangeActive;
    private Range shortRange;
    private Range longRange;
 //   private Range longRange;
    
    public RadarBoat(int pid) {
		super(pid);
	}

	public RadarBoat(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.RadarBoat);
        setSize(3);        
        setSpeed(3);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(1);
        setCannonDamage(1);
        setDirection(direction);
        
        weapons.add("cannon");
        Range cr = new Range(new Vector2(-3,-1), new Vector2(1,-1), 
            new Vector2(1,1), new Vector2(-3,1));   
        setCannonRange(cr);
        shortRange = new Range(new Vector2(-1,-1), new Vector2(4,-1), 
              new Vector2(4,1), new Vector2(-1,1));       
        setRadarRange(shortRange);
        longRange = new Range(new Vector2(-1,-1), new Vector2(10,-1), 
              new Vector2(10,1), new Vector2(-1,1));       
    //    setLongRadarRange(lrr); 
        
                
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        tempShipUnits[1] = new MidUnit(this);
        setShipUnits(tempShipUnits);
        moveTo(position);        
     }
    
    public void setLongRadarRange(Range r){
        this.longRange = r;
    }    
 
    public void turnOnLongRangeRadar(){
        if (!longRangeActive){
             this.setSpeed(0);
             longRangeActive = true;
             setRadarRange(longRange);
        }
    }
    
    public void turnOffLongRangeRadar(){
        if(longRangeActive){
           this.setSpeed(3);
           longRangeActive = false;
           setRadarRange(shortRange);
        }
    }
 
}
