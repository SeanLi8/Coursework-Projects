package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class GhostTargetingTest{
    @Test
    public void ambusherTest(){
        App testApp=new App();
        Ghost ghost=null;
        Waka waka=testApp.waka;
        for (Ghost x:testApp.ghosts){
            if (x.type==Ghost.GhostType.ambusher){
                ghost=x;
            }
        }
        //scatter target
        assertEquals(ghost.scatterTarget()[0],0);
        assertEquals(ghost.scatterTarget()[1],0);
        //chase target when waka is moving up
        waka.currentDirection=MovingObject.Direction.up;
        assertEquals(ghost.chaseTarget()[0],waka.x);
        assertEquals(ghost.chaseTarget()[1],waka.y-4*16);
        //chase target when waka is moving down
        waka.currentDirection=MovingObject.Direction.down;
        assertEquals(ghost.chaseTarget()[0],waka.x);
        assertEquals(ghost.chaseTarget()[1],waka.y+4*16);
        //chase target when waka is moving left
        waka.currentDirection=MovingObject.Direction.left;
        assertEquals(ghost.chaseTarget()[0],waka.x-4*16);
        assertEquals(ghost.chaseTarget()[1],waka.y);
        //chase target when waka is moving right
        waka.currentDirection=MovingObject.Direction.right;
        assertEquals(ghost.chaseTarget()[0],waka.x+4*16);
        assertEquals(ghost.chaseTarget()[1],waka.y);
    }

    @Test
    public void ChaserTest(){
        App testApp=new App();
        Ghost ghost=null;
        Waka waka=testApp.waka;
        for (Ghost x:testApp.ghosts){
            if (x.type==Ghost.GhostType.chaser){
                ghost=x;
            }
        }
        //scatter target
        assertEquals(ghost.scatterTarget()[0],28*16);
        assertEquals(ghost.scatterTarget()[1],0);
        //chase target
        waka.currentDirection=MovingObject.Direction.up;
        assertEquals(ghost.chaseTarget()[0],waka.x);
        assertEquals(ghost.chaseTarget()[1],waka.y);
        
    }
    @Test
    public void IgnorantTest(){
        App testApp=new App();
        Ghost ghost=null;
        Waka waka=testApp.waka;
        for (Ghost x:testApp.ghosts){
            if (x.type==Ghost.GhostType.ignorant){
                ghost=x;
            }
        }
        //scatter target
        assertEquals(ghost.scatterTarget()[0],0);
        assertEquals(ghost.scatterTarget()[1],36*16);
        //chase target when waka less than 8 grid space away
        ghost.x=waka.x;
        ghost.y=waka.y;
        assertEquals(ghost.chaseTarget()[0],0);
        assertEquals(ghost.chaseTarget()[1],36*16);
        //chase target when waka more than 8 grid space away
        ghost.x+=8*16;
        ghost.y+=8*16;
        assertEquals(ghost.chaseTarget()[0],waka.x);
        assertEquals(ghost.chaseTarget()[1],waka.y);
        
    }
    @Test
    public void WhimTest(){
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=null;
        Ghost chaser=null;
        Waka waka=testApp.waka;
        for (Ghost x:testApp.ghosts){
            if (x.type==Ghost.GhostType.whim){
                ghost=x;
            }
            if (x.type==Ghost.GhostType.chaser){
                chaser=x;
            }
        }
        //scatter target
        assertEquals(ghost.scatterTarget()[0],28*16);
        assertEquals(ghost.scatterTarget()[1],36*16);
        //chase target
        assertEquals(ghost.chaseTarget()[0],waka.x+1*16);
        assertEquals(ghost.chaseTarget()[1],waka.y+6*16);
        //if chaser has been eaten
        testApp.ghosts.remove(chaser);
        assertEquals(ghost.chaseTarget()[0],waka.x);
        assertEquals(ghost.chaseTarget()[1],waka.y);
        
    }
}