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
    protected ShipType shipType;
    protected ShipUnit[] shipUnits;
    protected final int playerID;
    protected int size;
    protected int speed;
    protected int currentSize;
    protected int currentSpeed;
    protected boolean heavyArmour;
    protected int[] radarRange;
    protected int[] canonRange;
    protected Weapon[] availableWeapons;

    
    /** Constructs a ship given a player ID. */
    public Ship(int pid){
        this.playerID = pid;
    }
    
    /**
     * @return The type of this ship.
     */
    public ShipType getShipType() {
        return this.shipType;
    }
    /**
     * This method is called so Map can calculate all the legal moves for this 
     * ship.
     * @return 
     */
    public int getCurrentSpeed(){
        return currentSpeed;
    }
    /**
     * This method updates each ShipUnit to the new position.
     * @param x The x-coordinate of the final position of the bow of the ship.
     * @param y The y-coordinate of the final position of the bow of the ship.
     */
    public void moveTo(int x, int y){
        
    }
    
    /**
     * This method return true if the ship can turn 180 degree in one turn
     * (Torpedoand radar boat)
     * 
     * @return 
     */
    public boolean hasFlexibleTurn(){
        return false;
    }
    
    /**
     * This method updates each ShipUnit to the new position.
     * @param x The x-coordinate of the final position of the bow of the ship.
     * @param y The y-coordinate of the final position of the bow of the ship.
     */
    public void turnTo(int x, int y) {
        
    }
    
    /**
     * 
     * @return array of 3 ints, indicating the number of square to the left/right,
     * front, and back??
     */
    public int[] getRadarRange(){
        return radarRange;       
    }
    
    /**
     * 
     *@return array of 3 ints, indicating the number of square to the left/right,
     * front, and back. 
     */
    public int[] getCanonRange(){
        return canonRange;
    }
}
