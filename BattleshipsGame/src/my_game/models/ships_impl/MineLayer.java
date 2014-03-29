/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import java.util.ArrayList;

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
public class MineLayer extends Ship {
    
	private int mineNum;
    
    public MineLayer(int pid, ArrayList<Vector2> position, ShipDirection direction){
        super(pid);
        setShipType(ShipType.MineLayer);
        setSize(2);
        setSpeed(6);
        setCurrentSize(getSize());
        setCurrentSpeed(getSpeed());
        setArmour(2);
        setCannonDamage(1);
        setDirection(direction);
        
        weapons.add("cannon");
        weapons.add("mine");
        Range cr = new Range(new Vector2(-2,-2), new Vector2(1,-2), 
            new Vector2(1,2), new Vector2(-2,2));   
        setCannonRange(cr);
        Range rr = new Range(new Vector2(-3,-2), new Vector2(2,-2), 
              new Vector2(2,2), new Vector2(-3,2));       
        setRadarRange(rr);
        
        ShipUnit[] tempShipUnits = new ShipUnit[getSize()];
        for (int i = 0; i < getSize(); i++){
            tempShipUnits[i] = new ShipUnit(this);
        }
        setShipUnits(tempShipUnits);
        moveTo(position);        
     }
    
    public MineLayer(int pid) {
		super(pid);
	}

    public Vector2[] getMineDropPickupZone() {
        Vector2 pos = this.getShipUnits()[0].getPosition();
    	int x = pos.x;
        int y = pos.y;
	Vector2[] range = new Vector2[6];
	ShipDirection direction = this.getDirection(); 
		
	if (direction == ShipDirection.North || direction == ShipDirection.East){
            if (direction == ShipDirection.North){
		x = pos.y;
		y = pos.x;
            }
		
            range[0] = new Vector2(x-1, y-1) ;
            range[1] = new Vector2(x, y-1) ;
            range[2] = new Vector2(x-2, y) ;
            range[3] = new Vector2(x+1, y) ;
            range[4] = new Vector2(x-1, y+1) ;
            range[5] = new Vector2(x, y+1) ;
	}else{
            if (direction == ShipDirection.South){
                x = pos.y;
		y = pos.x;
            }		
	
            range[0] = new Vector2(x+1, y-1) ;
            range[1] = new Vector2(x, y-1) ;
            range[2] = new Vector2(x+2, y) ;
            range[3] = new Vector2(x-1, y) ;
            range[4] = new Vector2(x+1, y+1) ;
            range[5] = new Vector2(x, y+1) ;
	}
	return range;
    }
    
    public boolean hasMine() {
    	return mineNum > 0;
    }
    
    public Mine layMine(Vector2 pos) {
        Mine mine = new Mine();
    	mine.setActive(true);
        mine.setPosition(pos);
        if(mineNum > 0) {
        	mineNum--;
        }
        else{
        	mine = null;
        }
        return mine;
    } 
    
    public void pickupMine(Mine mine){
    	mine.setActive(false);
    	mine.setPosition(null);
    	mineNum++;
    }
    
    
}
