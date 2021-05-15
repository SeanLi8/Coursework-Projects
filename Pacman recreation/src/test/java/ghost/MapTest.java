package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import processing.core.PApplet;


public class MapTest{
    
    @Test
    public void testParse(){
        //Checks that map is parsed correctly, that the right cell objects are in the right place
        App testApp=new App("src/test/resources/testConfig.json");
        assertTrue(testApp.map.grid.size()==36);
        assertTrue(testApp.map.grid.get(0).size()==28);
        assertTrue(testApp.map.ghostPoints.size()==4);
        assertTrue(testApp.map.ghostPoints.get(0).size()==3);
        assertTrue(testApp.map.playerPoint.size()==2);
        assertTrue(testApp.map.grid.get(0).get(0) instanceof Empty);
        assertTrue(testApp.map.grid.get(3).get(0) instanceof Wall);
        assertTrue(testApp.map.grid.get(4).get(0) instanceof Wall);
        assertTrue(testApp.map.grid.get(3).get(27) instanceof Wall);
        assertTrue(testApp.map.grid.get(33).get(0) instanceof Wall);
        assertTrue(testApp.map.grid.get(33).get(27) instanceof Wall);
        assertTrue(testApp.map.grid.get(3).get(1) instanceof Wall);
        assertTrue(testApp.map.grid.get(4).get(1) instanceof Fruit);
        assertTrue(testApp.map.grid.get(14).get(10) instanceof Empty);
        assertTrue(testApp.map.grid.get(14).get(12) instanceof Empty);
        assertTrue(testApp.map.grid.get(14).get(14) instanceof Empty);
        assertTrue(testApp.map.grid.get(14).get(16) instanceof Empty);
        assertTrue(testApp.map.grid.get(20).get(13) instanceof Empty);
        assertTrue(testApp.map.grid.get(5).get(1) instanceof SuperFruit);
        assertTrue(testApp.map.grid.get(23).get(2) instanceof Soda);

    }
 
    @Test 
    public void testRemove(){
        //Test that a cell is properly removed
        App testApp=new App("src/test/resources/testConfig.json");
        assertTrue(testApp.map.grid.get(3).get(0) instanceof Wall);
        testApp.map.remove(testApp.map.grid.get(3).get(0));
        assertTrue(testApp.map.grid.get(3).get(0) instanceof Empty);


    }

    @Test
    public void testLoadImages(){
        //Test that images are initialised by loadImages()
        App testApp=new App("src/test/resources/testConfig.json");
        for (ArrayList<Cell> x:testApp.map.grid){
            for (Cell y:x){
                assertTrue(y.sprite==null);
            }
        }
        PApplet.runSketch(new String[] {"ghost.app"},testApp);
        testApp.map.loadImages();
        for (ArrayList<Cell> x:testApp.map.grid){
            for (Cell y:x){
                assertTrue(y.sprite!=null || y instanceof Empty);
            }
        }

    }

    
}