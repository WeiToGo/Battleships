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
public class TorpedoBoat extends Ship {
    private int torpedoDamage;
    
    public TorpedoBoat(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.TorpedoBoat);
        setSize(3);
        
        setSpeed(9);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(1);
        setCannonDamage(1);
        setTorpedoDamage(1);
        setDirection(direction);
       
        weapons.add("cannon");
        weapons.add("torpedo");
        
        Range cr = new Range(new Vector2(-2,-2), new Vector2(2,-2), 
            new Vector2(2,2), new Vector2(-2,2));   
        setCannonRange(cr);
        
    /*    Range tr = new Range(new Vector2(0,1), new Vector2(0,11),
        		new Vector2(0,11), new Vector2(0,1));
        setTorpedoRange(tr);
    */    
        Range rr = new Range(new Vector2(-1,-1), new Vector2(4,-1), 
              new Vector2(4,1), new Vector2(-1,1));       
        setRadarRange(rr);
                
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
         moveTo(position);        
     }

    public TorpedoBoat(int pid) {
		super(pid);
    }
   
    public int getTorpedoDamage() {
    	return torpedoDamage;
    }
	
	void setTorpedoDamage(int torpedoDamage) {
    	this.torpedoDamage = torpedoDamage;
    }
    
	
	/**
	 * There are three sub cases: target is ShipUnit, BaseUnit or Mine.
	 * For ShipUnit case the function will first check if it hits from the side(another closed unit will be hit)
	 * For mine case it will change the indicator in Mine that it is destroyed 
	 * @param target = the target GameObject 
	 */
    public void fireTorpedo(GameObject target) {
    	if (target.getClass() == new ShipUnit().getClass()
    		|| target.getClass() == new MidUnit().getClass()){
			
    		Ship tempShip = null;
    		
    		if(target.getClass() == new ShipUnit().getClass()){
    			tempShip = ((ShipUnit)target).getShip();
    		}
    	    if(target.getClass() == new MidUnit().getClass()){
    	    	tempShip = ((MidUnit)target).getShip();
    	    }
			
			((ShipUnit)target).setDamage(getTorpedoDamage());
			ShipDirection dire = tempShip.getDirection();
			
			if (((ShipUnit)target).isDestroyed()){	
				tempShip.hitUpdate();
			}
			
			if (getDirection().ordinal() != dire.ordinal() && 
				getDirection().ordinal() != dire.ordinal() + 2 &&
				getDirection().ordinal() != dire.ordinal() -2 ){
				
				ShipUnit[] temp = tempShip.getShipUnits();
				ArrayList<ShipUnit> units = new ArrayList<ShipUnit>();
				for(int i = 0; i < temp.length; i++){
					units.add(temp[i]);
				}
				
				int index = units.indexOf((ShipUnit)target);
				int full = 0;
				
				if (index - 1 >= 0){
					ShipUnit neighbor = units.get(index - 1);  
					if (neighbor != null) {
						full++;
						neighbor.setDamage(getTorpedoDamage());
						if (neighbor.isDestroyed()){	
							tempShip.hitUpdate();
	    				}
					}	
				}
				
				if (full == 0 && index + 1 < units.size()){
					ShipUnit neighbor = units.get(index + 1);  
					if (neighbor != null) {
						neighbor.setDamage(getTorpedoDamage());
						if (neighbor.isDestroyed()){	
							tempShip.hitUpdate();
	    				}
					}
				}	
			}
		}
		
		if (target.getClass() == new BaseUnit().getClass()){
			((BaseUnit)target).setDamage();
		}
				
		if (target.getClass() == new Mine().getClass()){
			((Mine)target).setDestoryed(true);
		}
    }  
    
}
