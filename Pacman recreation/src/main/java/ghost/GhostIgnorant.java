package ghost;
import static java.lang.Math.*;

/**
 * Ghost will target waka if it is in chasemode and is more than 8 grid spaces away from waka, otherwise it will target the bottom left corner
 */
public class GhostIgnorant extends Ghost{
    public GhostIgnorant(App app,Map map,Waka waka,int x,int y){
        super(app,map,waka,x,y);
        this.type=GhostType.ignorant;
    }

    public void loadImage(){
        this.sprite=this.app.loadImage("src/main/resources/ignorant.png");
        this.frightenedSprite=this.app.loadImage("src/main/resources/frightened.png");
    }

    public int[] chaseTarget(){
        if (sqrt(pow(this.waka.x-this.x,2)+pow(this.waka.y-this.y,2))<8*16){
            return scatterTarget();
        }
        else{
            return new int[] {this.waka.x,this.waka.y};
        }
    }
    public int[] scatterTarget(){
        return new int[] {0,36*16};
    }
}