package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import processing.core.PFont;


/**
 * Main class of the Waka game, contains references to all objects required for the game and responsible for displaying the game onto a window
 */
public class App extends PApplet {
    /**
     * The width of the window which app is displayed on
     */
    public static final int WIDTH = 448;
    /**
     * The Height of the window which app is displaye don
     */
    public static final int HEIGHT = 576;
    /**
     * The game's single waka object
     */
    public Waka waka;
    /**
     * The game's map object
     */
    public Map map;
    /**
     * The game's ghost objects
     * note that a standard game only has 4 ghosts all of different ghost types
     */
    public ArrayList<Ghost> ghosts;
    /**
     * The game's config reader object
     */
    public ConfigReader config;
    /**
     * A boolean value which tells the app class whether or not it should display the target's of each ghost
     * allows debugging of ghost tracking
     */
    public boolean isDebugging=false;
    /**
     * A boolean value which tells the app class if it should continue to process game logic or not
     * if this is false, the app will display a win or lose screen and then reset the game
     */
    public boolean running=true;
    /**
     * A temperary frame variable used to keep track of time passed since game stops running
     * if 10 seconds have passed since this temporary value, the game will restart
     */
    public int tempFrame=0;
    /**
     * A variable used to hold a font format object used for winning and losing screens
     */
    public PFont font;
    /**
     * A variable that stores the filename of the config file
     */
    public String configName;


    public App() {
        this.configName="src/main/resources/config.json";
        newGame("src/main/resources/config.json");
    }
    public App(String configFile){
        this.configName=configFile;
        newGame(configFile);
    }

    /**
     * Loads pImage objects for the objects in the game and sets framerate to 60
     */
    public void setup() {
        frameRate(60);
        this.font=createFont("PressStart2P-Regular.ttf",40);
        this.map.loadImages();
        this.waka.loadImage();
        for (Ghost x:this.ghosts){
            x.loadImage();
        }
        // Load images
    }

    public void settings() {
        size(WIDTH, HEIGHT);
        
    }
    
    /**
     * Updates state of the game and display it onto a window
     */
    public void draw() { 
        //tick
        if (!running){
            if (frameCount>=this.tempFrame+60*10){
                newGame(this.configName);
                setup();
            }
            return;
        }
        this.waka.tick();
        for (Ghost x:this.ghosts){
            x.tick();
        }   
        if (!running){
            return;
        }     
        //actual draw
        background(0, 0, 0);
        this.map.draw();
        this.waka.draw();
        for (Ghost ghost:this.ghosts){
            ghost.draw();
            if (this.isDebugging && ghost.mode!=Ghost.GhostMode.frightened){
                stroke(255);
                line(ghost.x+8,ghost.y+8,ghost.target[0]+8,ghost.target[1]+8);
            }
        }          
        
    }
    /**
     * Takes in user input, arrow keys control waka object and space bar controls debugging variable
     */
    public void keyPressed(){
        if (keyCode==KeyEvent.VK_SPACE){
            if(this.isDebugging){
                this.isDebugging=false;
            }
            else{
                this.isDebugging=true;
            }
            return;
        }
        if (key==CODED){   
            this.waka.move(keyCode);
        }
    }

    /**
     * Sets new objects the App needs to run, can be used to reset the game
     * @param configFile, name of configuration file
     */
    public void newGame(String configFile){
        this.config=new ConfigReader(configFile);
        this.map=new Map(this,this.config.map);
        this.waka=new Waka(this,this.map);
        this.ghosts=new ArrayList<Ghost>();
        this.running=true;
        this.isDebugging=false;
        for (ArrayList<Integer> x:this.map.ghostPoints){
            if (x.get(0)==1){
                this.ghosts.add(new GhostAmbusher(this,this.map,this.waka,x.get(1),x.get(2)));
            }
            else if (x.get(0)==2){
                this.ghosts.add(new GhostChaser(this,this.map,this.waka,x.get(1),x.get(2)));
            }
            else if (x.get(0)==3){
                this.ghosts.add(new GhostIgnorant(this,this.map,this.waka,x.get(1),x.get(2)));
            }
            else{
                this.ghosts.add(new GhostWhim(this,this.map,this.waka,x.get(1),x.get(2)));
            }
        }

    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
