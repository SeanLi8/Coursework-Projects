package ghost;

/**
 * Ghost that will target 4 grid spaces in front of waka if in chase mode or the top left corner in scatter mode
 */
public class GhostAmbusher extends Ghost{
    public GhostAmbusher(App app,Map map,Waka waka,int x,int y){
        super(app,map,waka,x,y);
        this.type=GhostType.ambusher;

    }
    
    public void loadImage(){
        this.sprite=this.app.loadImage("src/main/resources/ambusher.png");
        this.frightenedSprite=this.app.loadImage("src/main/resources/frightened.png");
    }

    public int[] chaseTarget(){
        if (this.waka.currentDirection==Direction.up){
            return new int[] {this.waka.x,this.waka.y-(4*16)};
        }
        else if (this.waka.currentDirection==Direction.down){
            return new int[] {this.waka.x,this.waka.y+(4*16)};
        }
        if (this.waka.currentDirection==Direction.right){
            return new int[] {this.waka.x+(4*16),this.waka.y};
        }
        else {
            return new int[] {this.waka.x-(4*16),this.waka.y};
        }

    }

    public int[] scatterTarget(){
        return new int[] {0,0};
    }
}