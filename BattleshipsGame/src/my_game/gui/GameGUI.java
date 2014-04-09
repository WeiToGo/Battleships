/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_game.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import my_game.models.game_components.BaseUnit;
import my_game.models.game_components.CoralUnit;
import my_game.models.game_components.GameObject;
import my_game.models.game_components.GameObject.GameObjectType;
import my_game.models.game_components.GameState;
import my_game.models.game_components.Map;
import my_game.models.game_components.MidUnit;
import my_game.models.game_components.ShipUnit;
import my_game.models.player_components.Message;
import my_game.models.player_components.Player;
import my_game.util.GameException;
import my_game.util.Misc;
import my_game.util.Positions;
import my_game.util.ShipDirection;
import my_game.util.TurnPositions;
import my_game.util.Vector2;

/**
 * This is the JMonkeyEngine window where the gameplay interface and 3D graphics
 * are displayed.
 * @author Ivo Parvanov
 */
public class GameGUI extends SimpleApplication implements ActionListener {    

    public enum Action {
        Move, Turn, EndTurn, CannonAttack, Mine, TorpedoAttack, Repair, LongRadar, Suicide};
    
    /** Integers used to indicate to the block drawing algorithm what block type to draw. */
    private final static int BASE = 0, BLOCK = 1, BOW = 2, RED = 3, BLUE = 4, NEW = 5, DAMAGED = 6, DESTROYED = 7;
    /** The height at which buttons are displayed. */
    private final static float BUTTONS_Y = 15;
    /** The gap between buttons. */
    private float BUTTONS_GAP = 15;
    /** Constants used in creating the button interface. These are ratios connected to the ratio of the resolution
     * of the buttons to the resolution of 2560x1440. In other words, how would you scale the button to look the same
     * as it looked without scaling on a 2560x1440 screen. */
    private final static float BAR_HEIGHT_RATIO = 14.5454f, BUTTON_HEIGHT_RATIO = 26.1818181818f, BUTTON_WIDTH_RATIO = 16;
    
    private final static Plane gridPlane = new Plane(Vector3f.UNIT_Y, 0);
    /* The elevatedPlane is a plane above the gridPlane used to fix the difficult ship selection issue. */
    private final static Plane elevatedPlane = new Plane(Vector3f.UNIT_Y, 1.5f);
    /** This array contains the radar visibility for each coordinate from the last update. */
    boolean[][] visibility;
    
    Spatial grid, highlight, shade, blueShipBlock, blueRadarBlock, blueShipBow, 
            redShipBlock, redRadarBlock, redShipBow, blueBase, redBase, rock, mine;
    
    /** Interface buttons and other pictures. */
    Picture blackBar, moveButton, turnButton, shootCannonButton, torpedoButton, mineButton, repairButton, longRadar, suicideAttack, endTurnButton;
    
    /** Text showing messages and other info. */
    BitmapText chatText;
    /** Flags keeping track on the state of the three buttons. */
    public boolean moveActivated, turnActivated, cannonActivated, torpedoActivated, mineActivated, repairActivated, longRadarActivated, suicideActivated, endTurnActivated;
    
    /** A grid containing a Spatial at every grid position if there is a ship part there. */
    Spatial[][] objectsGrid, highlightsGrid, radarGrid;
    /** A node containing the game field: grid, ships, bases and corals. */
    Node field, highlightNode;
    Vector3f translation;
    Quaternion rotation;
    Vector3f direction;
    
    /** A reference to the guiListener interface used to communicate back to a controller. */
    GameGuiListener guiListener;
    /** Local reference to a game state which is drawn on frame update. */
    private GameState gameState;
    /** When playing animation, the animation is played by interpolating between the gameState and the updateGameState. */
    private GameState updateState;
    private Animator animation;
    /** A flag set to true every time the drawGameState method is called. */
    private boolean gameStateUpdated, updateStateUpdated;
    /** Reference to the player running the instance of this GUI. This variable
     * is used for determining the radar visibility, chat log and controls. */
    private Player player;
    /**
     * The last highlight positions.
     */
    private ArrayList<Vector2> highlightPos;
    private boolean highlightPosUpdated, clearHighlight;

