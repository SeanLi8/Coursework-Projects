package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {
    public ConfigReader testReader=new ConfigReader("src/test/resources/testConfig.json");

    @Test 
    public void simpleTest() {
        //testing that file is read correctly
        assertFalse(testReader==null);
        assertEquals("src/test/resources/testMap.txt",testReader.map);
        assertEquals(3,testReader.lives);
        assertEquals(1,testReader.speed);
        assertTrue(testReader.modeLengths.size()==8);
        assertTrue(testReader.modeLengths.get(0)==7);
        assertTrue(testReader.modeLengths.get(1)==20);
        assertTrue(testReader.modeLengths.get(2)==7);
        assertTrue(testReader.modeLengths.get(3)==20);
        assertTrue(testReader.modeLengths.get(4)==5);
        assertTrue(testReader.modeLengths.get(5)==20);
        assertTrue(testReader.modeLengths.get(6)==5);
        assertTrue(testReader.modeLengths.get(7)==1000);
    }
    
}