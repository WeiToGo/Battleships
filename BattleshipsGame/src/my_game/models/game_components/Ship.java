/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 */
public abstract class Ship {
    
    public enum ShipType {
        Cruiser, Destroyer, TorpedoBoat, MineLayer, RadarBoat
    };
    
    /** The type of this ship. */
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
    private final Weapon[] availableWeapons;
    
    public Ship(){
    
    }
    
    /**
     * @return The type of this ship.
     */
    public ShipType getShipType() {
        return this.shipType;
    }
}
