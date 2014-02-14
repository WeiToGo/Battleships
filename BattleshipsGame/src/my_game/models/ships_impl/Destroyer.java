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
public class Destroyer extends Ship{
    
    public Destroyer(int pid){
        super(pid);
        this.shipType = ShipType.Destroyer;
        this.size = 4;
        ShipUnit[] su = new ShipUnit[this.size];
        for (int i = 0; i < this.size; i++){
            ShipUnit sUnit = new ShipUnit(this);
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.speed = 8;
        this.currentSize = this.size;
        this.currentSpeed = this.speed;
        this.heavyArmour = false;
     }

   
    public Vector2[] getRadarRange(){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vector2[] getCanonRange(){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
