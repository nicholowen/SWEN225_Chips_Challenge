package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Persistence {
    public Persistence() {

    }

    /**
     * Reads a level file into an object
     *
     * @param level the level number
     * @throws FileNotFoundException if the level is not found
     */
    public void read(int level) throws FileNotFoundException {
        File file = getFile(level);
        JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));

        Gson gson = new Gson();
        Level levelObj = gson.fromJson(reader, Level.class);
        System.out.println(levelObj);

        for (Tile tile: levelObj.getGrid()){
            System.out.println(tile);
        }
    }

    /** Gets a file object from the levels directory */
    private File getFile(int level) throws FileNotFoundException {
        File file = new File("levels/level" + level + ".json");
        if(file.exists() && !file.isDirectory()) {
            return file;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static void main(String[] args) {
        Persistence persist = new Persistence();
        try {
            persist.read(1);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
