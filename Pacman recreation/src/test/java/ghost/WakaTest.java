package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import processing.core.PApplet;



public class WakaTest{
    @Test
    public void testConstructor(){
        App testApp=new App("src/test/resources/testConfig.json");
        Waka waka=testApp.waka;
        assertTrue(waka.x==13*16);
        assertTrue(waka.x==waka.xStart);
        assertTrue(waka.y==20*16);
        assertTrue(waka.y==waka.yStart);
        assertTrue(waka.xVel!=0 || waka.yVel!=0);
    }

    @Test
    public void testMove(){
        //Test waka moove function
        App testApp=new App("src/test/resources/testConfig.json");
        Waka waka=testApp.waka;
        waka.xVel=0;
        waka.yVel=0;
        waka.move(KeyEvent.VK_UP);
        assertTrue(waka.yVel==-testApp.config.speed && waka.xVel==0);
        waka.move(KeyEvent.VK_DOWN);
        assertTrue(waka.yVel==testApp.config.speed && waka.xVel==0);
        waka.yVel=0;
        waka.move(KeyEvent.VK_RIGHT);
        assertTrue(waka.yVel==0 && waka.xVel==testApp.config.speed);
        waka.move(KeyEvent.VK_LEFT);
        assertTrue(waka.yVel==0 && waka.xVel==-testApp.config.speed);
        waka.move(KeyEvent.VK_DOWN);
        assertTrue(waka.yVel==testApp.config.speed && waka.xVel==-testApp.config.speed);
        waka.move(KeyEvent.VK_UP);
        assertTrue(waka.yVel==-testApp.config.speed && waka.xVel==-testApp.config.speed);
    }

    @Test
    public void testTickMovement(){
        App testApp=new App("src/test/resources/testConfig.json");
        Waka waka=testApp.waka;
        int tempX=waka.x;
        int tempY=waka.y;
        waka.xVel=0;
        waka.yVel=0;
        waka.tickMovement();
        //walking into a wall
        assertTrue(waka.x==tempX && waka.y==tempY);
        waka.yVel=1;
        waka.tickMovement();
        assertTrue(waka.x==tempX && waka.y==tempY);
        //moving a single pixel right
        waka.yVel=0;
        waka.xVel=1;
        waka.tickMovement();
        assertTrue(waka.x!=tempX && waka.y==tempY);
        assertTrue(waka.x==tempX+1 && waka.y==tempY);
        assertTrue(waka.currentDirection==MovingObject.Direction.right);
        //moving in straight line right
        waka.x=tempX;
        for (int i=1;i<=10;i++){
            waka.tickMovement();
        }
        assertTrue(waka.x==tempX+10 && waka.y==tempY);
        assertTrue(waka.currentDirection==MovingObject.Direction.right);
        //making a turn moving right to moving down
        waka.x=tempX;
        waka.yVel=1;
        waka.xVel=1;
        for (int j=1;j<=500;j++){
            waka.tickMovement();
            assertTrue(waka.currentDirection==MovingObject.Direction.right || waka.currentDirection==MovingObject.Direction.down);
        }
        assertTrue(waka.x>tempX && waka.y>tempY);
        assertTrue(waka.xVel==0);
        //moving left 
        waka.x=tempX;
        waka.y=tempY;
        waka.yVel=0;
        waka.xVel=-1;
        for (int i=1;i<=10;i++){
            waka.tickMovement();
        }
        assertTrue(waka.x==tempX-10 && waka.y==tempY);
        assertTrue(waka.currentDirection==MovingObject.Direction.left);
        //making a turn moving left to moving up
        waka.x=tempX;
        waka.yVel=-1;
        waka.xVel=-1;
        for (int j=1;j<=500;j++){
            waka.tickMovement();
            assertTrue(waka.currentDirection==MovingObject.Direction.left || waka.currentDirection==MovingObject.Direction.up);

        }
        assertTrue(waka.x<tempX && waka.y<tempY);
        assertTrue(waka.xVel==0);
        //waka from a stationary position cannot make a move that will move instantly into a wall
        //waka is stationary is upper left corner, tries to go up, up move is erased
        waka.x=1*16;
        waka.y=4*16;
        waka.yVel=-1;
        waka.xVel=0;
        waka.tickMovement();
        assertTrue(waka.yVel==0);
        assertTrue(waka.xVel==0);
        //waka is stationary in upp left corner, tries to go left, left move is erased
        waka.x=1*16;
        waka.y=4*16;
        waka.yVel=0;
        waka.xVel=-1;
        waka.tickMovement();
        assertTrue(waka.yVel==0);
        assertTrue(waka.xVel==0);
    }


