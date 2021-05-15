package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import processing.core.PApplet;

//Tests in this class sometimes pass and sometimes fail reason is unknown
public class AppTest{

    @Test
    public void restartTest(){
        //checks if app restarts after 10 seconds of not running
        App testApp=new App("src/test/resources/testConfig.json");
        Map tempMap=testApp.map;
        Waka tempWaka=testApp.waka;
        ArrayList<Ghost> tempGhosts=testApp.ghosts;
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.running=false;
        testApp.tempFrame=0;
        testApp.setup();
        testApp.draw();
        testApp.frameCount=1000;
        testApp.draw();
        assertTrue(tempMap!=testApp.map);
        assertTrue(tempWaka!=testApp.waka);
        assertTrue(tempGhosts!=testApp.ghosts);
        assertTrue(testApp.running==true);
    }
    
    @Test
    public void debugTest(){
        //testing that debug mode runs without errors in frightened and non frightened mode
        App testApp=new App("src/test/resources/testConfig.json");
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.isDebugging=true;
        testApp.setup();
        testApp.draw();
        for (Ghost x:testApp.ghosts){
            x.mode=Ghost.GhostMode.frightened;
        }       
        testApp.draw();
    }
    
}
