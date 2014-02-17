/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Map;
import my_game.models.game_components.Ship;
import my_game.models.player_components.ChatLog;
import my_game.models.player_components.Player;

/**
 * The state of a game describes a game fully.
 */
public class GameState {
    
    public enum GamePhase {
        New,    // game has just been created, players have not interacted yet
        ShipPositioning, // after creating the game, players position their ships on the map
        PlayerTurns,    // the main part of the game where players take turns 
        GameOver    //the game is over, one player is the winner
    };
    
    protected GamePhase phase;
    
    /** An array of two players who are playing the game. */
    private Player[] player;
    
    /** Index of the players array pointing to the player whose turn it currently is. */
    private int playerTurn;
    
    /** The name of the game given to it when it is created for the first time. */
    public final String name;
    
    /** Identifier for the game. */
    //public final int gameID;    // TODO Is this necessary? Please clarify why. Also it will be unnecessarily comples to generate unique IDs
    
    protected ChatLog chatLog;
    protected Map map;
    
    
    //TODO accessors and mutators for chat log and map

    public GameState(Player[] player, CoralReef reef, int firstPlayer, String name) {
        //init game phase
        this.phase = GamePhase.New;
        //init the players array
        this.player = new Player[2];
        this.player[0] = player[0];
        this.player[1] = player[1]; 
        //init player turn
        this.playerTurn = firstPlayer;
        //set game name
        this.name = name;
        //init each player's ships
        Ship[] player0Ships = generateShips(player[0]);
        Ship[] player1Ships = generateShips(player[1]);
        Base player0Base = new Base(player[0].getID());
        Base player1Base = new Base(player[1].getID());
        //init map
        map = new Map(reef, player0Ships, player1Ships, player0Base, player1Base);
        //init chat
        chatLog = new ChatLog();
    }
    
    /**
     * Access the map object of this game state.
     * @return 
     */
    public Map getMap() {
        return this.map;
    }
    
    public GameState(GameState copyState) {
        //TODO copy all fields of the copyState to this game state
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Ship[] generateShips(Player player) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
