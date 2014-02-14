/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.Weapon;
import my_game.util.Vector2;


/**
 *
 */
public class RadarBoat extends Ship {
        public RadarBoat(int pid){
        super(pid);
        this.shipType = ShipType.MineLayer;
        this.size = 3;
        ShipUnit[] su = new ShipUnit[this.size];
        for (int i = 0; i < this.size; i++){
            ShipUnit sUnit = new ShipUnit(this);
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.speed = 3;
        this.currentSize = size;
        this.currentSpeed = speed;
        this.heavyArmour = false;
        //to be set
     }

   
    public Vector2[] getRadarRange(){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vector2[] getCanonRange(){
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
