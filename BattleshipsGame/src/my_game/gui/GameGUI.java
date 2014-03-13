/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_game.models.game_components.BaseUnit;
import my_game.models.game_components.CoralUnit;
import my_game.models.game_components.GameObject;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Map;
import my_game.models.game_components.ShipDirection;
import my_game.models.game_components.ShipUnit;
import my_game.models.player_components.Player;
import my_game.util.GameException;
import my_game.util.Vector2;

/**
 * This is the JMonkeyEngine window where the gameplay interface and 3D graphics
 * are displayed.
 * @author Ivo Parvanov
 */
public class GameGUI extends SimpleApplication implements ActionListener {    
    
    /** Integers used to indicate to the block drawing algorithm what block type to draw. */
    private final static int BASE_BLUE = 0, BLOCK_BLUE = 1, BOW_BLUE = 2, BASE_RED = 3, BLOCK_RED = 4, BOW_RED = 5;
    
    private final static Plane gridPlane = new Plane(Vector3f.UNIT_Y, 0);
    
    Spatial grid, shade, blueShipBlock, blueShipBow, redShipBlock, redShipBow, blueBase, redBase, rock;
    
    /** A grid containing a Spatial at every grid position if there is a ship part there. */
    Spatial[][] objectsGrid;
    /** A node containing the game field: grid, ships, bases and corals. */
    Node field;
    Vector3f translation;
    Quaternion rotation;
    Vector3f direction;
    
    /** A reference to the guiListener interface used to communicate back to a controller. */
    GameGuiListener guiListener;
    /** Local reference to a game state which is drawn on frame update. */
    private GameState gameState;
    /** A flag set to true every time the drawGameState method is called. */
    private boolean gameStateUpdated;
    /** Reference to the player running the instance of this GUI. This variable
     * is used for determining the radar visibility, chat log and controls. */
    private Player player;

    /**
     * 
     * @param width
     * @param height
     * @param g
     * @param p The player who is running the instance of this GUI.
     */
    public GameGUI(int width, int height, GameGuiListener g, Player p) {
        objectsGrid = new Spatial[width][height];
        guiListener = g;
        this.player = p;
    }
    
