/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.gui.GameGUI.Action;
import my_game.util.Vector2;

/**
 * A description of the action of a ship moving to a new position.
 */
public class MoveDescription extends ActionDescription {
    public Vector2[] oldPositions;
    public Vector2[] newPositions;
    
    public MoveDescription(Vector2[] oldPositions, Vector2[] newPositions) {
        this.oldPositions = oldPositions;
        this.newPositions = newPositions;
        this.actionType = Action.Move;
    }
}
