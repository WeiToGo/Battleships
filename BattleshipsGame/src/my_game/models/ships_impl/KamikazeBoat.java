/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;
import java.util.ArrayList;

import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.util.Range;
import my_game.util.ShipDirection;
import my_game.util.Vector2;

/**
 *
 * @author wei
 */
public class KamikazeBoat extends Ship{
    
    private Range explosionRange;
    
    public KamikazeBoat(int pid) {
    	super(pid);    
    }

    public KamikazeBoat(int pid, ArrayList<Vector2> position){
        super(pid);
        setShipType(Ship.ShipType.KamikazeBoat);
        setSize(1);
        setSpeed(2);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
       // setCannonDamage(2);
        setDirection(ShipDirection.NoDirection);
              
      //  weapons.add("cannon");
        Range rr = new Range(new Vector2(-2,-2), new Vector2(2,-2), 
            new Vector2(2,2), new Vector2(-2,2));   
        setRadarRange(rr);
        Range er = new Range(new Vector2(-1,-1), new Vector2(1,-1), 
              new Vector2(1,1), new Vector2(-1,1));       
        setExplosionRange(er);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        moveTo(position);  
    }

    public void setExplosionRange(Range r){
        this.explosionRange = r;
    }
    
    
    
}
