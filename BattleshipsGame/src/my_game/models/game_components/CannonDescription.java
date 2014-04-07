/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.models.game_components;

import my_game.gui.GameGUI.Action;
import my_game.util.Vector2;

/**
 *
 */
public class CannonDescription extends ActionDescription {
    
    public Vector2 origin;
    public Vector2 target;
    /** If there is an object other than water at the target coordinates, this boolean should be set to true. */
    public boolean hit;
    
    /**
     * NOTE: Does not make a copy of the vectors.
     * @param origin
     * @param target
     * @param hit 
     */
    public CannonDescription(Vector2 origin, Vector2 target, boolean hit) {
        this.origin = origin;
        this.target = target;
        this.hit = hit;
        
        this.actionType = Action.CannonAttack;
    }
}