    /**
     * 
     * @param width
     * @param height
     * @param g
     * @param p The player who is running the instance of this GUI.
     */
    public GameGUI(int width, int height, GameGuiListener g, Player p) {
        objectsGrid = new Spatial[width][height];
        radarGrid = new Spatial[width][height];
        highlightsGrid = new Spatial[width][height];
        guiListener = g;
        this.player = p;
    }
    
    @Override
    public void simpleInitApp() {
        
        setDisplayStatView(false); setDisplayFps(false);
        
        loadTerrain();
        loadGrid();
        loadLights();
        loadShip();
        loadBase();
        loadRock();
        loadMine();
        loadRadar();
        loadHUD();
        loadHighlight();
        
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
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        inputManager.addMapping("CLICK", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("ESC", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, new String[]{"CLICK", "ESC"});

        
        
        //report to the guiListener that init. is complete so he can now send requests to the gui
        guiListener.initializeComplete();
        animation = new Animator(assetManager, field);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(this.gameStateUpdated) {
            drawGameState();
            gameStateUpdated = false;
        }
        
        if(this.updateStateUpdated) {
            if(updateGameState()) { //if the animation is complete
                updateStateUpdated = false; 
            }
        }
       
        if(clearHighlight) {
            clearHighlight();
        }
        if(highlightPos != null && !clearHighlight && this.highlightPosUpdated ) {
            highlightPositions();
            highlightPosUpdated = false;
        }
        if(moveActivated) {
            this.moveButton.setImage(assetManager, "/Interface/moveEnabled.png", true);
        } else {
            this.moveButton.setImage(assetManager, "/Interface/moveDisabled.png", true);
        }
        if(turnActivated) {
            this.turnButton.setImage(assetManager, "/Interface/turnEnabled.png", true);
        } else {
            this.turnButton.setImage(assetManager, "/Interface/turnDisabled.png", true);
        }
        if(cannonActivated) {
            this.shootCannonButton.setImage(assetManager, "/Interface/attackEnabled.png", true);
        } else {
            this.shootCannonButton.setImage(assetManager, "/Interface/attackDisabled.png", true);
        }
        if(longRadarActivated) {
            this.longRadar.setImage(assetManager, "/Interface/longRadarEnabled.png", true);
        } else {
            this.longRadar.setImage(assetManager, "/Interface/longRadarDisabled.png", true);
        }
        if(torpedoActivated) {
            this.torpedoButton.setImage(assetManager, "/Interface/torpedoEnabled.png", true);
        } else {
            this.torpedoButton.setImage(assetManager, "/Interface/torpedoDisabled.png", true);
        }
        if(mineActivated) {
            this.mineButton.setImage(assetManager, "/Interface/mineEnabled.png", true);
        } else {
            this.mineButton.setImage(assetManager, "/Interface/mineDisabled.png", true);
        }
        if(repairActivated) {
            this.repairButton.setImage(assetManager, "/Interface/repairEnabled.png", true);
        } else {
            this.repairButton.setImage(assetManager, "/Interface/repairDisabled.png", true);
        }
        if(suicideActivated) {
            this.suicideAttack.setImage(assetManager, "/Interface/suicideEnabled.png", true);
        } else {
            this.suicideAttack.setImage(assetManager, "/Interface/suicideDisabled.png", true);
        }
        if(endTurnActivated) {
            this.endTurnButton.setImage(assetManager, "/Interface/endTurnEnabled.png", true);
        } else {
            this.endTurnButton.setImage(assetManager, "/Interface/endTurnDisabled.png", true);
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
        
        highlightNode = new Node("Field");
        highlightNode.setLocalScale(3, 1.5f, 3);
        highlightNode.setLocalTranslation(0, 0.4f, 0);
        rootNode.attachChild(highlightNode);
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
        
        blueRadarBlock = assetManager.loadModel("/Models/RadarShipBlock/RadarShipBlock.j3o");
        blueRadarBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        blueRadarBlock.setLocalScale(0.95f, 1, 1);
        
        blueShipBow = assetManager.loadModel("/Models/ShipBlocks/ShipBow.j3o");
        blueShipBow.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
        blueShipBow.setLocalScale(0.95f, 1, 1);
        
        
        redShipBlock = assetManager.loadModel("/Models/ShipBlocks/ShipBlock.j3o");
        redShipBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
        redShipBlock.setLocalScale(0.95f, 1, 1);
        
        redRadarBlock = assetManager.loadModel("/Models/RadarShipBlock/RadarShipBlock.j3o");
        redRadarBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
        redRadarBlock.setLocalScale(0.95f, 1, 1);
        
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
    
    private void loadMine() {
        mine = assetManager.loadModel("/Models/mine/mine.j3o");
        mine.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
    }
    
    private void loadRadar() {
        shade = assetManager.loadModel("Models/ShaderBlock/Cube.mesh.xml"); 
        shade.setMaterial(assetManager.loadMaterial("Materials/shade.j3m"));
        shade.setQueueBucket(RenderQueue.Bucket.Transparent);
    }
    
    private void loadHUD() {
        //load the black bar on the bottom of the window
        blackBar = new Picture("Bar");
        blackBar.setImage(assetManager, "/Interface/blackbar.png", true);
        blackBar.setWidth(settings.getWidth());
        float height = settings.getHeight() / BAR_HEIGHT_RATIO;
        blackBar.setHeight(height);
        blackBar.setPosition(0, 0);
        
        blackBar.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(blackBar);
        
        moveButton = new Picture("MoveButton");
        moveButton.setImage(assetManager, "/Interface/moveDisabled.png", true);
        float width = settings.getWidth() / BUTTON_WIDTH_RATIO;
        moveButton.setWidth(width);
        height = settings.getHeight() / BUTTON_HEIGHT_RATIO;
        moveButton.setHeight(height);
        float resolutionAdjustedGap = BUTTONS_GAP * (settings.getWidth() / 2560.0f);
        float resolutionAdjustedY = BUTTONS_Y * (settings.getHeight() / 1440.0f);
        
        moveButton.setPosition(resolutionAdjustedGap, resolutionAdjustedY);
        
        moveButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(moveButton);
        //*************************
        turnButton = new Picture("TurnButton");
        turnButton.setImage(assetManager, "/Interface/turnDisabled.png", true);
        
        turnButton.setWidth(width);
        turnButton.setHeight(height);
        turnButton.setPosition(2 * resolutionAdjustedGap + width , resolutionAdjustedY);
        
        turnButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(turnButton);
        //*************************
        shootCannonButton = new Picture("ShootCannonButton");
        shootCannonButton.setImage(assetManager, "/Interface/attackDisabled.png", true);
        
        shootCannonButton.setWidth(width);
        shootCannonButton.setHeight(height);
        shootCannonButton.setPosition(3 * resolutionAdjustedGap +  2 * width , resolutionAdjustedY);
        
        shootCannonButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(shootCannonButton);
        //**************************
        //init. torpedo button
        torpedoButton = new Picture("TorpedoButton");
        torpedoButton.setImage(assetManager, "/Interface/torpedoDisabled.png", true);
        
        torpedoButton.setWidth(width);
        torpedoButton.setHeight(height);
        torpedoButton.setPosition(4 * resolutionAdjustedGap +  3 * width , resolutionAdjustedY);
        
        torpedoButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(torpedoButton);
        //**************************
        //**************************
        //init. mine button
        mineButton = new Picture("MineButton");
        mineButton.setImage(assetManager, "/Interface/mineDisabled.png", true);
        
        mineButton.setWidth(width);
        mineButton.setHeight(height);
        mineButton.setPosition(5 * resolutionAdjustedGap +  4 * width , resolutionAdjustedY);
        
        mineButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(mineButton);
        //**************************
        //init. end turn button
        repairButton = new Picture("RepairButton");
        repairButton.setImage(assetManager, "/Interface/repairDisabled.png", true);
        
        repairButton.setWidth(width);
        repairButton.setHeight(height);
        repairButton.setPosition(6 * resolutionAdjustedGap +  5 * width , resolutionAdjustedY);
        
        repairButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(repairButton);
        //**************************
        //init. end turn button
        longRadar = new Picture("LongRadarButton");
        longRadar.setImage(assetManager, "/Interface/longRadarDisabled.png", true);
        
        longRadar.setWidth(width);
        longRadar.setHeight(height);
        longRadar.setPosition(7 * resolutionAdjustedGap +  6 * width , resolutionAdjustedY);
        
        longRadar.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(longRadar);
        //**************************
        //init. end turn button
        suicideAttack = new Picture("SuicideAttack");
        suicideAttack.setImage(assetManager, "/Interface/suicideDisabled.png", true);
        
        suicideAttack.setWidth(width);
        suicideAttack.setHeight(height);
        suicideAttack.setPosition(8 * resolutionAdjustedGap +  7 * width , resolutionAdjustedY);
        
        suicideAttack.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(suicideAttack);
        //**************************
        //init. end turn button
        endTurnButton = new Picture("EndTurnButton");
        endTurnButton.setImage(assetManager, "/Interface/endTurnDisabled.png", true);
        
        endTurnButton.setWidth(width);
        endTurnButton.setHeight(height);
        endTurnButton.setPosition(9 * resolutionAdjustedGap +  8 * width , resolutionAdjustedY);
        
        endTurnButton.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(endTurnButton);
        //**************************
        //init. the text for the chat log
        chatText = new BitmapText(guiFont, false);  
        chatText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        chatText.setColor(ColorRGBA.White);                             // font color
        chatText.setText("");                                            // the text
        chatText.setLocalTranslation(getButtonWidth() * 10 , chatText.getLineHeight() + getButtonGap(), 0); // position
        chatText.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.attachChild(chatText);
        
    }
    
    private void loadHighlight() {
        highlight = assetManager.loadModel("Models/ShaderBlock/Cube.mesh.xml");
        highlight.setMaterial(assetManager.loadMaterial("Materials/shade_green.j3m"));
        highlight.setQueueBucket(RenderQueue.Bucket.Translucent);
    }
    
    /* *********************** END OF LOADERS ******************************** */
    
    /**
     * Sets a flag in the GameGUI which will clear all highlights at the next update.
     */
    public void requestClearHighlight() {
        this.highlightPosUpdated = false;
        this.clearHighlight = true;
    }
    
    /**
     * Clears the highlight in the spaces specified in the highlight Positions object.
     * @param highlight 
     */
    private void clearHighlight() {        
        for(int x = 0; x < highlightsGrid.length; x++) {
            for(int y = 0; y < highlightsGrid[0].length; y++) {
                //Spatial tmp = highlightsGrid[x][y];
                highlightsGrid[x][y] = null;
            }
        }
        
        highlightNode.detachAllChildren();
        clearHighlight = false;
        //now notify all threads waiting for the clear to complete
        synchronized(this) {
            this.notifyAll();
        }
    }
    
    /**
     * Displays a highlight over the specified position.
     * @param highlight 
     */    
    public void highlightPositions(Positions highlights) {
        this.highlightPos = highlights.getAll();
        this.highlightPosUpdated = true;
    }
    
    public void highlightPositions(TurnPositions highlights) {
        this.highlightPos = highlights.getAll();
        this.highlightPosUpdated = true;
    }
    
    public void highlightPositions(ArrayList<Vector2> highlights) {
        this.highlightPos = highlights;
        this.highlightPosUpdated = true;
    }
    
    private void highlightPositions() {
        for(Vector2 v: highlightPos) {
                drawHighlight(v.x, v.y);
        }
    }
    
    /**
     * Makes sure the gui thread is not currently clearing or waiting to clear the
     * highlight.
     */
    private void waitForClear() {
        synchronized(this) {
            try {
                while(this.clearHighlight) {
                    this.wait();
                }
            } catch(InterruptedException ignore) {}
        }
    }
    
    private void drawHighlight(int x, int y) {
        if(x >= 0 && y >= 0 && x < gameState.getMap().WIDTH && y < gameState.getMap().HEIGHT) {
            Spatial highlightInstance = highlight.clone();
                    // x-axis columns; y-axis rows
            highlightInstance.setLocalTranslation(2 * (x - 15) + 1, 0, 2 * (y - 15) + 1);
            if(highlightsGrid[x][y] == null) {
                highlightNode.attachChild(highlightInstance);
                this.highlightsGrid[x][y] = highlightInstance;
            }
        }
    }
    
    private void drawMine(int x, int y) {
        Spatial mineInstance = mine.clone();
        // x-axis columns; y-axis rows
        mineInstance.setLocalTranslation(2 * (x - 15) + 1, 0, 2 * (y - 15) + 1);
        
        field.attachChild(mineInstance);
        this.objectsGrid[x][y] = mineInstance;
    }
    
    private void drawShipPart(int x, int y, ShipDirection dir, int type, int colour, int damage) {
        Spatial shipPartInstance = null;
        switch(type) {
            case BLOCK:
                shipPartInstance = blueShipBlock.clone();
                break;
            case BOW:
                shipPartInstance = blueShipBow.clone();
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Unexpected ship unit type in drawShipPart(...)!"));
                break;
        }
        
        switch(colour) {
            case RED:
                if(damage == NEW) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
                } else if(damage == DAMAGED) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialRed.j3m"));
                } else {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialRed.j3m"));
                }
                break;
            case BLUE:
                if(damage == NEW) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
                } else if(damage == DAMAGED) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialBlue.j3m"));
                } else {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialBlue.j3m"));
                }
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Unexpected colour!"));
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
            case South:
                rotation = new Quaternion(new float[] {0f, (float) Math.PI, 0f});
                break;
            case East:
                rotation = new Quaternion(new float[] {0f, (float) (3 * Math.PI) / 2, 0f});
                break;
            case North:
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
    
    private void drawBasePart(int x, int y, int colour, int damage) {
        Spatial baseBlock = blueBase.clone();
        switch(colour) {
            case RED:
                if(damage == NEW) {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
                } else if(damage == DAMAGED) {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialRed.j3m"));
                } else {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialRed.j3m"));
                }
                break;
            case BLUE:
                if(damage == NEW) {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
                } else if(damage == DAMAGED) {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialBlue.j3m"));
                } else {
                    baseBlock.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialBlue.j3m"));
                }
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
        this.radarGrid[x][y] = shadeInstance;
    }
    
    /**
     * Displays the messages of a chatlog
     * @param chatLog 
     */
    private void drawChatLog(List<Message> chatLog) {
        //for now only display the last message
        Message m = null;
        if(chatLog.size() > 0) {
          m = chatLog.get(chatLog.size() - 1);

          switch(m.getMessageType()) {
              case Game:
                  chatText.setColor(ColorRGBA.Cyan);
                  break;
              case NetworkError:
                  chatText.setColor(ColorRGBA.Red);
                  break;
              case Chat:
                  chatText.setColor(ColorRGBA.White);
                  break;
              case NetworkInfo:
                  chatText.setColor(ColorRGBA.DarkGray);
          }

          chatText.setText(m.text);
        }
    }
    
    /**
     * Renders on the map all objects in the provided game state. Also populates
     * the chat log with the messages contained in the game state.
     * @param gs
     */
    public void drawGameState(GameState gs) {
        //use direct reference as the gui does not change the game state
        this.gameState = new GameState(gs);
        this.gameStateUpdated = true;
    }
    
    /**
     * This method should be called if a player has taken a single action on the 
     * GameState which was lastly rendered on this gui. What this method will do is
     * figure out the single action that the player took and play the single animation
     * which would bring the previous GameState to the current one. 
     * NOTE: Undetermined behaviour if method called inappropriately.
     * @param updateState The GameState which is one action ahead of the current GameState in the GUI.
     */
    public void updateGameState(GameState updateState) {
        this.updateState = new GameState(updateState);
        this.updateStateUpdated = true;
        animation.prepareNewAnimation();
    }
    
    private boolean updateGameState() {
        if(animation.isPreparing()) {
            switch(updateState.previousAction.getActionType()) {
                case Move:
                    //set the radar visibility to the current radar UNION the radar in the update gameState
                    this.visibility = mergeVisibility(updateState, this.gameState, this.player);
                    updateRadar(gameState);
                    //now we are ready to animate the movement of the ship
                    animation.startMoveAnimation(updateState, objectsGrid);
                    break;
                case CannonAttack:
                    animation.startCannonAnimation(updateState, objectsGrid);
                    break;
                default:
                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, new GameException("Unknown or unimplemented action taken."));
                    break;
            }
            return false;
        } else if(animation.isAnimating()) {
            switch(updateState.previousAction.getActionType()) {
                case Move:
                    //move the ship a little bit towards the destination
                    animation.nextMoveFrame();
                    break;
                case CannonAttack:
                    animation.nextCannonFrame();
                    break;
                default:
                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, new GameException("Unknown or unimplemented action taken."));
                    break;
            }
            return false;
        } else {
            //DONE
            gameState = new GameState(updateState);
            this.visibility = updateState.getRadarVisibility(player);
            updateShips(gameState);
            updateRadar(gameState);
            return true;
        }
    }
    
