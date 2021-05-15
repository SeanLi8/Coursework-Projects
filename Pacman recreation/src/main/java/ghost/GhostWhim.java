package ghost;

/**
 * Ghost will target a spot such that is is the vector from the chaser ghost to waka doubled from the with the vector tail at chaser ghost
 */
public class GhostWhim extends Ghost{
    public GhostWhim(App app,Map map,Waka waka,int x,int y){
        super(app,map,waka,x,y);
        this.type=GhostType.whim;
    }
    public void loadImage(){
        this.sprite=this.app.loadImage("src/main/resources/whim.png");
        this.frightenedSprite=this.app.loadImage("src/main/resources/frightened.png");
    }


    public int[] chaseTarget(){
        GhostChaser chaser=null;
        for (Ghost x: this.app.ghosts){
            if (x.type==GhostType.chaser){
                chaser=(GhostChaser) x;
            }
        }
        if (chaser==null){
            return new int[] {this.waka.x,this.waka.y};
        }
        int[] chaserTarget=chaser.chaseTarget();
        int vertComponent=chaserTarget[1]-chaser.y;
        int horComponent=chaserTarget[0]-chaser.x;
        return new int[] {chaser.x+horComponent*2,chaser.y+vertComponent*2};

        
    }
    public int[] scatterTarget(){
        return new int[] {28*16,36*16};
        
    }
}