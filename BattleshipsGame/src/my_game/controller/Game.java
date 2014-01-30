/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.controller;

import my_game.models.GameState;
import my_game.networking.server.entities.Player;

/**
 * This class represents a game session which involves two players and a
 * game state (map + chat log). A game is created every time a user loads or 
 * hosts a new game.
 */
public class Game {
    
    private final Player player1;
    private final Player player2;
    
    private GameState gameState;
    
    /**
     * When a new game is constructed, this constructor also initializes all
     * data structures necessary for the new game.
     */
    public Game() {
        // TODO implement constructor(s)
        player1 = player2 = null;
    } 
    
    // TODO Different methods to modify and control the game satate
}
