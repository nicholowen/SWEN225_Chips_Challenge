package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.*;
import java.nio.Buffer;

import static nz.ac.vuw.ecs.swen225.gp20.maze.Maze.getBoardSize;


public class Persistence {
    private final Gson gson = new Gson();

    public Persistence() {

    }

    /**
     * Reads a level from a file into an object
     *
     * @param level the level number
     * @return a Level object
     * @throws FileNotFoundException if the level is not found
     * @throws JsonSyntaxException   {@inheritDoc}
     */
    public Level read(int level) throws FileNotFoundException, JsonSyntaxException, LevelFileException {
        File file = getLevelFile(level);
        JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));
        Level levelObj = gson.fromJson(reader, Level.class);
        levelObj.validate();
        return levelObj;
    }

    /**
     * Alternative read method used for testing purposes.
     *
     * @param json well formed JSON string
     * @return Level object
     * @throws JsonSyntaxException {@inheritDoc}
     */
    public Level read(String json) throws JsonSyntaxException, LevelFileException {
        Level levelObj = gson.fromJson(json, Level.class);
        levelObj.validate();
        return levelObj;
    }

    /**
     * Helper method that checks if a file exists.
     */
    private File checkFile(File file) throws FileNotFoundException {
        if (file.exists() && !file.isDirectory()) {
            return file;
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Gets a file object from the levels directory & checks that file exists.
     */
    private File getLevelFile(int level) throws FileNotFoundException {
        return checkFile(new File("resources/levels/level" + level + ".json"));
    }

    /**
     * Gets a file object from the save directory & checks that file exists.
     */
    private File getSaveFile(String filename) throws FileNotFoundException {
        return checkFile(new File(filename));
    }

    /**
     * Saves the recording to a json file
     *
     * @param record the recording to save
     */
    public static void saveRecording(RecordAndPlay record) {

    }

    /**
     * Reads a recorded game from a json file
     */
    public void readRecording() {

    }

    public static void main(String[] args) {
        Persistence persist = new Persistence();
        try {
            Level level = persist.read(1);
        } catch (FileNotFoundException | JsonSyntaxException | LevelFileException e) {
            System.out.println("here1");
            System.out.println(e.getClass());
            e.printStackTrace();
        }

        String jsonString =
                "{" +
                        " \"description\": \"Level 1\"," +
                        "  \"width\": 15," +
                        "  \"height\": 14," +
                        "  \"startX\": 7," +
                        "  \"startY\": 6," +
                        "  \"grid\": [" +
                        "      {\"x\": 0, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 1, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 2, \"y\": 0, \"name\": \"wall\"}" +
                        "  ]" +
                        "}";

        try {
            Level level = persist.read(jsonString);
        } catch (JsonSyntaxException | LevelFileException e) {
            System.out.println("here2");
            System.out.println(e.getClass());
            e.printStackTrace();
        }
    }

    public static String getGameState(Main game) {
        String jsonGame;

        // Json dump board
        Json.createObjectBuilder();
        JsonObjectBuilder builder;

        // Dump game info
        builder = Json.createObjectBuilder()
                .add("timeLeft", game.getTimeRemaining());

        // Compose game section
        try (Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(builder.build());
            jsonGame = writer.toString();
        } catch (IOException e) {
            throw new Error("Failed to parse game");
        }
        return jsonGame;
    }
}
