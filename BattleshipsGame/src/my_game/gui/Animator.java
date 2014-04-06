/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.gui;

import com.jme3.scene.Spatial;
import my_game.models.game_components.GameState;
import my_game.models.game_components.MoveDescription;
import my_game.util.Misc;
import my_game.util.Vector2;

/**
 * A class capable of executing different animations for the different actions that
 * ships undertake.
 */
public class Animator {
    public enum AnimationPhase {
        Preparing, Animating, Done
    };
    
    AnimationPhase phase;
    
    Spatial[][] objectGridRef;
    
    /* Move animation variables. */
    public final float STEP = 0.02f;
    Vector2 path;
    float alpha;
    MoveDescription move;
    
    
    void prepareNewAnimation() {
        phase = AnimationPhase.Preparing;
    }
    
    /**
     * @return True if the current phase of the animator is preparing for the next animation.
     */
    boolean isPreparing() {
        return phase.equals(AnimationPhase.Preparing);
    }
    
    /**
     * Initiates a new move animation.
     * @param updateState 
     */
    void startMoveAnimation(GameState updateState, Spatial[][] grid) {
        //get a vector pointing from old position to the new one
        move = (MoveDescription) updateState.previousAction;
        path = new Vector2(move.newPositions[0]);
        path.sub(move.oldPositions[0]);
        //scale the path by 3 to fit the scale of the remaining things on screen
        path.x = path.x * 2;
        path.y = path.y * 2;
        //get a reference to all the spatials in order to be able to manipulate them directly
        objectGridRef = grid;
        
        alpha = 0;
        //change the phase as ready to animate
        phase = AnimationPhase.Animating;
    }
    
    /**
     * @return True if the animation is currently in progress, otherwise false.
     */
    boolean isAnimating() {
        return phase.equals(AnimationPhase.Animating);
    }
    
    /**
     * Advance to the next frame.
     */
    void nextMoveFrame() {
        if(alpha >= 1) {
            //interpolation is done
            //move all spatials from the old positions to their new ones as the animation is done
            Spatial[] tmp = new Spatial[move.newPositions.length];
            for(int i = 0; i < move.newPositions.length; i++) {
                //backup all Spatials in the tmp array and set their entries in the objects grid to null
                int oldx = move.oldPositions[i].x;
                int oldy = move.oldPositions[i].y;
                tmp[i] = objectGridRef[oldx][oldy];
                objectGridRef[oldx][oldy] = null;
            }
            
            for(int i = 0; i < move.newPositions.length; i++) {
                //retrieve the objects from the temporary array and set them to their new positions
                int newx = move.newPositions[i].x;
                int newy = move.newPositions[i].y;
                
                objectGridRef[newx][newy] = tmp[i];
            }
            phase = AnimationPhase.Done;
        } else {
            //take alpha to the next step
            alpha += STEP;
            if(alpha > 1) {
                alpha = 1;
            }
            //move every ship unit
            for(int i = 0; i < move.newPositions.length; i++) {
                int oldx = move.oldPositions[i].x;
                int oldy = move.oldPositions[i].y;
                objectGridRef[oldx][oldy].setLocalTranslation((2 * (oldx - 15) + 1) + (path.x * alpha), 1,
                        (2 * (oldy - 15) + 1) + (path.y * alpha));
            }
        }
    }
}
