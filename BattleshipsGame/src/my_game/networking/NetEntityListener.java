/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.networking;

import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;

/**
 * An interface which allows a class to be added to the listeners list
 * of a network entity so that the network entity communicates back events
 * through the interface as they occur.
 * @author Ivo Parvanov
 */
public interface NetEntityListener {
    // add methods needed for communication here.
    
    /**
     * This method is called every time another network entity is connected to
     * this one.
     */
    public void onConnected();
    
    /**
     * This method is called whenever the network entity receives a packet containing
     * a CoralReef object.
     * @param reef The CoralReef object received by the network entity.
     */
    public void onReefReceive(CoralReef reef);

    /**
     * This methods is called whenever the network entity receives a packet 
     * containing a map vote. True means a positive vote and false means a 
     * negative one.
     * @param vote 
     */
    public void onVoteReceive(boolean vote);

    /**
     * This method is called whenever the network entity receives a packet
     * containing a GameState object. 
     * @param gs The GameState object received.
     */
    public void onGameStateReceive(GameState gs);
}
