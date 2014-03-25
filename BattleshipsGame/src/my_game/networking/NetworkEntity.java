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
	public void sendGameStateToListeners(GameState gs);

	/**
	 * Sends a game state packet to all other connected entities.
	 * @param gs
	 */
	public void sendGameState(GameState gs);
        
        /**
         * Sends a vote to all other connected entities (clients or servers).
         * @param vote 
         */
        public void sendVote(boolean vote);
        
        /**
         * Returns the ipAddress of the player connected to this network entity.
         * @return 
         */
        public InetAddress getRemote();

        /**
         * Sends the coral reef back to all listeners.
         * @param reef 
         */
        public void sendCoralReefToListeners(CoralReef reef);

        /**
         * Sends a received map vote to all listeners.
         * @param vote The received vote: true means accept and false means refuse.
         */
        public void sendVoteToListeners(boolean vote);
        
        /**
         * Adds a NetEntityListener to the list of listeners of this NetEntity.
         * @param l 
         */
        public void addNetListener(NetEntityListener l);
    
        /**
         * Removes a NetEntityListener to the list of listeners of this NetEntity.
         * @param l 
         */
        public void removeNetListener(NetEntityListener l);

    public void invalidPacket();
}