    @Test
    public void testGetFruit(){
        App testApp=new App("src/test/resources/testConfig.json");
        Waka waka=testApp.waka;
        int tempLives=testApp.map.lives;
        int tempPoints=waka.points;
        waka.xVel=0;
        waka.yVel=0;
        waka.getFruit();
        assertTrue(waka.points==tempPoints);
        //getting normal fruit
        waka.x=14*16;
        assertTrue(testApp.map.grid.get(20).get(14) instanceof Fruit);
        waka.getFruit();
        assertTrue(testApp.map.grid.get(20).get(14) instanceof Empty);
        assertTrue(waka.points==tempPoints+1);
        //getting soda
        waka.x=2*16;
        waka.y=23*16;
        for (Ghost x:testApp.ghosts){
            assertFalse(x.frightenedBySoda);

        }
        assertTrue(testApp.map.grid.get(23).get(2) instanceof Soda);
        waka.getFruit();
        assertTrue(testApp.map.grid.get(23).get(2) instanceof Empty);
        assertTrue(waka.points==tempPoints+2);
        for (Ghost x:testApp.ghosts){
            assertTrue(x.mode==Ghost.GhostMode.frightened);
            assertTrue(x.frightenedBySoda);

        } 
        //getting superfruit
        for (Ghost x:testApp.ghosts){
            x.mode=Ghost.GhostMode.scatter;
            x.frightenedBySoda=false;
            assertTrue(x.mode==Ghost.GhostMode.scatter);
        }
        waka.x=1*16;
        waka.y=5*16;
        assertTrue(testApp.map.grid.get(5).get(1) instanceof SuperFruit);
        waka.getFruit();
        assertTrue(testApp.map.grid.get(5).get(1) instanceof Empty);
        assertTrue(waka.points==tempPoints+3);
        for (Ghost x:testApp.ghosts){
            assertTrue(x.mode==Ghost.GhostMode.frightened);
            assertFalse(x.frightenedBySoda);
        }
        //get fruit when wak horizontally betwen cells
        waka.x=10*16+1;
        waka.y=20*16;
        assertTrue(testApp.map.grid.get(20).get(10) instanceof Fruit);
        assertTrue(testApp.map.grid.get(20).get(11) instanceof Fruit);
        waka.getFruit();
        assertTrue(testApp.map.grid.get(20).get(10) instanceof Empty);
        assertTrue(testApp.map.grid.get(20).get(11) instanceof Empty);
        assertTrue(waka.points==tempPoints+5);
        //get fruit when waka vertically between cells
        waka.x=1*16;
        waka.y=6*16+1;
        assertTrue(testApp.map.grid.get(6).get(1) instanceof Fruit);
        assertTrue(testApp.map.grid.get(7).get(1) instanceof Fruit);
        waka.getFruit();
        assertTrue(testApp.map.grid.get(6).get(1) instanceof Empty);
        assertTrue(testApp.map.grid.get(7).get(1) instanceof Empty);
        assertTrue(waka.points==tempPoints+7);


    }

    @Test
    public void testCheckGhost(){
        App testApp=new App("src/test/resources/testConfig.json");
        Waka waka=testApp.waka;
        int tempLives=testApp.map.lives;
        int tempX=waka.x;
        int tempY=waka.y;
        waka.xVel=0;
        waka.yVel=0;
        //did not touch ghost
        waka.checkGhost();
        assertTrue(testApp.map.lives==tempLives);
        //touching ghost
        waka.x=testApp.ghosts.get(0).x;
        waka.y=testApp.ghosts.get(0).y;
        waka.checkGhost();
        assertTrue(testApp.map.lives==tempLives-1);
        assertTrue(tempX==waka.x);
        assertTrue(tempY==waka.y);
        assertTrue(waka.xVel!=0 || waka.yVel!=0);
        //touching ghost but checking if ghost positions reset
        int tempGhostX=testApp.ghosts.get(0).x;
        int tempGhostY=testApp.ghosts.get(0).y;
        testApp.ghosts.get(0).x=tempX;
        testApp.ghosts.get(0).y=tempY;
        waka.checkGhost();
        assertTrue(testApp.map.lives==tempLives-2);
        assertTrue(tempGhostX==testApp.ghosts.get(0).x);
        assertTrue(tempGhostY==testApp.ghosts.get(0).y);
        //touching a frightened ghost, lives dont change ghost is removed
        testApp.ghosts.get(0).x=tempX;
        testApp.ghosts.get(0).y=tempY;
        testApp.ghosts.get(0).mode=Ghost.GhostMode.frightened;
        int tempGhostAmount=testApp.ghosts.size();
        waka.checkGhost();
        assertTrue(testApp.map.lives==tempLives-2);
        assertTrue(tempGhostAmount==testApp.ghosts.size()+1);

    }

    @Test
    public void loadImageTest(){
        App testApp=new App();
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        Waka waka=testApp.waka;
        waka.loadImage();
        assertTrue(waka.sprite!=null);
        assertTrue(waka.upSprite!=null);
        assertTrue(waka.downSprite!=null);
        assertTrue(waka.rightSprite!=null);
        assertTrue(waka.leftSprite!=null); 
    }

    @Test
    public void chooseSpriteTest(){
        App testApp=new App();
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        Waka waka=testApp.waka;
        waka.loadImage();
        //choose closed sprite
        testApp.frameCount=0;
        assertEquals(waka.chooseSprite(),waka.sprite);
        //choose directional sprite
        testApp.frameCount=10;
        //choose up sprite
        waka.currentDirection=MovingObject.Direction.up;
        assertEquals(waka.chooseSprite(),waka.upSprite);
        //choose down sprite
        waka.currentDirection=MovingObject.Direction.down;
        assertEquals(waka.chooseSprite(),waka.downSprite);
        //choose right sprite
        waka.currentDirection=MovingObject.Direction.right;
        assertEquals(waka.chooseSprite(),waka.rightSprite);
        //choose left sprite
        waka.currentDirection=MovingObject.Direction.left;
        assertEquals(waka.chooseSprite(),waka.leftSprite); 
    }

    @Test
    public void tickTest(){
        //test to see if any errors come from calling tick, function mostly just combines above tested functions
        App testApp=new App();
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.font=testApp.createFont("PressStart2P-Regular.ttf",40);
        Waka waka=testApp.waka;
        waka.loadImage();
        waka.tick();
        //winning state
        testApp=new App();
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.font=testApp.createFont("PressStart2P-Regular.ttf",40);
        waka=testApp.waka;
        waka.points=testApp.map.fruitCount;
        waka.tick();
        //losing state
        testApp=new App();
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.font=testApp.createFont("PressStart2P-Regular.ttf",40);
        waka=testApp.waka;
        waka.loadImage();
        testApp.map.lives=0;
        waka.tick();
    }


}
