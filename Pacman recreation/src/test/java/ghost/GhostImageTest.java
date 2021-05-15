package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import processing.core.PApplet;


public class GhostImageTest{

    @Test
    public void loadImageTest(){
        //Tests that loading the image initialises sprite attributes of ghost
        App testApp=new App("src/test/resources/testConfig.json");
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        for (Ghost x:testApp.ghosts){
            assertTrue(x.frightenedSprite==null);
            assertTrue(x.sprite==null);
        }
        for (Ghost x:testApp.ghosts){
            x.loadImage();
        }
        for (Ghost x:testApp.ghosts){
            assertTrue(x.frightenedSprite!=null);
            assertTrue(x.sprite!=null);
        }

    }
    @Test
    public void drawTest(){
        //Test the draw method of ghosts runs without error
        App testApp=new App("src/test/resources/testConfig.json");
        Ghost ghost=testApp.ghosts.get(0);
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        ghost.loadImage();
        ghost.mode=Ghost.GhostMode.frightened;
        ghost.draw();
        ghost.frightenedBySoda=true;
        ghost.draw();
    }

}
