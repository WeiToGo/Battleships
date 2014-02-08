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
    private ShipType shipType;
    private final ShipUnit[] shipUnits;
    private final int playerID;
    private final int size;
    private final int speed;
    private int currentSize;
    private int currentSpeed;
    private final boolean heavyArmour;
    private final int[] radarRange;
    private final int[] canonRange;
 //   private final Weapon[] availableWeapons = new Weapon[1];
    private final int[] availableWeapons;
    
    public Cruiser(int pid){
        this.shipType = ShipType.Cruiser;
        ShipUnit[] su = new ShipUnit[5];
        for (int i = 0; i < 5; i++){
            ShipUnit sUnit = new ShipUnit();
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.playerID = pid;
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
