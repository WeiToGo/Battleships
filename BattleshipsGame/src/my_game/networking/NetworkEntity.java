package my_game.networking;

import java.awt.Color;
import java.net.InetAddress;
import my_game.models.game_components.CoralReef;
import my_game.models.game_components.GameState;
import my_game.models.player_components.Player;

public interface NetworkEntity {

    
    //TODO adapt this interface to the battleships game
    
    
	/**
	 * Communicates the opponent back to all network listeners(GUI and controllers).
	 * @param username
	 */
	public void setOpponent(Player p);

	/**
	 * Sends the game state back to all network listeners (GUI and controllers).
	 * @param gs
	 */
	public void updateGameState(GameState gs);

	/**
	 * Sends a game state packet to all other connected entities.
	 * @param gs
	 */
	public void sendGameState(GameState gs);
        
        /**
         * Returns the ipAddress of the player connected to this network entity.
         * @return 
         */
        public InetAddress getRemote();

        /**
         * Sends the coral reef back to all listeners.
         * @param reef 
         */
        public void sendCoralReef(CoralReef reef);
}
