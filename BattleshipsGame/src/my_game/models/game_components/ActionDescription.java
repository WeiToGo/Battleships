/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.gui.GameGUI.Action;

/**
 * Describes all details of a single action in order for someone to be able to
 * animate the action using this information. This is basically a container for data.
 * @author Ivaylo Parvanov
 */
public abstract class ActionDescription implements java.io.Serializable {
    Action actionType;
    
    /**
     * @return The type of the action described by the data in this class.
     */
    public Action getActionType() {
        return actionType;
    }
}
