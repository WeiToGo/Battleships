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
public class Cruiser extends Ship {
    
    public Cruiser(int pid){
        super(pid);
        this.shipType = ShipType.Cruiser;
        ShipUnit[] su = new ShipUnit[5];
        for (int i = 0; i < 5; i++){
            ShipUnit sUnit = new ShipUnit(this);
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.size = 5;
        this.speed = 10;
        this.currentSize = 5;
        this.currentSpeed = 10;
        this.heavyArmour = true;
        this.radarRange = new int[] {0,0,0};
        this.canonRange = new int[] {0,0,0}; //heavy canon
        this.availableWeapons = new int[] {1};
       
    }
}
