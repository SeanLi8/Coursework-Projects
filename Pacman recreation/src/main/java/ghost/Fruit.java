package ghost;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * Cells which waka must interact with to win the game, waka must interact with all such objects and subclasses to win the game
 */
public class Fruit extends Cell{
    public Map map;
    public Fruit(App app,Map map){
        this.app=app;
        this.map=map;
    }
    
    /**
     * Gives waka a point
     * @param waka, the waka instance associated with current game
     */
    public void collide(Waka waka){
        waka.points+=1;
        this.map.remove(this);
    }

    public boolean passable(){
        return true;
    }

    public void loadImage(){
        this.sprite=app.loadImage("src/main/resources/fruit.png");
    }

}