    /** Redraws all ships and bases. */
    private void updateShips(GameState gs) {
        for(int x = 0; x < objectsGrid.length; x++) {
            for(int y = 0; y < objectsGrid[0].length; y++) {
                if(objectsGrid[x][y] != null) {
                    field.detachChild(objectsGrid[x][y]);
                    objectsGrid[x][y] = null;
                }
                Vector2 position = new Vector2(x, y);
                if(!gameState.getMap().isCoral(position)) {
                    drawMapObject(position, gs.getMap());
                    if(objectsGrid[x][y] != null && !visibility[x][y]) {
                        objectsGrid[x][y].setCullHint(CullHint.Always);
                    }
                }
            }
        }
    }
    
    /**
     * Renders the radar on screen.
     */
    public void updateRadar(GameState state) {
        for(int x = 0; x < visibility.length; x++) {
            for(int y = 0; y < visibility[0].length; y++) {
                //update only mismatches between what is rendered on screen and what is in the visibility matrix
                if(visibility[x][y]) {
                    //this cell should be visible
                    radarGrid[x][y].setCullHint(CullHint.Always);
                    //do not redraw coral reefs
                    if(objectsGrid[x][y] != null) {
                        //this.drawMapObject(new Vector2(x, y), state.getMap());
                        objectsGrid[x][y].setCullHint(CullHint.Never);
                    }
                } else if(!visibility[x][y]) {
                    //this cell should not be visible
                    GameObject o = gameState.getMap().getObjectAt(new Vector2(x, y));
                    //do not repaint or hide coral reefs
                    if(objectsGrid[x][y] != null && o != null && !o.getObjectType().equals(GameObjectType.CoralReef)) {
                        //field.detachChild(objectsGrid[x][y]);
                        objectsGrid[x][y].setCullHint(CullHint.Always);
                    }
                    //make the radar shade visible
                    radarGrid[x][y].setCullHint(CullHint.Never);
                    //objectsGrid[x][y] = null;
                }
            }
        }
    }
    
