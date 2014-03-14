/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.controller;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Position;
import my_game.gui.GameGUI;
import my_game.gui.GameGUI.Action;
import my_game.models.game_components.GameObject;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Map;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.Ship;
import my_game.models.game_components.ShipDirection;
import my_game.models.game_components.ShipUnit;
import my_game.networking.NetworkEntity;
import my_game.models.player_components.Player;
import my_game.networking.NetEntityListener;
import my_game.util.Vector2;
import my_game.util.GameException;
import my_game.util.Misc;
import my_game.util.Positions;
import my_game.util.TurnPositions;

    
/**
 * This class is a controller of the game. One instance
 * of this class runs at the host and one at the client, 
 * both of these communicating amongst them via networking
 * entities (GameServer and GameClient).
 */
public class Game implements GameGUI.GameGuiListener {    
    
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
    private GameState gameState, receivedGameState;
    private boolean receivedNewGamestate = false;
    
    /** The player type is a flag indicating whether the player running the instance
     * of this Game object is the host of the game, or a client connected to the host. */
    private final Game.PlayerType playerType;
    
    private final ServerListener sListener;
    private final ClientListener cListener;
    
    /** This variable contains a ship selected by the user. */
    private Ship selectedShip;
    /** A reference to the game gui which is displaying the game itself. */
    private GameGUI gui;
    
    private Positions moveHighlight;
    private TurnPositions turnHighlight;
    private boolean awaitingInput;
    private Vector2 input;
    
