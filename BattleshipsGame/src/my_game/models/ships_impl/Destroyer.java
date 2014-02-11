/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.ships_impl;

import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipUnit;
import my_game.models.game_components.Weapon;

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
        this.radarRange = new int[] {0,0,0};
        this.canonRange = new int[] {0,0,0};
  // needs to see how specific weapon is implemented.  
  //    this.availableWeapons = new Weapons[] ;
       
    }
}