    @Override
    public void simpleInitApp() {
        
        loadTerrain();
        loadGrid();
        loadLights();
        loadShip();
        loadBase();
        loadRock();
        loadRadar();
        
                //Post processing
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        BloomFilter bloom=new BloomFilter();
        bloom.setBloomIntensity(0.5f);
        bloom.setExposureCutOff(0.8f);
        bloom.setBlurScale(1.1f);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
        
        
        FilterPostProcessor water;
        water = assetManager.loadFilter("/Models/water.j3f");
        viewPort.addProcessor(water);

        flyCam.setEnabled(false);
        final RtsCam rtsCam = new RtsCam(cam, rootNode);
        rtsCam.registerWithInput(inputManager);
        rtsCam.setCenter(new Vector3f(0, 80, 100));
        rtsCam.setMaxSpeed(RtsCam.Degree.FWD, 50, 0.5f);
        rtsCam.setMaxSpeed(RtsCam.Degree.SIDE, 50, 0.5f);
        inputManager.setCursorVisible(true);
        
        //report to the guiListener that init. is complete so he can now send requests to the gui
        guiListener.initializeComplete();
        
        inputManager.addMapping("CLICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, new String[]{"CLICK"});
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(this.gameStateUpdated) {
            drawGameState();
            gameStateUpdated = false;
        }
    }
    
    
    /* *************************** ASSET LOADER METHODS ********************** */
    
    private void loadTerrain() {
        Spatial terrain;
        terrain = assetManager.loadModel("/Scenes/world.j3o");
        rootNode.attachChild(terrain);
    }
    
    private void loadGrid() {
        
        field = new Node("Field");
        grid = assetManager.loadModel("/Models/Grid/Plane.002.mesh.xml");
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
    
    /**
     * Loads the spatials for all ship parts.
     */
    private void loadShip() {
        blueShipBlock = assetManager.loadModel("/Models/ShipBlocks/ShipBlock.j3o");
        blueShipBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        blueShipBlock.setLocalScale(0.95f, 1, 1);
        
        blueShipBow = assetManager.loadModel("/Models/ShipBlocks/ShipBow.j3o");
        blueShipBow.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        blueShipBow.setLocalScale(0.95f, 1, 1);
        
        
        redShipBlock = assetManager.loadModel("/Models/ShipBlocks/ShipBlock.j3o");
        redShipBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
        redShipBlock.setLocalScale(0.95f, 1, 1);
        
        redShipBow = assetManager.loadModel("/Models/ShipBlocks/ShipBow.j3o");
        redShipBow.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
        redShipBow.setLocalScale(0.95f, 1, 1);
    }
    
    private void loadBase() {
        blueBase = assetManager.loadModel("/Models/BaseBlocks/BaseBlock.j3o");
        blueBase.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        blueBase.setLocalScale(0.8f, 1, 1);
        
        redBase = assetManager.loadModel("/Models/BaseBlocks/BaseBlock.j3o");
        redBase.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
        redBase.setLocalScale(0.8f, 1, 1);
        
    }
    
    private void loadRock() {
        rock = assetManager.loadModel("Models/Rock/Cube.mesh.xml");
        rock.setMaterial(assetManager.loadMaterial("Materials/rockMaterial.j3m"));
    }
    
    private void loadRadar() {
        shade = assetManager.loadModel("Models/ShaderBlock/Cube.mesh.xml"); 
        shade.setMaterial(assetManager.loadMaterial("Materials/shade.j3m"));
        shade.setQueueBucket(RenderQueue.Bucket.Transparent);
    }
    
    /* *********************** END OF LOADERS ******************************** */
    
    
    
    private void drawShipPart(int x, int y, ShipDirection dir, int type) {
        Spatial shipPartInstance = null;
        switch(type) {
            case BLOCK_BLUE:
                shipPartInstance = blueShipBlock.clone();
                break;
            case BOW_BLUE:
                shipPartInstance = blueShipBow.clone();
                break;
            case BLOCK_RED:
                shipPartInstance = redShipBlock.clone();
                break;
            case BOW_RED:
                shipPartInstance = redShipBow.clone();
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Unexpected ship unit type in drawShipPart(...)!"));
                break;
        }
        //position the ship unit within the grid
        
        shipPartInstance.setLocalTranslation(2 * (x - 15) + 1, 1, 2 * (y - 15) + 1);
        
        //set the rotation of the ship relative to its direction
        Quaternion rotation = Quaternion.IDENTITY;
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
    
    private void drawBasePart(int x, int y, int type) {
        Spatial baseBlock = null;
        switch(type) {
            case BASE_BLUE:
                baseBlock = blueBase.clone();
                break;
            case BASE_RED:
                baseBlock = redBase.clone();
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null,                         
                        new GameException("Unexpected base block type found!"));
                break;
        }
        
        baseBlock.setLocalTranslation(2 * (x - 15) + 1, 1, 2 * (y - 15) + 1);
        baseBlock.setLocalRotation(Quaternion.IDENTITY);
        field.attachChild(baseBlock);
        this.objectsGrid[x][y] = baseBlock;
    }
    
    
    private void drawCoral(int x, int y) {
        Random rand = new Random();
        Spatial rockInstance = rock.clone();
        Quaternion rockRotation = new Quaternion(new float[] {rand.nextFloat() * 3.14f, rand.nextFloat() * 3.14f, rand.nextFloat() * 3.14f});
        rockInstance.setLocalRotation(rockRotation);
        // x-axis columns; y-axis rows
        rockInstance.setLocalTranslation(2 * (x - 15) + 1, 0, 2 * (y - 15) + 1);
        
        field.attachChild(rockInstance);
        this.objectsGrid[x][y] = rockInstance;
    }
    
    
    private void drawRadarShade(int x, int y) {
        Spatial shadeInstance = shade.clone();
        // x-axis columns; y-axis rows
        shadeInstance.setLocalTranslation(2 * (x - 15) + 1, 0, 2 * (y - 15) + 1);
        field.attachChild(shadeInstance);
        this.objectsGrid[x][y] = shadeInstance;
    }
    
    /**
     * Renders on the map all objects in the provided game state. Also populates
     * the chat log with the messages contained in the game state.
     * @param gs
     */
    public void drawGameState(GameState gs) {
        //use direct reference as the gui does not change the game state
        this.gameState = gs;
        this.gameStateUpdated = true;
    }
    
    /**
     * Draws the GamState that is saved locally. This method is private because
     * only the GUI thread is allowed to modify the scene, so this method is
     * called in the simpleUpdate(...) method and not by an external class and thread.
     */
    private void drawGameState() {
        //draw map objects 
        Map m = gameState.getMap();
        //get the radar visibility for the current player
        boolean[][] visibility = gameState.getRadarVisibility(this.player);
        
        for(int x = 0; x < m.WIDTH; x++) {
            for(int y = 0; y < m.HEIGHT; y++) {
                Vector2 position = new Vector2(x, y);
                if(visibility[x][y]) {
                    if(m.isClear(position)) {
                        //don't draw anything
                    } else {
                        //find out what the object is and draw it
                        GameObject o = m.getObjectAt(position);

                        switch(o.getObjectType()) {
                            case Ship:
                                ShipUnit s = (ShipUnit) o;
                                if(s.isBow()) {
                                    drawShipPart(position.x, position.y, s.getShip().getDirection(), m.isBlue(s.getShip()) ? BOW_BLUE : BOW_RED);
                                } else {
                                    drawShipPart(position.x, position.y, s.getShip().getDirection(), m.isBlue(s.getShip()) ? BLOCK_BLUE : BLOCK_RED);
                                }

                                break;
                            case Base:
                                BaseUnit b = (BaseUnit) o;
                                drawBasePart(position.x, position.y, m.isBlue(b.getBase()) ? BASE_BLUE : BASE_RED);
                                break;
                            case CoralReef:
                                CoralUnit c = (CoralUnit) o;
                                drawCoral(position.x, position.y);
                            default:
                                //do nothing just yet
                                break;
                        }
                    }
                } else {
                    //this cell is not visible by this player
                    drawRadarShade(x, y);
                }
            }//END INNER LOOP
        }//ENDFOR
        
        //we are done with displaying the contents of the game state
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name == "CLICK" && isPressed) {
            // Convert screen click to 3d position
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    // Aim the ray from the clicked spot forwards.
            Ray ray = new Ray(click3d, dir);
            Vector3f clickOnGridPlane = new Vector3f();
            ray.intersectsWherePlane(gridPlane, clickOnGridPlane);
            
            int x, y;
            
            if(clickOnGridPlane.x < 0) {
                x = (( ( (int) clickOnGridPlane.x) / 6) + 14);
            } else {
                x = (( ( (int) clickOnGridPlane.x + 6) / 6) + 14);
            }
            if(clickOnGridPlane.z < 0) {
                y =  (( ( (int) clickOnGridPlane.z) / 6) + 14);
            } else {
                y =  (( ( (int) clickOnGridPlane.z + 6) / 6) + 14);
            }
            
            System.out.println("Click event: " + x + ";" + y);
        }
    }

    /**
     * Interface used by GameGUI to communicate back to the controller which
     * initialized the gui.
     */
    public static interface GameGuiListener {
        /**
         * This method is called when the initialization of the gui
         * is complete.
         */
        public void initializeComplete();
    }
}
