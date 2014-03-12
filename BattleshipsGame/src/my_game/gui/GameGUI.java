/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.ShipDirection;
import my_game.util.GameException;

/**
 * This is the JMonkeyEngine window where the gameplay interface and 3D graphics
 * are displayed.
 * @author Ivo Parvanov
 */
public class GameGUI extends SimpleApplication {
    
    /** Integers used to indicate to the block drawing algorithm what block type to draw. */
    private final static int BASE = 0, BLOCK = 1, BOW = 2;
    
    Spatial grid, shade, shipMain, shipBow;
    
    /** A grid containing a Spatial at every grid position if there is a ship part there. */
    Spatial[][] objectsGrid;
    /** A node containing the game field: grid, ships, bases and corals. */
    Node field;
    Vector3f translation;
    Quaternion rotation;
    Vector3f direction;

    public GameGUI(int width, int height) {
        objectsGrid = new Spatial[width][height];
    }
    
    @Override
    public void simpleInitApp() {
        
        loadTerrain();
        loadGrid();
        loadLights();
        loadShip();
        
                //Post processing
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        BloomFilter bloom=new BloomFilter();
        bloom.setBloomIntensity(0.5f);
        bloom.setExposureCutOff(0.8f);
        bloom.setBlurScale(1.1f);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
        
        
        FilterPostProcessor water;
        water = assetManager.loadFilter("Models/water.j3f");
        viewPort.addProcessor(water);

        flyCam.setEnabled(false);
        final RtsCam rtsCam = new RtsCam(cam, rootNode);
        rtsCam.registerWithInput(inputManager);
        rtsCam.setCenter(new Vector3f(70, 60, 70));
        rtsCam.setMaxSpeed(RtsCam.Degree.FWD, 50, 0.5f);
        rtsCam.setMaxSpeed(RtsCam.Degree.SIDE, 50, 0.5f);
        inputManager.setCursorVisible(true);

        
        //TESTS
        //draw a bunch of ship squares
        ShipDirection dir = ShipDirection.East;
        drawShipPart(9, 9, dir, BLOCK);
        drawShipPart(10, 9, dir, BLOCK);
        drawShipPart(11, 9, dir, BLOCK);
        drawShipPart(12, 9, dir, BLOCK);
        drawShipPart(13, 9, dir, BOW);
    }

    private void loadTerrain() {
        Spatial terrain;
        terrain = assetManager.loadModel("Scenes/world.j3o");
        rootNode.attachChild(terrain);
    }
    
    private void loadGrid() {
        
        field = new Node("Field");
        grid = assetManager.loadModel("Models/Grid/Plane.002.mesh.xml");
        field.attachChild(grid);
        field.setLocalScale(3, 1.5f, 3);
        field.setLocalTranslation(0, 0.4f, 0);
        rootNode.attachChild(field);
    }
    
    private void loadLights() {
        //Light settings
        /**
         * A white, directional light source
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
        /**
         * A white ambient light source.
         */
        AmbientLight ambient1 = new AmbientLight();
        ambient1.setColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 1));
        rootNode.addLight(ambient1);
    }
    
    private void loadShip() {
        shipMain = assetManager.loadModel("Models/ShipBlocks/ShipBlock.j3o");
        shipMain.setMaterial(assetManager.loadMaterial("Materials/shipMaterialDefault.j3m"));
        
        shipBow = assetManager.loadModel("Models/ShipBlocks/ShipBow.j3o");
        shipBow.setMaterial(assetManager.loadMaterial("Materials/shipMaterialDefault.j3m"));
    }
    
    private void drawShipPart(int x, int y, ShipDirection dir, int type) {
        Spatial shipPartInstance = null;
        switch(type) {
            case BLOCK:
                shipPartInstance = shipMain.clone();
                break;
            case BOW:
                shipPartInstance = shipBow.clone();
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Unexpected ship unit type in drawShipPart(...)!"));
                break;
        }
        //position the ship unit within the grid
        shipPartInstance.setLocalTranslation(2 * (x - 15) + 1, 1, 2 * (y - 15) + 1);
        //set the rotation of the ship relative to its direction
        Quaternion rotation = null;
        switch(dir) {
            case West:
                rotation = new Quaternion(new float[] {0f, (float) Math.PI / 2, 0f});
                break;
            case North:
                rotation = new Quaternion(new float[] {0f, (float) Math.PI, 0f});
                break;
            case East:
                rotation = new Quaternion(new float[] {0f, (float) (3 * Math.PI) / 2, 0f});
                break;
            case South:
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null,                         
                        new GameException("Unexpected direction of the ship found."));
                break;
        }
        shipPartInstance.setLocalRotation(rotation);
        field.attachChild(shipPartInstance);
        this.objectsGrid[x][y] = shipPartInstance;
    }
}
