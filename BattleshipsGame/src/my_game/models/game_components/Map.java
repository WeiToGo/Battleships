/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;


/**
 * This is the map object containing all game objects dispayed on the
 * screen: ships, obstacles (corals), bases and weapons.
 */
public class Map {
    
    private Grid mapGrid;
    private CoralReef reef;
    
    private Ship[] player1Ships;
    private Ship[] player2Ships;
    
    public Map() {
        /* TODO when creating the mapGrid use a CoralReef to provide 
         * the positions of all obstacles. */
    }
}
