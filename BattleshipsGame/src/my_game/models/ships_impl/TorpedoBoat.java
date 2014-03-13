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
   
    public int getTorpedoDamage() {
    	return torpedoDamage;
    }
	
	void setTorpedoDamage(int torpedoDamage) {
    	this.torpedoDamage = torpedoDamage;
    }
    
    public void fireTorpedo(GameObject target) {
    	if (target.getClass() == new ShipUnit().getClass()){
			Ship tempShip = ((ShipUnit)target).getShip();
			
			((ShipUnit)target).setDamage(getTorpedoDamage());
			ShipDirection dire = tempShip.getDirection();
			
			if (((ShipUnit)target).isDestoryed()){	
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
						if (neighbor.isDestoryed()){	
							tempShip.hitUpdate();
	    				}
					}	
				}
				
				if (full == 0 && index + 1 < units.size()){
					ShipUnit neighbor = units.get(index + 1);  
					if (neighbor != null) {
						neighbor.setDamage(getTorpedoDamage());
						if (neighbor.isDestoryed()){	
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
