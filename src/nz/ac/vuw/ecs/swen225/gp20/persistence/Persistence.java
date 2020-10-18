package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Level;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    /**
     * Reads a level from a file into an object
     *
     * @param level the level number
     * @return a Level object
     * @throws IOException if the level is not found
     * @throws JsonSyntaxException   {@inheritDoc}
     */
    public static Level read(int level) throws IOException, JsonSyntaxException {
        File file = getLevelFile(level);

        Level levelObj = readJsonFromFile(file, Level.class);
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
    public static Level read(String json) throws JsonSyntaxException {
        Level levelObj = readJsonFromString(json, Level.class);
        levelObj.validate();

        return levelObj;
    }

    private static <T> T readJsonFromFile(File file, Class<T> type) throws IOException {
        InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        T typeObj = gson.fromJson(streamReader, type);
        streamReader.close();

        return typeObj;
    }

    private static <T> T readJsonFromString(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    /**
     * Helper method that checks if a file exists.
     */
    private static File checkFile(File file) throws FileNotFoundException {
        if (file.exists() && !file.isDirectory()) {
            return file;
        } else {
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

        OutputStream outputStream = new FileOutputStream(Paths.get(savedState.toString(), filename).toFile());
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        gson.toJson(maze, bufferedWriter);
        bufferedWriter.close();
    }

    /**
     * Returns the current state of the maze as a json string
     *
     * @param maze the game to save
     * @return json string of the maze
     */
    public static String getGameState(Maze maze) {
        return gson.toJson(maze);
    }

    /**
     * Reads the most recent saved game
     * from a json file into a Maze object
     *
     * @return the saved maze object
     */
    public static Maze loadGameState() throws IOException {
        File recentSave = getRecentSave();
        return readJsonFromFile(recentSave, Maze.class);
    }

    /**
     * Loads a the saved game state from a string into a Maze object
     *
     * @return the saved maze object
     */
    public static Maze loadGameState(String state) {
        return readJsonFromString(state, Maze.class);
    }

    /**
     * Gets the most recent save from the save game state directory.
     *
     * @throws FileNotFoundException if the directory is empty
     * @return the most recent file, cannot be null
     */
    private static File getRecentSave() throws FileNotFoundException {
        File[] directoryList= savedState.toFile().listFiles();

        if (directoryList == null || directoryList.length == 0){
            throw new FileNotFoundException("Directory '" + savedState + "' is empty.");
        } else {
            Arrays.sort(directoryList, Comparator.comparing(File::getName).reversed());
            assert directoryList[0] != null;
            return directoryList[0];
        }
    }

    public static int getHighestLevel() {
        try {
            Maze maze = loadGameState();
            return maze.getLevel();
        } catch (IOException e) {
            return 1;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Persistence.getRecentSave());
    }
}
