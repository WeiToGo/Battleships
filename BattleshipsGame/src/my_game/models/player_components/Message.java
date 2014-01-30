/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.player_components;

/**
 * A message of a certain type which also contains visibility information, which
 * means that it can be seen by one player and not the other.
 */
public class Message {
    
    public enum MessageType {
        Chat, NetworkInfo, NetworkError, Game
    };
    
    /** The type of this message. */
    private MessageType type;
    /** Messages of certain types are only seen by one player. 
     * That is the receiver of the message. */
    private Player receiver;
    
    /**
     * @return The type of this message.
     */
    public MessageType getMessageType() {
        return this.type;
    }
    
    /**
     * @return If the type of this message is a type of message which is only
     * seen by one player, returns the only Player meant to receive and see 
     * this message, otherwise returns null.
     */
    public Player getReceiver() {
        // TODO Not implemented.
        return null;
    }
}