    /**
     * Returns the union of the radar visibilities of g1 and g2.
     * @param g1
     * @param g2
     * @param p Which player are we generating the radar visibility for?
     * @return 
     */
    public static boolean[][] mergeVisibility(GameState g1, GameState g2, Player p) {
        boolean[][] map1 = g1.getRadarVisibility(p);
        boolean[][] map2 = g2.getRadarVisibility(p);
        //unite the two matrices
        for(int x = 0; x < map1.length; x++) {
            for(int y = 0; y < map1[0].length; y++) {
                if(map1[x][y] || map2[x][y]) {
                    map1[x][y] = true;      //do the updates in place inside map1
                } else {
                    map1[x][y] = false;
                }
            }
        }
        return map1;
    }
    
    /**
     * Draws the GamState that is saved locally. This method is private because
     * only the GUI thread is allowed to modify the scene, so this method is
     * called in the simpleUpdate(...) method and not by an external class and thread.
     */
    private void drawGameState() {
        field.detachAllChildren();
        clearObjectsGrid();
        clearRadarGrid();
        clearHighlight();
        field.attachChild(grid);
        
        //display the last message of the chatLog in the chatText
        //TODO make this show the whole chat log scrollable
        drawChatLog(gameState.getChatLog(player));
        
        //draw map objects 
        Map m = gameState.getMap();
        //get the radar visibility for the current player
        visibility = gameState.getRadarVisibility(this.player);
        
        for(int x = 0; x < m.WIDTH; x++) {
            for(int y = 0; y < m.HEIGHT; y++) {
                Vector2 position = new Vector2(x, y);
                if(visibility[x][y]) {
                   drawMapObject(position, m);
                   drawRadarShade(x, y);
                   //the radar shade at this coordinate should be invisible
                   radarGrid[x][y].setCullHint(CullHint.Always);
                   if(gameState.getMap().isMine(position)) {
                       //check if within sonar range
                       if(!gameState.isSeenBySonar(position, player)) {
                           objectsGrid[x][y].setCullHint(CullHint.Always);
                       }
                   }
                } else {
                    //this cell is not visible by this player
                    drawRadarShade(x, y);
                    drawMapObject(position, m);
                    if(objectsGrid[x][y] != null) {
                        objectsGrid[x][y].setCullHint(CullHint.Always); //make this piece invisible
                    }
                }
            }//END INNER LOOP
        }//ENDFOR
        
        //we are done with displaying the contents of the game state
    }
    
