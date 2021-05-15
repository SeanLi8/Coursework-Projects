package ghost;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * A subcass of fruit which gives waka a point when eaten but also turn ghosts into a frightened state and allows waka to eat the ghosts.
 */
public class SuperFruit extends Fruit{  
    public SuperFruit(App app,Map map){
        super(app,map);
    }
    
    public void collide(Waka waka){
        waka.points+=1;
        for (Ghost x:this.app.ghosts){
            x.mode=Ghost.GhostMode.frightened;
            x.tempFrame=this.app.frameCount;
        }
        this.map.remove(this);
    }

    public boolean passable(){
        return true;
    }
    
    public void loadImage(){
        this.sprite=app.loadImage("src/main/resources/superFruit.png");
    }
    
    
}