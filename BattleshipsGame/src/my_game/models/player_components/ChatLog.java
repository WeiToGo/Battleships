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
public class ChatLog implements java.io.Serializable {
    
    /** This is the log of messages. */
    private ArrayList<Message> messages;
    
    /**
     * TODO When saving the chat log, it should not be saved in ASCII because
     * then it would be very easy for the player who made the save to open 
     * the save file and read all the game messages intended to be visible 
     * only to the opponent.
     */
    
    public ChatLog() {
        this(null);
    }

    public ChatLog(ChatLog chatLog) {
        messages = new ArrayList();
        if(chatLog != null) {
            //if a chat log is passed, make a copy of every message
            for(Message m: chatLog.messages) {
                this.messages.add(m);
            }
        }
    }
    
    /**
     * Returns a subset of the full list of messages, which contains all the
     * messages intended to be seen by the specified player (hides messages
     * meant to be seen by the opponent of Player p).
     * @param p The player whose messages subset will be returned.
     * @return 
     */
    public List<Message> getMessages(Player p) {
        ArrayList<Message> playerMsgs = new ArrayList<Message>();
        for(Message m: this.messages) {
            Player receiver = m.getReceiver();
            if(receiver == null || receiver.equals(p)) {
                playerMsgs.add(m);
            }
        }
        return playerMsgs;
    }
    
    /**
     * Adds a message to the chat log.
     * @param m 
     */
    public void addMessage(Message m) {
        if(m != null) {
            this.messages.add(m);
        }
    }
    
    @Override
    public String toString() {
        return "ChatLog contains " + messages.size() + " messages.";
    }
}