    private void drawMapObject(Vector2 position, Map m) {
        if(m.isClear(position)) {
            //don't draw anything
        } else {
            //find out what the object is and draw it
            GameObject o = m.getObjectAt(position);

            switch(o.getObjectType()) {
                case MidUnit:
                    MidUnit mu = (MidUnit) o;
                    int damage1 = 0;
                    if(mu.isHealthy()) {
                        damage1 = NEW;
                    } else if(mu.isDestroyed()) {
                        damage1 = DESTROYED;
                    } else {
                        damage1 = DAMAGED;
                    }
                    drawMidShipPart(position.x, position.y, mu.getShip().getDirection(), m.isBlue(mu.getShip()) ? BLUE : RED, damage1);
                    break;
                case Ship:
                    ShipUnit s = (ShipUnit) o;
                    int damage = 0;
                    if(s.isHealthy()) {
                        damage = NEW;
                    } else if(s.isDestroyed()) {
                        damage = DESTROYED;
                    } else {
                        damage = DAMAGED;
                    }
                    if(s.isBow()) {
                        drawShipPart(position.x, position.y, s.getShip().getDirection(), BOW, m.isBlue(s.getShip()) ? BLUE : RED, damage);
                    } else {
                        drawShipPart(position.x, position.y, s.getShip().getDirection(), BLOCK, m.isBlue(s.getShip()) ? BLUE : RED, damage);
                    }

                    break;
                case Base:
                    BaseUnit b = (BaseUnit) o;
                    damage = 0;
                    if(b.isHealthy()) {
                        damage = NEW;
                    } else if(b.isDestoryed()) {
                        damage = DESTROYED;
                    } else {
                        damage = DAMAGED;
                    }
                    drawBasePart(position.x, position.y, m.isBlue(b.getBase()) ? BLUE : RED, damage);
                    break;
                case CoralReef:
                    //CoralUnit c = (CoralUnit) o;
                    drawCoral(position.x, position.y);
                case Mine:
                    drawMine(position.x, position.y);
                default:
                    //do nothing just yet
                    break;
            }
        }
    }

    
    private void clearObjectsGrid() {
        for(int x = 0; x < objectsGrid.length; x++) {
            for(int y = 0; y < objectsGrid[0].length; y++) {
                objectsGrid[x][y] = null;
            }
        }
    }
    
