/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models;

import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Map;
import my_game.models.player_components.ChatLog;
import my_game.networking.server.entities.Player;

/**
 * The state of a game describes a game fully.
 */
public class GameState {
    
    /** An array of two players who are playing the game. */
    private Player[] players;
    
    /** Index of the players array pointing to the player whose turn it currently is. */
    private int playerTurn;
    
    /** The name of the game given to it when it is created for the first time. */
    public final String name;
    
    /** Identifier for the game. */
    public final int gameID;    // TODO Is this necessary? Please clarify why. Also it will be unnecessarily comples to generate unique IDs
    
    private ChatLog chatLog;
    private Map map;
    
    //TODO accessors and mutators for chat log and map

    public GameState(Player[] player, CoralReef reef, int firstPlayer) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public GameState(GameState copyState) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
