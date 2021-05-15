package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * Map object which stores all cells in the game
 */
public class Map{
    /**
     * A 2-D arraylist which contains each cell associated with the current app instance
     */
    public ArrayList<ArrayList<Cell>> grid;
    /**
     * The app object associated with this map
     */
    public App app;
    /**
     * A list of integers which contain co-ordinates of the ghost's starting position and the type of ghost
     * the the first elemetn of each arraylist is the type of ghost. 1,2,3,4 correspond to a,c,i,w
     * the second elemetn is the x ordinate and the second is the y ordinate
     */
    public ArrayList<ArrayList<Integer>> ghostPoints;
    /**
     * A list of integers describing the player starting postiosn
     * the first element is the x ordinate and the second is the y ordinate
     */
    public ArrayList<Integer> playerPoint;
    /**
     * The number of fruits on the map when initially loaded, waka must interact with this many fruit objects to win the game
     */
    public int fruitCount=0;
    public int lives;

    public Map(App app,String filename){
        this.app=app;
        this.lives=app.config.lives;
        this.ghostPoints=new ArrayList<ArrayList<Integer>>();
        this.playerPoint=new ArrayList<Integer>();
        this.grid=parseMap(filename);
    }

    /**
     * A function which parses a text file which contains characters corresponding to cells
     * @param filename, name of the file containg map structure
     * @return a 2D array of cells
     */
    public ArrayList<ArrayList<Cell>> parseMap(String filename){
        ArrayList<ArrayList<Cell>> parsedGrid=new ArrayList<ArrayList<Cell>>();
        try{
            File mapFile=new File(filename);
            Scanner scan=new Scanner(mapFile);
            while (scan.hasNext()){
                ArrayList<Cell> tempRow=new ArrayList<Cell>();
                String[] line=scan.nextLine().split("");
                int col=0;
                for (String x:line){
                    Cell cell=null;
                    if(x.equals("0")){
                        cell=new Empty();
                    }
                    if(x.equals("1")){
                        cell=new Wall(this.app,"1");
                    }
                    if(x.equals("2")){
                        cell=new Wall(this.app,"2");
                    }
                    if(x.equals("3")){
                        cell=new Wall(this.app,"3");
                    }
                    if(x.equals("4")){
                        cell=new Wall(this.app,"4");
                    }
                    if(x.equals("5")){
                        cell=new Wall(this.app,"5");
                    }
                    if(x.equals("6")){
                        cell=new Wall(this.app,"6");
                    }
                    if(x.equals("7")){
                        cell=new Fruit(this.app,this);
                        this.fruitCount++;
                    }
                    if(x.equals("8")){
                        cell=new SuperFruit(this.app,this);
                        this.fruitCount++;
                    }
                    if(x.equals("s")){
                        cell=new Soda(this.app,this);
                        this.fruitCount++;
                    }
                    if(x.equals("p")){
                        cell=new Empty();
                        this.playerPoint.add(col);
                        this.playerPoint.add(parsedGrid.size());
                    }
                    if(x.equals("a")){
                        cell=new Empty();
                        ArrayList<Integer> temp=new ArrayList<Integer>();
                        temp.add(1);
                        temp.add(col*16);
                        temp.add(parsedGrid.size()*16);
                        this.ghostPoints.add(temp);
                    }
                    if(x.equals("c")){
                        cell=new Empty();
                        ArrayList<Integer> temp=new ArrayList<Integer>();
                        temp.add(2);
                        temp.add(col*16);
                        temp.add(parsedGrid.size()*16);
                        this.ghostPoints.add(temp);
                    }
                    if(x.equals("i")){
                        cell=new Empty();
                        ArrayList<Integer> temp=new ArrayList<Integer>();
                        temp.add(3);
                        temp.add(col*16);
                        temp.add(parsedGrid.size()*16);
                        this.ghostPoints.add(temp);
                    }
                    if(x.equals("w")){
                        cell=new Empty();
                        ArrayList<Integer> temp=new ArrayList<Integer>();
                        temp.add(4);
                        temp.add(col*16);
                        temp.add(parsedGrid.size()*16);
                        this.ghostPoints.add(temp);
                    }
                    if (cell!=null){
                        tempRow.add(cell);
                    }
                    col++;
                }
                parsedGrid.add(tempRow);
            }
            return parsedGrid;
        }
        catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        }
    }

    /**
     * Calls the draw method of each cell contained in the grid and updates the lives of waka shown in bottom left of the app window
     */
    public void draw(){
        for (int i=0;i<grid.size();i++){
            for(int j=0;j<grid.get(0).size();j++){
                grid.get(i).get(j).draw(j*16,i*16);     
            }
        } 
        for (int i=0;i<this.lives;i++){
            this.app.image(this.app.loadImage("src/main/resources/playerRight.png"),i*30,34*16); 
        }


    }

    /**
     * changes a cell in the grid to an empty cell, used when waka collects a fruit typ eobject
     * @param cell, the cell that should be removed
     */
    public void remove(Cell cell){
        for (ArrayList<Cell> x:this.grid){
            for(Cell y:x){
                if (y==cell){
                    this.grid.get(this.grid.indexOf(x)).set(x.indexOf(y),new Empty());
                }
            }
        }

    }

    /**
     * calls the load image fucntion of each cell in the grid
     */
    public void loadImages(){
        for (ArrayList<Cell> x:this.grid){
            for(Cell y:x){
                y.loadImage();
            }
        }
    }
}