    private void clearRadarGrid() {
        for(int x = 0; x < radarGrid.length; x++) {
            for(int y = 0; y < radarGrid[0].length; y++) {
                radarGrid[x][y] = null;
            }
        }
    }
    
    /**
     * @return The height of the UI bar element at the bottom of the screen.
     */
    private float getBarHeight() {
        return settings.getHeight() / BAR_HEIGHT_RATIO;
    }
    
    /**
     * @return The width of every button in the UI.
     */
    private float getButtonWidth() {
        return settings.getWidth() / BUTTON_WIDTH_RATIO;
    }
    
    /**
     * @return The height of every button in the UI.
     */
    private float getButtonHeight() {
        return settings.getHeight() / BUTTON_HEIGHT_RATIO;
    }
    
    /**
     * @return The gap between buttons adjusted for the current resolution.
     */
    private float getButtonGap() {
        return BUTTONS_GAP * (settings.getWidth() / 2560.0f);
    }
    
    /**
     * @return The height at which every button is placed, adjusted for the current resolution.
     */
    private float getButtonY() {
        return BUTTONS_Y * (settings.getHeight() / 1440.0f);
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("CLICK") && isPressed) {
            // Convert screen click to 3d position
            Vector2f click2d = inputManager.getCursorPosition();
            if(click2d.y > getBarHeight()) {
                //This is a click inside the gaming area, treat as ship selection
                
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                        // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                Vector3f clickOnGridPlane = new Vector3f();
                ray.intersectsWherePlane(elevatedPlane, clickOnGridPlane);

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

                guiListener.onMouseClick(x, y);
            } else {
                //this is a click in the black bar area, treat as a click on one of 
                //the ui buttons; find out which button was pressed
                float x = click2d.x;
                float y = click2d.y;
                
                //first find out if we are within the range of buttons on the y axis
                if(y >= this.getButtonY() && y <= this.getButtonHeight() + this.getButtonY()) {
                    //we are in vertical range of buttons
                    //find out which button is clicked
                    float width = this.getButtonWidth() + this.getButtonGap();
                    //index is the number of button widths from the 0 x-coord. to the clicked x-coord.
                    int index = (int) (x / width);
                    switch(index) {
                        case 0:
                            //move button is pressed
                            guiListener.onButtonPressed(Action.Move);
                            break;
                        case 1:
                            guiListener.onButtonPressed(Action.Turn);
                            break;
                        case 2:
                            guiListener.onButtonPressed(Action.CannonAttack);
                            break;
                        case 3:
                            guiListener.onButtonPressed(Action.TorpedoAttack);
                            break;
                        case 4:
                            guiListener.onButtonPressed(Action.Mine);
                            break;
                        case 5:
                            guiListener.onButtonPressed(Action.Repair);
                            break;
                        case 6:
                            guiListener.onButtonPressed(Action.LongRadar);
                            break;
                        case 7:
                            guiListener.onButtonPressed(Action.Suicide);
                            break;
                        case 8:
                            guiListener.onButtonPressed(Action.EndTurn);
                            break;
                        default:
                            //ignore clicks outside the buttons
                    }
                }
            }
        } else if(name.equals("ESC")) {
            escapeMenu();
        }
    }

    public void escapeMenu(){
        if (JOptionPane.showConfirmDialog(null, 
                "Would you like to save the game and quit?", "", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // yes option
            JTextField fileName = new JTextField(); 
            Object[] message = {"File name", fileName.getText()};
            //send text of filename 
            String option = JOptionPane.showInputDialog(null, message, 
                    "Add New", JOptionPane.OK_CANCEL_OPTION); 
            System.out.println(fileName.getText());
            if(option!=null){ 
                try { 
                    if(this.gameState.saveGame(option)) {
                        this.guiListener.endGame(); 
                        this.stop(); 
                    }
                } catch (
                    IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } //TODO stop the game
                
            } else{
                JOptionPane.showConfirmDialog(null,"Save Failed"); 
            }
        }  
    }//Chat Conversation End


    
    /**
     * Enables and disables the action buttons of the game gui.
     * @param active 
     */
    public void setActionButtonsEnabled(boolean active) {
        this.moveActivated = active;
        this.cannonActivated = active;
        this.turnActivated = active;
        this.mineActivated = active;
        this.torpedoActivated = active;
        this.repairActivated = active;
        this.longRadarActivated = active;
        this.suicideActivated = active;
    }
    
    /**
     * Enables and disables all buttons of the game gui.
     * @param active 
     */
    public void setAllButtonsEnabled(boolean active) {
        this.setActionButtonsEnabled(active);
        this.endTurnActivated = active;
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
        
        /**
         * This method is called when the player clicks on the screen.
         * @param x
         * @param y 
         */
        public void onMouseClick(int x, int y);

        /**
         * This method is called whenever an action button is pressed in the gui.
         * @param action
         */
        public void onButtonPressed(Action action);
        
        public void endGame();
    }
    
    /**
     * @param x
     * @param y
     * @return True if the coordinate pressed is currently revealed on the map, i.e. it is in radar range.
     */
    public boolean isVisible(int x, int y) {
        return visibility[x][y];
    }
    
        private void drawMidShipPart(int x, int y, ShipDirection dir, int colour, int damage) {
        Spatial shipPartInstance = blueRadarBlock.clone();
        
        switch(colour) {
            case RED:
                if(damage == NEW) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialRed.j3m"));
                } else if(damage == DAMAGED) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialRed.j3m"));
                } else {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialRed.j3m"));
                }
                break;
            case BLUE:
                if(damage == NEW) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/baseMaterialBlue.j3m"));
                } else if(damage == DAMAGED) {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/damagedMaterialBlue.j3m"));
                } else {
                    shipPartInstance.setMaterial(assetManager.loadMaterial("/Materials/destroyedMaterialBlue.j3m"));
                }
                break;
            default:
                Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, 
                        new GameException("Unexpected colour!"));
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
            case South:
                rotation = new Quaternion(new float[] {0f, (float) Math.PI, 0f});
                break;
            case East:
                rotation = new Quaternion(new float[] {0f, (float) (3 * Math.PI) / 2, 0f});
                break;
            case North:
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
