/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.gui;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import my_game.models.game_components.CannonDescription;
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
    
    /* Cannon animation variables. */
    CannonDescription cannon;
    Spatial cannonSpatial;
    float currentHeight;
    float distance;
    
    AssetManager as;
    Node field;
    

    Animator(AssetManager assetManager, Node field) {
        as = assetManager;
        cannonSpatial = as.loadModel("/Models/Cannon/Cannon.j3o");
        cannonSpatial.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        this.field = field;
    }
    
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
        //scale the path to fit the scale of the remaining things on screen
        path.x = path.x * 2;
        path.y = path.y * 2;
        //get a reference to all the spatials in order to be able to manipulate them directly
        objectGridRef = grid;
        
        alpha = 0;
        //change the phase as ready to animate
        phase = AnimationPhase.Animating;
    }
    
    /**
     * Advance to the next frame.
     */
    void nextMoveFrame() {
        try {
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
        } catch(NullPointerException e) {
            alpha = 1;
        }
    }
    
    void startCannonAnimation(GameState updateState, Spatial[][] grid) {
        cannon = (CannonDescription) updateState.previousAction;
        path = new Vector2(cannon.target);
        path.sub(cannon.origin);
        //scale the path to fit the scale of the remaining things on screen
        path.x = path.x * 2;
        path.y = path.y * 2;
        //get a reference to all the spatials in order to be able to manipulate them directly
        objectGridRef = grid;
        
        alpha = 0;
        distance = path.getLength();
        //add the cannon spatial to the scene
        field.attachChild(cannonSpatial);
        //change the phase as ready to animate
        phase = AnimationPhase.Animating;
    }
    
    void nextCannonFrame() {
        try {
            if(alpha >= 1) {
                //TODO add explosion if target hit
                field.detachChild(cannonSpatial);
                phase = AnimationPhase.Done;
            } else {
                //take alpha to the next step
                alpha += STEP;
                if(alpha > 1) {
                    alpha = 1;
                }
                //set position of the cannon
                float param = alpha * distance;
                cannonSpatial.setLocalTranslation((2 * (cannon.origin.x - 15) + 1) + (path.x * alpha), 2 * (1 - (param * (param - distance))),    //for the height, use quadratic formula to get parabola
                            (2 * (cannon.origin.y - 15) + 1) + (path.y * alpha));
            }
        } catch(NullPointerException e) {
            alpha = 1;
        }
    }
        
    /**
     * @return True if the animation is currently in progress, otherwise false.
     */
    boolean isAnimating() {
        return phase.equals(AnimationPhase.Animating);
    }
}
