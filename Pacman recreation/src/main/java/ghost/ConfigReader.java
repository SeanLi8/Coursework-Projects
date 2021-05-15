package ghost;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
* Reads a json file and extracts configuration information for the app
*/
public class ConfigReader{
    /**
     * The name of the map file tht should be parsed
     */
    public String map;
    /**
     * The amount of lives waka should have
     */
    public int lives;
    /**
     * The speed at which waka and ghosts in the game move, this value can either be 1 or 2
     */
    public int speed;
    /**
     * The time intervals which ghosts switch between scatter and chase mode as a list of integers
     */
    public ArrayList<Integer> modeLengths;

    
    public ConfigReader(String filename){
        JSONParser parser = new JSONParser();
        Object object = null;
        try {
            File file = new File(filename);
            FileReader f = new FileReader(file);
            object = parser.parse(f);
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }
        catch (ParseException e) {
        }
        
        JSONObject jsonObject = (JSONObject) object;  
        this.map = (String) jsonObject.get("map");
        this.lives = ((Long) jsonObject.get("lives")).intValue();
        this.speed = ((Long) jsonObject.get("speed")).intValue();
        JSONArray jsonarray = (JSONArray) jsonObject.get("modeLengths");
        this.modeLengths = new ArrayList<Integer>();
        for (Object x:jsonarray) {
            this.modeLengths.add(((Long) x).intValue());
        }
    }
    
    
}