package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Persistence {
    private final Gson gson = new Gson();

    public Persistence() {

    }

    /**
     * Reads a level from a file into an object
     *
     * @param level the level number
     * @return a Maze object?
     * @throws FileNotFoundException if the level is not found
     * @throws JsonSyntaxException   {@inheritDoc}
     */
    public Level read(int level) throws FileNotFoundException, JsonSyntaxException {
        File file = getFile(level);
        JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));

        return gson.fromJson(reader, Level.class);
    }

    /**
     * Alternative read method used for testing purposes.
     *
     * @param json well formed JSON string
     * @return ?
     * @throws JsonSyntaxException {@inheritDoc}
     */
    public Level read(String json) throws JsonSyntaxException {
        return gson.fromJson(json, Level.class);
    }

    /**
     * Gets a file object from the levels directory & checks that file exists.
     */
    private File getFile(int level) throws FileNotFoundException {
        File file = new File("levels/level" + level + ".json");
        if (file.exists() && !file.isDirectory()) {
            return file;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static void main(String[] args) {
        Persistence persist = new Persistence();
        try {
            Level level = persist.read(1);
        } catch (FileNotFoundException | JsonSyntaxException e) {
            System.out.println("here1");
            System.out.println(e.getClass());
        }

        String jsonString =
                "{" +
                        " \"description\": \"Level 1\"," +
                        "  \"width\": 15," +
                        "  \"height\": 14," +
                        "  \"characters\": [" +
                        "      {\"x\": 7, \"y\": 6, \"name\": \"chap\"}" +
                        "  ]," +
                        "  \"grid\": [" +
                        "      {\"x\": 0, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 1, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 2, \"y\": 0, \"name\": \"wall\"}" +
                        "  ]" +
                        "}";

        try {
            Level level = persist.read(jsonString);
        } catch (JsonSyntaxException e) {
            System.out.println("here2");
            System.out.println(e.getClass());
        }
    }
}
