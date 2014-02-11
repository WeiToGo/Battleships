/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.player_components;

import java.util.ArrayList;
import java.util.List;

/**
 * A log of chat messages and game information messages sent between players.
 */
public class ChatLog {
    
    /** This is the log of messages. */
    private List<Message> messages;
    
    /**
     * TODO When saving the chat log, it should not be saved in ASCII because
     * then it would be very easy for the player who made the save to open 
     * the save file and read all the game messages intended to be visible 
     * only to the opponent.
     */
    
    public ChatLog() {
        messages = new ArrayList();
    }
    
    /**
     * Returns a subset of the full list of messages, which contains all the
     * messages intended to be seen by the specified player (hides messages
     * meant to be seen by the opponent of Player p).
     * @param p The player whose messages subset will be returned.
     * @return 
     */
    public List<String> getMessages(Player p) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
