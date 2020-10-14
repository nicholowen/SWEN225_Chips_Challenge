package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

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
     * Saves the current state of the maze as a json file
     *
     * @param maze the maze to save
     * @throws IOException {@inheritDoc}
     */
    public static void saveGameState(Maze maze) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String filename = dateFormat.format(Calendar.getInstance().getTime()) + "-game-state.json";

        if (!savedState.toFile().exists() && !savedState.toFile().mkdirs()) {
            throw new IOException("Error creating directory, check that you have permission");
        }

        try (Writer writer = new FileWriter(Paths.get(savedState.toString(), filename).toString())) {
            gson.toJson(maze, writer);
        }
    }

    /**
     * Saves the current state of the maze as a string
     *
     * @param maze the game to save
     */
    public static String getGameState(Maze maze) {
        return gson.toJson(maze);
    }

    /**
     * Reads a saved game from a json file into a Maze object
     *
     * @return the saved maze object
     */
    public static Maze loadGameState() throws IOException {
        File recentSave = getRecentSave();

        if (recentSave == null) {
            return null;
        } else {
            try {
                JsonReader reader = new JsonReader(new FileReader(recentSave.getAbsoluteFile()));
                return gson.fromJson(reader, Maze.class);
            } catch (FileNotFoundException e){
                return null;
            }
        }
    }

    /**
     * Loads a the saved game state from a string into a Maze object
     *
     * @return the saved maze object
     */
    public static Maze loadGameState(String state) {
        return gson.fromJson(state, Maze.class);
    }

    /**
     * Gets the most recent save from the save game state directory.
     */
    private static File getRecentSave() throws IOException {
        File[] directoryList= savedState.toFile().listFiles();

        if (directoryList == null){
            throw new IOException("Directory '" + savedState + "' is empty.");
        } else {
            return Arrays.stream(directoryList).max(Comparator.comparing(File::getName)).orElse(null);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, LevelFileException {
        Maze maze = new Maze();
        String state = Persistence.getGameState(maze);
        System.out.println(state);

        Persistence.read(2);

        try {
            Persistence.saveGameState(maze);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
