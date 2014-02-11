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
public class TorpedoBoat extends Ship {
        public TorpedoBoat(int pid){
        super(pid);
        this.shipType = ShipType.MineLayer;
        this.size = 3;
        ShipUnit[] su = new ShipUnit[this.size];
        for (int i = 0; i < this.size; i++){
            ShipUnit sUnit = new ShipUnit(this);
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.speed = 9;
        this.currentSize = size;
        this.currentSpeed = speed;
        this.heavyArmour = false;
        //to be set
        this.radarRange = new int[] {0,0,0};
        this.canonRange = new int[] {0,0,0}; 
  // needs to see how specific weapon is implemented.  
  //    this.availableWeapons = new Weapons[] ;
       
    }
    
}
