package my_game.networking;

import java.awt.Color;

public interface NetworkEntity {

    
    //TODO adapt this interface to the battleships game
    
    
	/**
	 * Sets the name of an opponent entity.
	 * @param username
	 */
	public void setOpponentName(String username);

	/**
	 * Updates the game state of the main window gui interface connected with the entity 
	 * with a circle at the specified coordinates and of the given colour and radius.
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 */
	public void updateGameState(float x, float y, float radius, Color color);

	/**
	 * Sends a game state packet to all other connected entities.
	 * @param x
	 * @param y
	 * @param radius
	 * @param color
	 */
	public void sendGameState(float x, float y, float radius, Color color);
	
}
