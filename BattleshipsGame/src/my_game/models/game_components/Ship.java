/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;
import my_game.util.Vector2;
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
    protected Vector2[] radarRange;
    protected Vector2[] canonRange;
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
    
    public ShipUnit[] getShipUnits(){
        return this.shipUnits;
    }
    
    /**
     * This method updates each ShipUnit to the new position, and it's called for
     * both move and turn actions.
     * @param An array of Vector2 indicating the new potions starting at the bow
     * of the ship.
     */
    public void moveTo(Vector2[] newPosition){
        // assert newPosition.length == shipUnits.length
        // and assuming it's the right order.
        ShipUnit[] shipUnits = this.getShipUnits();
        for (ShipUnit s: shipUnits){
            for (Vector2 v: newPosition){
                s.setPosition(v);
            }
        }
    }
    
    /**
     * This method return true if the ship can turn 180 degree in one turn
     * (Torpedo and radar boat)
     * 
     * @return 
     */
    public boolean hasFlexibleTurn(){
        if (this.shipType.equals(ShipType.TorpedoBoat)){
            return true;
        }else if (this.shipType.equals(ShipType.RadarBoat)){
            return true; 
        }else{
            return false;
        }
    }
    
 
    /**
     *@return array of positons on the map that is within the range of this 
     * ship's radar.
     */
    public abstract Vector2[] getRadarRange();
    
    /**
     *@return array of positons on the map that is within the range of this 
     * ship's canon.
     */
    public abstract Vector2[] getCanonRange();
    
    /**
     *@return array of positons on the map that this ship can move to.
     */
    public abstract Vector2[] availableMoves();
    
    /**
     *@return array of positons on the map that this ship can turn to.
     */
    public abstract Vector2[] availableTurns();  
          
}
