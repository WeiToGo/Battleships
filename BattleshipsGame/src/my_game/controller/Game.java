/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Map;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Ship;
import my_game.networking.NetworkEntity;
import my_game.models.player_components.Player;
import my_game.util.Vector2;
import my_game.util.GameException;
import my_game.util.Positions;

/**
 * This class is a controller of the game. One instance
 * of this class runs at the host and one at the client, 
 * both of these communicating amongst them via networking
 * entities (GameServer and GameClient).
 */
public class Game {
    public enum PlayerType {
        Host, Client
    };
    
    /** The Player who is running this instance of the Game controller. */
    private final Player player;
    /** The opponent of the player who is running this instance of the Game controller. */
    private final Player opponent;
    /** Network object used for communication with the opponent. */
    private final NetworkEntity net;
    /** The game state contains a full description of the game that is currently being played. */
    private GameState gameState;
    /** The player type is a flag indicating whether the player running the instance
     * of this Game object is the host of the game, or a client connected to the host. */
    private final Game.PlayerType playerType;
    
    
    
    public Game(Player player, Player opponent, CoralReef reef, NetworkEntity net, Game.PlayerType playerType, String name) {
        this(player, opponent, reef, net, playerType, name, -1);
    }
    
    /**
     * When a new game is constructed, this constructor also initializes all
     * data structures necessary for the new game.
     * @param player Player who is creating this object.
     * @param opponent Player who is connected to this game via the network.
     * @param reef Coral reef used for the game.
     * @param net Network entity used for connection to the other player.
     * @param playerType Type of the player: host or client
     * @param name The name of the game.
     * @param startingPlayer The player who starts first.
     */
    public Game(Player player, Player opponent, CoralReef reef, NetworkEntity net, 
            Game.PlayerType playerType, String name, int startingPlayer) {
        //init local fields
        this.player = player;
        this.opponent = opponent;
        this.net = net;
        this.playerType = playerType;
        
        if(playerType == Game.PlayerType.Host) {
            //generate the index of the first player if an invalid index is passed.
            int firstPlayer = startingPlayer;
            if(startingPlayer < 0 || startingPlayer > 1) {
                firstPlayer = getRandomFirstPlayer();
            }
            //init game state
            gameState = new GameState(new Player[] {player, opponent}, reef, firstPlayer, name);
            
            //TODO Send generated game state to client!!!
            //startHostGame();
        } else {
            //You are a client. Wait to receive a game state from server
            GameState receivedGameState = null; //receive game state from server
            gameState = new GameState(receivedGameState);
            //startClientGame();
        }
    } 
    
    /**
     * Places the provided ship at the specified coordinates of the map.
     * @param s
     * @param x
     * @param y 
     */
    private void positionShip(Ship s, Vector2 position) {
        try {
            gameState.getMap().moveShip(s,position);
        } catch (GameException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    private int getRandomFirstPlayer() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    // TODO Different methods to modify and control the game satate
    
    public void moveAction(Ship s){
        //need to be called on the map object.
        //Positions highlight = map.prepareMoveShip(s);

        // TO DO: pass these positions to GUI and get user's selection in Vector2 newPosition)
        
        
        //map.moveShip(s,newPosition, highlight);
    }
    
    public void turnAction(Ship s){
        //need to be called on the map object.
        //Positions highlight = map.prepareTurnShip(s);

        // TO DO: pass these positions to GUI and get user's selection in Vector2 newPosition)
        
        
        //map.turnShip(s,newPosition, highlight);
    }
            
    
}