    public Game(Player player, Player opponent, CoralReef reef, NetworkEntity net, Game.PlayerType playerType, String name) {
        this(player, opponent, reef, net, playerType, name, 0);
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
            sListener = new ServerListener();
            cListener = null;
            
            net.addNetListener(sListener);
            
            //generate the index of the first player if an invalid index is passed.
            int firstPlayer = startingPlayer;
            if(startingPlayer < 0 || startingPlayer > 1) {
                firstPlayer = getRandomFirstPlayer();
            }
            //init game state
            gameState = new GameState(new Player[] {player, opponent}, reef, firstPlayer, name);
            
            //TODO if the line below is commented, uncomment for proper behaviour
            //this.net.sendGameState(gameState);
            startGame();
        } else {
            //add a listener to the client
            sListener = null;
            cListener = new ClientListener();
            
            net.addNetListener(cListener);
            
            System.out.println("Client will now wait to receive game state.");
            //You are a client. Wait to receive a game state from server
            synchronized(player) {
                while(!receivedNewGamestate) {
                    try {
                        player.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            System.out.println("Client received a game state.");
            gameState = new GameState(this.receivedGameState);
            receivedNewGamestate = false;
            //replace the partial information about this player in the gamestate with the full info
            gameState.setPlayer(1, player);
            
            startGame();
        }
    } 
    
    /**
     * A listener for the server, if this player is a host.
     */
    private class ServerListener implements NetEntityListener {

        public void onConnected() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onReefReceive(CoralReef reef) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onVoteReceive(boolean vote) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onGameStateReceive(GameState gs) {
            //use player as a common synchronization object
            synchronized(player) {
                receivedGameState = gs;
                receivedNewGamestate = true;
                player.notifyAll();
            }
        }
        
    }
    
    /**
     * A listener for the client, if this player joined the game of a host.
     */
    public class ClientListener implements NetEntityListener {

        public void onConnected() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onReefReceive(CoralReef reef) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onVoteReceive(boolean vote) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void onGameStateReceive(GameState gs) {
            //use player as a common synchronization object
            synchronized(player) {
                receivedGameState = gs;
                receivedNewGamestate = true;
                player.notifyAll();
            }
        }
        
    }
    
    private void startGame() {
        //MAIN GAME LOOP PSEUDO
        //init and display GUI
        gui = new GameGUI(30, 30, this, this.player);
        gui.start();
        //now wait until the gui initializes
        synchronized(this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        gui.drawGameState(this.gameState);
        //positioning phase
        //gameState.setGamePhase(GameState.GamePhase.ShipPositioning);
        Ship s; // get the ship we want to position from GUI
        
        // get the new position we want to move that ship around the base;
        
        // calls positionShip
        
        //main game loop
            //if your turn
                //activate actions in gui
                //wait to receive an action
                //process action
                //end turn
            //else
                //wait for other player to finish turn
        //repeat until game over
    }
    
    /* **************  GameGuiListener implemented methods ******************* */
    
    @Override
    public void initializeComplete() {
        //if someone is waiting on this monitor, notify that gui is initialized
        synchronized(this) {
            this.notifyAll();
        }
    }
    
    @Override
    public void onMouseClick(int x, int y) {
        //if it's the player's turn and there is a ship under the mouse cursor, select the ship
        Player playerWithTurn = gameState.getPlayer(gameState.getPlayerTurn());
        Vector2 position = new Vector2(x, y);
        
        Misc.log((playerWithTurn.equals(player)) + " and " + gameState.getMap().isShip(position));
        if(playerWithTurn == player && gameState.getMap().isShip(position)) {
            //mark the ship as selected and enable the gui buttons
            ShipUnit s = (ShipUnit) gameState.getMap().getObjectAt(position);
            Ship ship = s.getShip();
            this.selectedShip = ship;
            gui.setButtonsEnabled(true);
            Misc.log("Clicked on ship.");
        } else if (awaitingInput) {
            synchronized(this) {
                input = new Vector2(position);
                this.notifyAll();
            }
        } else {
            this.selectedShip = null;
            gui.setButtonsEnabled(false);
            if(moveHighlight != null || turnHighlight != null) {
                gui.requestlearHighlight();
                moveHighlight = null;
                turnHighlight = null;
            }
        }
    }
    
    @Override
    public void onButtonPressed(Action action) {
        if(selectedShip != null) {
            //we can take an action, there's a ship already selected
            Thread t;
            switch(action) {
                case Move:
                    t = new Thread(new Runnable() {
                        public void run() {
                            moveAction(selectedShip);
                        }
                    });
                    t.start();
                    break;
                case Turn:
                    Misc.log("Turning");
                    t = new Thread(new Runnable() {
                        public void run() {
                            turnAction(selectedShip);
                        }
                    });
                    t.start();
                    break;
                case Attack:
                    t = new Thread(new Runnable() {
                        public void run() {
                            attackAction(selectedShip);
                        }
                    });
                    t.start();
                    break;
                default:
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, new GameException("Unknown action encountered."));
                    break;
            }
        }
    }
    
    /* *********************************************************************** */
    
    
    /**
     * Places the provided ship at the specified coordinates of the map.
     * @param s
     * @param x
     * @param y 
     */

    
    /**
     * Chooses randomly between 0 and 1, then returns the result.
     * @return 
     */
    private int getRandomFirstPlayer() {
        double r = Math.random();
        if(r < 0.5) {
            return 0;
        } else {
            return 1;
        }
    }
    
    public void moveAction(Ship s){
        //need to be called on the map object.
        moveHighlight = gameState.getMap().prepareMoveShip(s);

        // TO DO: pass these positions to GUI and get user's selection in Vector2 newPosition)
        gui.highlightPositions(moveHighlight);
       
        synchronized(this) {
            try {
                awaitingInput = true;
                this.wait();
                awaitingInput = false;
                //check if the result is acceptable
                
                if(gameState.getMap().moveShip(s, input, moveHighlight)) {
                    gui.drawGameState(gameState);
                    
                    sendGameState();
                }
                
                gui.requestlearHighlight();
                gui.setButtonsEnabled(false);
                selectedShip = null;
                moveHighlight = null;

            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void turnAction(Ship s){
        //need to be called on the map object.
        turnHighlight = gameState.getMap().prepareTurnShip(s);

        // TO DO: pass these positions to GUI and get user's selection in Vector2 newPosition)
        gui.highlightPositions(turnHighlight);
       
        synchronized(this) {
            try {
                awaitingInput = true;
                this.wait();
                awaitingInput = false;
                //check if the result is acceptable
                
                if(gameState.getMap().turnShip(s, input, turnHighlight)) {
                    gui.drawGameState(gameState);
                    
                    sendGameState();
                }
                
                gui.requestlearHighlight();
                gui.setButtonsEnabled(false);
                selectedShip = null;
                moveHighlight = null;

            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
            
    private void attackAction(Ship s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
        
    public void fireCannon(GameObject unit){
    	//can be called by any type of ship
    	//TO DO: highlight the available ship and cannonRange to GUI pass the (user selected)attacker and targeting position to map
    	//map.cannonAttack(Ship attacker, Vector2 position)
    }
    
    public void fireTorpedo(GameObject unit){
    	//can only be called by destroyer and torpedo boat
    	//TO DO: highlight the available ship and torpedoRange to GUI pass the (user selected)attacker and targeting position to map
    	//map.TorpedoAttack(Ship attacker, Vector2 position)
    }
    
    /**
     * Sends the game state to other connected players.
     */
    private void sendGameState() {
        net.sendGameState(this.gameState);
    }
}
