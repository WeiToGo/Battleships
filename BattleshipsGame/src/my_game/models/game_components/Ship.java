/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

/**
 *
 */
public abstract class Ship extends GameObject {
    
    public enum ShipType {
        Cruiser, Destroyer, TorpedoBoat, MineLayer, RadarBoat
    };
    
    /** The type of this ship. */
    private ShipType shipType;
    
    public Ship() {
        this.type = GameObjectType.Ship;
        // TODO complete
    }
    
    /**
     * @return The type of this ship.
     */
    public ShipType getShipType() {
        return this.shipType;
    }
}
