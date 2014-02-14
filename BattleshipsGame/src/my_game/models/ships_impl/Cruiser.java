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
public class Cruiser extends Ship {
    
    public Cruiser(int pid){
        super(pid);
        this.shipType = ShipType.Cruiser;
        this.size = 5;
        ShipUnit[] su = new ShipUnit[this.size];
        for (int i = 0; i < this.size; i++){
            ShipUnit sUnit = new ShipUnit(this);
            su[i] = sUnit;
        }
        this.shipUnits = su;
        this.speed = 10;
        this.currentSize = this.size;
        this.currentSpeed = this.speed;
        this.heavyArmour = true;
        // TO DO:
        //Weapon[] w = new Weapon();
        //this.availableWeapons = new Weapons[] ;       
    }
    
/*    public Vector2[] availableMoves(){
        ShipUnit[] shipUnits = this.getShipUnits();
        //assuming the the 1st square of the ship is the 1st element 
        //in this array.
        Vector2 tipPosition = shipUnits[0].getPosition();
        for (ShipUnit s: shipUnits){
        
    }

    public abstract Vector2[] availableTurns(){
    
    }
    
    public abstract Vector2[] getRadarRange(){
        
    }

    public abstract Vector2[] getCanonRange(){
        
    }
*/
}
