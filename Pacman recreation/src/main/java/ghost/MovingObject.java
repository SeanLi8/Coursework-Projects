package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.awt.event.KeyEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class which contains logic for moving objects in the game
 * handles collision logic with walls
 */
public abstract class MovingObject{
    /**
     * The current horizontal position of object
     */
    public int x;
    /**
     * current vertical position of object
     */
    public int y;
    /**
     * starting horizontal position for object
     */
    public int xStart;
    /**
     * starting vertical position for object
     */
    public int yStart;
    /**
     * horizontal velocity for th eobject
     * negativ numbers  represent upwards movemet whil positive numbers represent downwards movement
     */
    public int xVel;
    /**
     * vertical velocity for the object
     * negative numbers represent leftwards movemnt while posive number represent rightwards movement
     */
    public int yVel;
    /**
     * The image associated the the object
     */
    public PImage sprite;
    /**
     * The app instance associated with the object
     */
    public App app;
    /**
     * The map instance associated with the object
     */
    public Map map;
    /**
     * The current direction the object is moving
     */
    public Direction currentDirection;

    
    public enum Direction{
        up,
        down,
        right,
        left;

        /**
         * returns the opposite of a direction
         * @return the direction
         */
        public Direction getReverse(){
            if (this==up){
                return down;
            }
            else if (this==down){
                return up;
            }
            else if (this==right){
                return left;
            }
            else{
                return right;
            }
        }
    }

    /**
     * Updates the logic for the object
     */
    public abstract void tick();
    /**
     * displas the object onto app window
     */
    public abstract void draw();
    
    /**
     * updates the movement logic for an object
     */
    public abstract void tickMovement();



    /**
     * checks if a movement in a given direction wil result in waka touching a non passable cell
     * @param mode, either "Vertical" or "Horizontal" it tells function which axis to test
     * @param movement, can be positive or negative, tells the fucntion what direction in a certain axis to test
     * @return possable, true if it is passable, false otherwise
     */
    
    public boolean checkPassable(String mode,int movement){
        int tempX=this.x;
        int tempY=this.y;
        if (mode.equals("Vertical")){
            tempY+=movement;
        }
        if (mode.equals("Horizontal")){
            tempX+=movement;
        }
        //checks if grid position the of the top left of the object is passable
        if (!this.map.grid.get(Math.floorDiv(tempY,16)).get(Math.floorDiv(tempX,16)).passable()){
            return false;   
        }
        //checks if grid position the of the bottom right of the object is passable
        if(tempX%16!=0 && tempY%16!=0 && !this.map.grid.get(Math.floorDiv(tempY,16)+1).get(Math.floorDiv(tempX,16)+1).passable()){
            return false;
        }   
        //checks if grid position the of the right of the object is passable            
        if(tempX%16!=0 && !this.map.grid.get(Math.floorDiv(tempY,16)).get(Math.floorDiv(tempX,16)+1).passable()){
            return false;
        }   
        //checks if grid position the of below the object is passable
        if(tempY%16!=0 && !this.map.grid.get(Math.floorDiv(tempY,16)+1).get(Math.floorDiv(tempX,16)).passable()){
            return false;
        }   
        return true;   
    }

    /**
     * Uses Random module to generate a random number and pick a direction out of possible directions the object can mvoe
     */
    public void moveRandom(){
        ArrayList<Direction> possible=new ArrayList<Direction>();
        Random randomObject=new Random();
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
        int chosenIndex=randomObject.nextInt(possible.size());
        Direction chosenDirection=possible.get(chosenIndex);

        if (chosenDirection==Direction.up){
            this.yVel=-this.app.config.speed;
            this.xVel=0;
            this.currentDirection=Direction.up;
        }        
        else if (chosenDirection==Direction.down){
            this.yVel=this.app.config.speed;
            this.xVel=0;
            this.currentDirection=Direction.down;
        }
        else if (chosenDirection==Direction.right){
            this.xVel=this.app.config.speed;
            this.yVel=0;
            this.currentDirection=Direction.right;
        }
        else{
            this.xVel=-this.app.config.speed;
            this.yVel=0;
            this.currentDirection=Direction.left;
        }
    }

    /**
     * sets current position of object to its original position and sets velocities to 0
     */
    public void resetPosition(){
        this.x=this.xStart;
        this.y=this.yStart;
        this.xVel=0;
        this.yVel=0;
    }

    /**
     * Checks if an object is at an intersection
     * an intersection is defined as a position where waka can make both vertical and horizontal movement
     * @return dFlse if not at intersection true if at itersection
     */
    public boolean checkAtIntersection(){
        boolean horizontalPassable=checkPassable("Horizontal",1) || checkPassable("Horizontal",-1); 
        boolean verticalPassable=checkPassable("Vertical",1) || checkPassable("Vertical",-1); 
        return horizontalPassable && verticalPassable;
    }

    


    

}