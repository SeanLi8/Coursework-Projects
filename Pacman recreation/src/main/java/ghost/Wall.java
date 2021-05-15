package ghost;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * Wall objects which waka or ghost cannot walk through
 */
public class Wall extends Cell{
    public String wallType;
    public Wall(App app,String wallType){
        this.app=app;
        this.wallType=wallType;
        
    }
    
    //note that this method is never called because waka can never collide with wall
    public void collide(Waka waka){
    }
    public boolean passable(){
        return false;
    }
    public void loadImage(){
        if (this.wallType.equals("1")){
            this.sprite=app.loadImage("src/main/resources/horizontal.png");
        }
        if (this.wallType.equals("2")){
            this.sprite=app.loadImage("src/main/resources/vertical.png");
        }
        if (this.wallType.equals("3")){
            this.sprite=app.loadImage("src/main/resources/upLeft.png");
        }
        if (this.wallType.equals("4")){
            this.sprite=app.loadImage("src/main/resources/upRight.png");
        }
        if (this.wallType.equals("5")){
            this.sprite=app.loadImage("src/main/resources/downLeft.png");
        }
        if (this.wallType.equals("6")){
            this.sprite=app.loadImage("src/main/resources/downRight.png");
        }
    }

}