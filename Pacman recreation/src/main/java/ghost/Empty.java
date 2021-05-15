package ghost;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * Empty cell used for grid spaces outside of the playable area and for when fruits are eaten by waka
 */
public class Empty extends Cell{
    public Empty(){
        this.app=null;
        this.sprite=null;
    }
    public void draw(){
        return;
    }
    public void collide(Waka waka){
        return;
    }
    public boolean passable(){
        return true;
    }

    public void loadImage(){

    }

}