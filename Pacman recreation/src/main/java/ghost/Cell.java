package ghost;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * Objects filling the grid of the displayed game, waka and ghosts interact differently with each cell 
 */
public abstract class Cell{
    /**
     * The app instance associated with a certain cell
     */
    public App app;
    /**
     * Image displayed in app window for each cell
     */
    public PImage sprite;
    
    /**
     * Logic behind how waka interacts with each type of cell
     * @param waka, the waka instance that interacts with the cell
     */
    public abstract void collide(Waka waka);

    /**
     * Checks if waka is able to move into a cell
     * @return passable, true if it is passable, false if otherwise
     */
    public abstract boolean passable();
    /**
     * Loads the sprites for a cell
     */
    public abstract void loadImage();

    /**
     * Draws a sprite onto the app window
     * @param x, horizontal position of the cell in the app window
     * @param y, vertical position of the cell in the app window
     */
    public void draw(int x,int y){
        if (this.sprite==null || this.app==null){
            return;
        }
        this.app.image(this.sprite,x,y);        
    }

}