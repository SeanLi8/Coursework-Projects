package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.awt.event.KeyEvent;
import static java.lang.Math.*;
import java.util.ArrayList;

/**
 * Ghost object which can kill wakaand decrease its lvies upon interaction
 */
public abstract class Ghost extends MovingObject{
    
    /**
     * The Waka object associated with the current game instance
     */
    public Waka waka;
    /**
     * The position on the app window which the ghost is targetting
     * this array has 2 elements, the first one being the horizontal position and the second being the vertical position
     */
    public int[] target;
    /**
     * The current mode of the ghost
     */
    public GhostMode mode;
    /**
     * The type of the ghost
     */
    public GhostType type;
    /**
     * A timer used to keep track of what mode the ghost should be in
     */
    public int intervalTimer=0;
    /**
     * An index which tells the ghost which mode it should be in
     * note that if index is even then ghost is in scatter mode and if it is odd ghost is in chase mode
     */
    public int intervalIndex=0;
    /**
     * A temporary variable used to time the duration of the ghosts frightened mode
     */
    public int tempFrame=0;
    /**
     * The frightened sprite of the ghost
     */
    public PImage frightenedSprite;
    /**
     * A boolean used to check if the ghost entered frightened mode by interacting with a soda object
     */
    public boolean frightenedBySoda;

    /**
     * The types of ghosts in the game, the difference between the ghosts is their targetting logic
     */
    public enum GhostType{
        ambusher,
        chaser,
        ignorant,
        whim;
    }

    /**
     * The modes of ghosts in the game, each mode has different targetting logic and interactions with waka
     * frightened ghosts move randomly
     */
    public enum GhostMode{
        chase,
        scatter,
        frightened;
    }


    public Ghost(App app,Map map,Waka waka,int x,int y){
        this.app=app;
        this.map=map;
        this.waka=waka;
        this.x=x;
        this.xStart=this.x;
        this.y=y;
        this.yStart=this.y;
        this.xVel=0;
        this.yVel=0;
        this.target=new int[2];
        this.mode=GhostMode.scatter;
        moveRandom();
    }

    public abstract void loadImage();
 
    /**
     * Updates targetting and movement of ghost object
     */
    public void tick(){
        if (this.mode==GhostMode.frightened){
            if (this.app.frameCount>=this.tempFrame+60*10){
                chooseMode();
                this.frightenedBySoda=false;
            }
        }
        else{
            if (this.app.frameCount%60==0){
                this.intervalTimer++;          
            }
            chooseMode();
            //only choose to update target here for the debug mode to look good
            if (this.mode==GhostMode.scatter){
                this.target=scatterTarget();
            }
            else{
                this.target=chaseTarget();
            }
        }
        tickMovement();
    }

    /**
     * Updates position of ghost object
     */
    public void tickMovement(){
        if (checkAtIntersection()){
            if (this.mode==GhostMode.frightened){
                moveRandom();
                this.x+=this.xVel;
                this.y+=this.yVel;
                return;
            }
            this.currentDirection=decideTurn(getPossibleTurns()); 
            if (this.currentDirection==Direction.down){
                this.xVel=0;
                this.yVel=this.app.config.speed;
            }
            else if (this.currentDirection==Direction.up){
                this.xVel=0;
                this.yVel=-this.app.config.speed;
            }
            else if (this.currentDirection==Direction.right){
                this.yVel=0;
                this.xVel=this.app.config.speed;
            }
            else{
                this.yVel=0;
                this.xVel=-this.app.config.speed;
            }
        }
        this.x+=this.xVel;
        this.y+=this.yVel;
    }

    /**
     * Decides what mode the ghost should be in
     */
    public void chooseMode(){
        if (this.intervalTimer>=this.app.config.modeLengths.get(this.intervalIndex)){
            this.intervalIndex=(this.intervalIndex+1)%this.app.config.modeLengths.size();
            this.intervalTimer=0;
        }
        if (this.intervalIndex%2==0){
            this.mode=GhostMode.scatter;
        }
        else{
            this.mode=GhostMode.chase;
        }   
    }

    /**
     * returns the target of a a ghost in scatter mode
     * @return an array with horizontal and vertical postion of target
     */
    public abstract int[] scatterTarget();
    /**
     * returns the target of a ghost in chase mode
     * @return an array with horizontal and vertical postion of target
     */
    public abstract int[] chaseTarget();

    /**
     * Given a list of possible turns that can be made, method returns the ideal direction ghost should mode to reach target
     * @param possible, a list a possible directions ghost can move
     * @return  the ideal direction for ghost to turn
     */
    public Direction decideTurn(ArrayList<Direction> possible){
        int xDist=this.target[0]-this.x;
        int yDist=this.target[1]-this.y;
        Direction vertPreferred;
        Direction horPreferred;
        if (xDist>0){
            horPreferred=Direction.right;
        }
        else{
            horPreferred=Direction.left;
        }
        if (yDist>0){
            vertPreferred=Direction.down;
        }
        else{
            vertPreferred=Direction.up;
        }
        if (possible.contains(horPreferred) && possible.contains(vertPreferred)){
            if (abs(xDist)>abs(yDist)){
                return horPreferred;
            }
            else{
                return vertPreferred;
            }
        }
        if (possible.contains(horPreferred)){
            return horPreferred;
            
        }
        if(possible.contains(vertPreferred)){
            return vertPreferred;
        }
        if (possible.contains(horPreferred.getReverse()) && possible.contains(vertPreferred.getReverse())){
            if (abs(xDist)>abs(yDist)){
                return vertPreferred.getReverse();
            }
            else{
                return horPreferred.getReverse();
            }
        }
        return possible.get(0);
    }

    /**
     * Uses position of ghost and current direction of movement to determine what possible directions it can go
     * note that ghosts will not move backwards
     * @return possible,a list of possible turns
     */
    public ArrayList<Direction> getPossibleTurns(){
        ArrayList<Direction> possible=new ArrayList<Direction>();
        if (this.checkPassable("Vertical",-1)){
            possible.add(Direction.up);
        }
        if (this.checkPassable("Vertical",1)){
            possible.add(Direction.down);
        }
        if (this.checkPassable("Horizontal",1)){
            possible.add(Direction.right);
        }
        if (this.checkPassable("Horizontal",-1)){
            possible.add(Direction.left);
        }
        possible.remove(this.currentDirection.getReverse());
        
        return possible;
    }

   

    public void draw(){
        if (this.mode==GhostMode.frightened){
            
            if (this.frightenedBySoda){
                return;
            }
            this.app.image(this.frightenedSprite,this.x-4,this.y-5);
            return;
        }
        this.app.image(this.sprite,this.x-4,this.y-5);
    }

}