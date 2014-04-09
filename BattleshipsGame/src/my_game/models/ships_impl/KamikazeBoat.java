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
    private boolean activeAttack;
    
    public KamikazeBoat(int pid) {
    	super(pid);    
    }

    public KamikazeBoat(int pid, ArrayList<Vector2> position, ShipDirection d){
        super(pid);
        setShipType(Ship.ShipType.KamikazeBoat);
        setSize(1);
        setSpeed(2);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
        // set to east because it needs to have a valid direction to use the range methods.
        setDirection(d);
        activeAttack = false;
              
      //  weapons.add("cannon");
        Range rr = new Range(new Vector2(-2,-2), new Vector2(2,-2), 
            new Vector2(2,2), new Vector2(-2,2));   
        setRadarRange(rr);
        Range er = new Range(new Vector2(-1,-1), new Vector2(1,-1), 
              new Vector2(1,1), new Vector2(-1,1));       
        setCannonRange(er); //name to cannonrange so I can use method in Ship.
        
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
    public Range getExplosionRange() {
    	return explosionRange;
    } 
    
    public void activateAttack(){
        if (!activeAttack){
            activeAttack = true;
        }
    }
    
    public void suicideAttack(){
        if (this.getCurrentSize() > 0){
            ShipUnit su = this.getShipUnits()[0];
            su.setDamageLevel(2); // may not be the best way to do it.
        }
    }
  
}
