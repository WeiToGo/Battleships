/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models;

import my_game.models.game_components.Map;
import my_game.models.player_components.ChatLog;
import my_game.networking.server.entities.Player;

/**
 * The state of a game describes a game fully.
 */
public class GameState {
    
    /** Whose turn is it? */
    private Player turn;
    
    /** The name of the game given to it when it is created for the first time. */
    public final String name;
    
    /** Identifier for the game. */
    public final int gameID;    // TODO Is this necessary? Please clarify why. 
    
    private ChatLog chatLog;
    private Map map;
    
    public GameState() {
        name = null;
        gameID = 0;
    }
    
    //TODO accessors and mutators for chat log and map
}
