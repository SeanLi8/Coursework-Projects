package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.awt.event.KeyEvent;
import java.lang.Math;

/**
 * The user controlled object in the game
 */
public class Waka extends MovingObject{
    
    /**
     * A variable to keep track of how many fruits waka has eaten
     */
    public int points=0;
    /**
     * Th upwards directional sprite of waka
     */
    public PImage upSprite=null;
    /**
     * The downwards directional sprite of waka
     */
    public PImage downSprite=null;
    /**
     * The rightwards directional sprite of waka
     */
    public PImage rightSprite=null;
    /**
     * the leftwards directional sprite of waka
     */
    public PImage leftSprite=null;


    public Waka(App app,Map map){
        this.app=app;
        this.map=map;
        this.x=map.playerPoint.get(0)*16;
        this.xStart=this.x;
        this.y=map.playerPoint.get(1)*16;
        this.yStart=this.y;
        this.xVel=0;
        this.yVel=0;
        moveRandom();   
        
    }

    public void loadImage(){
        this.sprite=this.app.loadImage("src/main/resources/playerClosed.png");
        this.upSprite=this.app.loadImage("src/main/resources/playerUp.png");
        this.downSprite=this.app.loadImage("src/main/resources/playerDown.png");
        this.leftSprite=this.app.loadImage("src/main/resources/playerLeft.png");
        this.rightSprite=this.app.loadImage("src/main/resources/playerRight.png");


    }
    

    public void tick(){
        chooseSprite();
        tickMovement();
        getFruit();
        checkGhost();
        if (this.points>=this.map.fruitCount || this.map.lives==0){
            this.app.tempFrame=this.app.frameCount;
            this.app.textAlign(this.app.CENTER,this.app.CENTER);
            this.app.textFont(this.app.font,40);
            this.app.background(0, 0, 0);
            if (this.points>=this.map.fruitCount){
                this.app.text("YOU WIN",14*16,18*16);
            }
            else{
                this.app.text("GAME OVER",14*16,18*16);
            }   
            this.app.running=false;
     
        }
    }

    public void tickMovement(){
        //from stationary position waka cannot make move instantly moving into a wall
        if (checkAtIntersection()){
            if (this.yVel!=0 && this.xVel==0 && !checkPassable("Vertical",this.yVel)){
                this.yVel=0;
                return;
            }
            if (this.xVel!=0 && this.yVel==0 && !checkPassable("Horizontal",this.xVel)){
                this.xVel=0;
                return;
            }
            
        }
        if (this.yVel!=0 && checkPassable("Vertical",this.yVel)){
            this.y+=yVel;
            if (yVel>0){
                this.currentDirection=Direction.down;
            }
            else{
                this.currentDirection=Direction.up;
            }
            if (checkAtIntersection()){
                if(!checkPassable("Vertical",this.yVel) || (this.xVel!=0 && checkPassable("Horizontal",this.xVel))){
                    this.yVel=0;
                    return;
                }  
            }

        }
        if (this.xVel!=0 && checkPassable("Horizontal",this.xVel)){
            this.x+=xVel;
            if (xVel>0){
                this.currentDirection=Direction.right;
            }
            else{
                this.currentDirection=Direction.left;
            }
            if (checkAtIntersection()){
                if(!checkPassable("Horizontal",this.xVel) || (this.yVel!=0 && checkPassable("Vertical",this.yVel))){
                    this.xVel=0;
                    return;
                }
            }
        } 
        
    
    }

    /**
     * updates velocites of waka
     * @param keyCode, keycode of a directional key on the keyboard
     */
    public void move(int keyCode){
        if (keyCode==KeyEvent.VK_UP){
            this.yVel=-this.app.config.speed;  
        }
        if (keyCode==KeyEvent.VK_DOWN){
            this.yVel=this.app.config.speed;
        }
        if (keyCode==KeyEvent.VK_RIGHT){
            this.xVel=this.app.config.speed;
        }
        if (keyCode==KeyEvent.VK_LEFT){
            this.xVel=-this.app.config.speed;
        } 
    }


  
    public void draw(){
        this.app.image(chooseSprite(),this.x-4,this.y-5);
    }

    /**
     * Checks any grid space near waka and interacts with it
     */
    public void getFruit(){
        //collides with cell in the same grid space as waka
        this.map.grid.get(Math.floorDiv(this.y,16)).get(Math.floorDiv(this.x,16)).collide(this);
        //colldes with second cell if waka is horizontally between 2 cells
        if(this.x%16!=0){
            this.map.grid.get(Math.floorDiv(this.y,16)).get(Math.floorDiv(this.x,16)+1).collide(this);   
        }
        //collides with second cell if waka is horizontally between 2 cells
        else if(this.y%16!=0){
            this.map.grid.get(Math.floorDiv(this.y,16)+1).get(Math.floorDiv(this.x,16)).collide(this);   
        }
    }

    /**
     * Checks grid spaces near waka for ghosts and collides with them if they are overlapping
     */
    public void checkGhost(){
        for (Ghost ghost:this.app.ghosts){
            if ((ghost.x<=this.x && this.x<=ghost.x+16) || (ghost.x<=this.x+16 && this.x+16<=ghost.x+16)){
                if((ghost.y<=this.y && this.y<=ghost.y+16) || (ghost.y<=this.y+16 && this.y+16<=ghost.y+16)){
                    if (ghost.mode==Ghost.GhostMode.frightened){
                        this.app.ghosts.remove(ghost);
                        return;
                    }
                    this.map.lives-=1;
                    resetPosition();
                    moveRandom();
                    for (Ghost x:this.app.ghosts){
                        x.resetPosition();
                        x.moveRandom();
                    }
                    return;
                }
  
            }
        }
    }

    /**
     * Return appropriate directional sprite of waka based on the direction it is moving
     * waks should switch between closed sprite and a directional sprite every 8 frames
     * @return the sprite that is chosen
     */
    public PImage chooseSprite(){
        if (this.app.frameCount%16<8){
            return this.sprite;
        }
        else{
            if (this.currentDirection==Direction.up){
                return this.upSprite;
            }
            if (this.currentDirection==Direction.down){
                return this.downSprite;
            }
            if (this.currentDirection==Direction.left){
                return this.leftSprite;
            }
            else{
                return this.rightSprite;
            }
        }
    }

}

