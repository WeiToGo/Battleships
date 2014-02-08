/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.player_components;

/**
 * Contains all information necessary to represent a player.
 */
public class Player {
    
    /** The name used to identify this player. */
    private String name;
        
    /** The unique identifier of this player. */
    public final int id;    
    
    /** The password. */
    private String password;
    
    
    private int status;
    
    public Player(int id) {
        this.id = id;
        // TODO modify constructor as necessary
    }
    
}
