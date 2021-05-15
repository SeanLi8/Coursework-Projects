package ghost;
/**
 * Ghost will target waka if in chase mode and will target the top right corner if in scatter mode
 */
public class GhostChaser extends Ghost{
    public GhostChaser(App app,Map map,Waka waka,int x,int y){
        super(app,map,waka,x,y);
        this.type=GhostType.chaser;

    }
    public void loadImage(){
        this.sprite=this.app.loadImage("src/main/resources/chaser.png");
        this.frightenedSprite=this.app.loadImage("src/main/resources/frightened.png");
    }

    public int[] chaseTarget(){
        return new int[] {this.waka.x,this.waka.y};
    }
    public int[] scatterTarget(){
        return new int [] {28*16,0};
    }
}