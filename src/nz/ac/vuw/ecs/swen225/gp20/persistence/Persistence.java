package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class to handle reading of json level files and
 * reading and writing of game state
 *
 * @author Campbell Whitworth 300490070
 */
public class Persistence {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final Path resources = Paths.get("resources");
    public static final Path levels = Paths.get(resources.toString(), "levels");
    public static final Path recordings = Paths.get(resources.toString(), "recordings");
    public static final Path savedState = Paths.get(resources.toString(), "saved-state");

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
    public static Level read(int level) throws FileNotFoundException, JsonSyntaxException, LevelFileException {
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
    public static Level read(String json) throws JsonSyntaxException, LevelFileException {
        Level levelObj = gson.fromJson(json, Level.class);
        levelObj.validate();
        return levelObj;
    }

    /**
     * Helper method that checks if a file exists.
     */
    private static File checkFile(File file) throws FileNotFoundException {
        if (file.exists() && !file.isDirectory()) {
            return file;
        } else {
            System.out.println(file);
            throw new FileNotFoundException();
        }
    }

    /**
     * Gets a file object from the levels directory & checks that file exists.
     */
    private static File getLevelFile(int level) throws FileNotFoundException {
        return checkFile(Paths.get(levels.toString(), "level" + level + ".json").toFile());
    }

    /**
     * Gets a file object from the save directory & checks that file exists.
     */
    private File getSaveFile(String filename) throws FileNotFoundException {
        return checkFile(new File(filename));
    }

    /**
     * Saves the current state of the maze as a json file
     *
     * @param game the game to save
     * @throws IOException {@inheritDoc}
     */
    public static void saveGameState(Main game) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String filename = dateFormat.format(Calendar.getInstance().getTime()) + "-game-state.json";

        if (!savedState.toFile().exists() && !savedState.toFile().mkdirs()) {
            System.out.println("Error making directory");
            return;
        }

        try (Writer writer = new FileWriter(Paths.get(savedState.toString(), filename).toString())) {
            gson.toJson(game, writer);
        }
    }

    /**
     * Reads a saved game from a json file into a Maze object
     *
     * @return the saved maze object
     */
    public Maze loadGameState() {
        return null;
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.getMaze().loadMaze(1);
        try {
            Persistence.saveGameState(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
