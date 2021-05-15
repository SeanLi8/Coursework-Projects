package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class GhostMovementTest{

    @Test
    public void getPossubleTurnsTest(){
        App testApp=new App();
        Ghost ghost=testApp.ghosts.get(0);
        //going up in vertical pathway non intersection
        ghost.x=1*16;
        ghost.y=5*16;
        ghost.currentDirection=MovingObject.Direction.up;
        ArrayList<MovingObject.Direction> tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==1);
        assertTrue(tempList.contains(MovingObject.Direction.up));
        //going down in vertical pathway non intersection
        ghost.x=1*16;
        ghost.y=5*16;
        ghost.currentDirection=MovingObject.Direction.down;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==1);
        assertTrue(tempList.contains(MovingObject.Direction.down));
        //going right in a horizontal pathway non intersection
        ghost.x=2*16;
        ghost.y=4*16;
        ghost.currentDirection=MovingObject.Direction.right;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==1);
        assertTrue(tempList.contains(MovingObject.Direction.right));
        //going left in a horizontal pathway non intersection
        ghost.x=2*16;
        ghost.y=4*16;
        ghost.currentDirection=MovingObject.Direction.left;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==1);
        assertTrue(tempList.contains(MovingObject.Direction.left));
        //moving into a corner
        ghost.x=1*16;
        ghost.y=4*16;
        ghost.currentDirection=MovingObject.Direction.up;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==1);
        assertTrue(tempList.contains(MovingObject.Direction.right));
        //moving into a cross section
        ghost.x=6*16;
        ghost.y=8*16;
        ghost.currentDirection=MovingObject.Direction.up;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==3);
        assertTrue(tempList.contains(MovingObject.Direction.right));
        assertTrue(tempList.contains(MovingObject.Direction.up));
        assertTrue(tempList.contains(MovingObject.Direction.left));
        //moving int a t section
        ghost.x=6*16;
        ghost.y=4*16;
        ghost.currentDirection=MovingObject.Direction.right;
        tempList=ghost.getPossibleTurns();
        assertTrue(tempList.size()==2);
        assertTrue(tempList.contains(MovingObject.Direction.right));
        assertTrue(tempList.contains(MovingObject.Direction.down));        
    }

    @Test
    public void decideTurnTest(){
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=testApp.ghosts.get(0);
        //turn decision at cross section
        ghost.x=6*16;
        ghost.y=23*16;
        //target towards left
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16-20,23*16};
        MovingObject.Direction decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.left);
        //target towards right
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16+20,23*16};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.right);
        //target upwards
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16,23*16-20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.up);
        //target towards downwards
        ghost.currentDirection=MovingObject.Direction.right;
        ghost.target=new int[] {6*16,23*16+20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.down);
        //prioritizes a horizontal direction
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16+50,23*16-20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.right);
        //prioritizes a vertical direction
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16+20,23*16-50};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.up);
        //turn decisions at a corner
        ghost.x=1*16;
        ghost.y=4*16;
        //only horizontal preference is possible
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {1*16+20,4*16-50};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.right);
        //only vertical preference is possible
        ghost.currentDirection=MovingObject.Direction.down;
        ghost.target=new int[] {1*16-10,4*16+20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.down);
        //none of preference is possible
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {1*16-20,4*16-20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.right);
        //both non preferences are available prioritize horizontal
        ghost.currentDirection=MovingObject.Direction.down;
        ghost.target=new int[] {1*16-20,4*16-30};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.right);
        //both non preferences are available prioritize vertical
        ghost.currentDirection=MovingObject.Direction.down;
        ghost.target=new int[] {1*16-30,4*16-20};
        decidedDirection=ghost.decideTurn(ghost.getPossibleTurns());
        assertTrue(decidedDirection==MovingObject.Direction.down);


    }

    

    @Test
    public void chooseModeTest(){
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=testApp.ghosts.get(0);
        assertTrue(ghost.mode==Ghost.GhostMode.scatter);
        //first call of method at start of game
        ghost.chooseMode();
        assertTrue(ghost.mode==Ghost.GhostMode.scatter);
        //timer increased passed first interval
        ghost.intervalTimer=10;
        ghost.chooseMode();
        assertTrue(ghost.mode==Ghost.GhostMode.chase);
        assertTrue(ghost.intervalTimer==0);
        assertTrue(ghost.intervalIndex==1);
        //timer increased passed second interval
        ghost.intervalTimer=25;
        ghost.chooseMode();
        assertTrue(ghost.mode==Ghost.GhostMode.scatter);
        assertTrue(ghost.intervalTimer==0);
        assertTrue(ghost.intervalIndex==2);
    }

    @Test
    public void tickMovementTest(){
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=testApp.ghosts.get(0);
        //going up in vertical pathway non intersection
        ghost.x=1*16;
        ghost.y=5*16;
        ghost.yVel=-1;
        ghost.xVel=0;
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.tickMovement();
        assertTrue(ghost.x==1*16);
        assertTrue(ghost.y==5*16-1);
        //going right in a horizontal pathway non intersection
        ghost.x=2*16;
        ghost.y=4*16;
        ghost.yVel=0;
        ghost.xVel=1;
        ghost.currentDirection=MovingObject.Direction.right;
        ghost.tickMovement();
        assertTrue(ghost.x==2*16+1);
        assertTrue(ghost.y==4*16);
        //ghost is frightened
        ghost.x=6*16;
        ghost.y=8*16;
        ghost.yVel=0;
        ghost.xVel=0;
        ghost.mode=Ghost.GhostMode.frightened;
        ghost.tickMovement();
        assertTrue(ghost.yVel!=0 || ghost.xVel!=0);
        ghost.mode=Ghost.GhostMode.scatter;
        //ghost decides to go up
        ghost.x=6*16;
        ghost.y=23*16;
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16,23*16-20};
        ghost.tickMovement();
        assertTrue(ghost.x==6*16);
        assertTrue(ghost.y==23*16-1);
        //ghost decides to go down
        ghost.x=6*16;
        ghost.y=23*16;
        ghost.currentDirection=MovingObject.Direction.down;
        ghost.target=new int[] {6*16,23*16+20};
        ghost.tickMovement();
        assertTrue(ghost.x==6*16);
        assertTrue(ghost.y==23*16+1);
        //ghost decides to go right
        ghost.x=6*16;
        ghost.y=23*16;
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16+20,23*16};
        ghost.tickMovement();
        assertTrue(ghost.x==6*16+1);
        assertTrue(ghost.y==23*16);
        //ghost decides to go left
        ghost.x=6*16;
        ghost.y=23*16;
        ghost.currentDirection=MovingObject.Direction.up;
        ghost.target=new int[] {6*16-20,23*16};
        ghost.tickMovement();
        assertTrue(ghost.x==6*16-1);
        assertTrue(ghost.y==23*16);
    }

    @Test 
    public void tickTest(){
        //scatter tick
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=testApp.ghosts.get(0);
        int tempX=ghost.x;
        int tempY=ghost.y;
        testApp.frameCount=0;
        ghost.intervalIndex=0;
        ghost.tick();
        assertTrue(ghost.mode==Ghost.GhostMode.scatter);
        assertTrue(tempX!=ghost.x || tempY!=ghost.y);
        //chase tick
        tempX=ghost.x;
        tempY=ghost.y;
        testApp.frameCount=0;
        ghost.intervalTimer=0;
        ghost.intervalIndex=1;
        ghost.mode=Ghost.GhostMode.chase;
        ghost.tick();
        assertTrue(ghost.mode==Ghost.GhostMode.chase);
        assertTrue(tempX!=ghost.x || tempY!=ghost.y);
        //scatter or chase tick intervalTimer increments at a second
        testApp.frameCount=60;
        ghost.intervalTimer=0;
        ghost.tick();
        assertTrue(ghost.intervalTimer==1);
        //frightened tick
        tempX=ghost.x;
        tempY=ghost.y;
        testApp.frameCount=700;
        testApp.tempFrame=20;
        ghost.mode=Ghost.GhostMode.frightened;
        ghost.tick();
        assertTrue(ghost.mode!=Ghost.GhostMode.frightened);

    }
}