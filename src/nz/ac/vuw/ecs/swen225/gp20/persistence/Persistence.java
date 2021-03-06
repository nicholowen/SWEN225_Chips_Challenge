package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Level;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to handle reading of json level files and
 * reading and writing of game state
 *
 * @author Campbell Whitworth 300490070
 */
public class Persistence {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final Path levels = Paths.get("resources", "levels");
    public static final Path recordings = Paths.get("resources", "recordings");
    public static final Path savedState = Paths.get("resources", "saved-state");

    /**
     * Reads a level from a file into an object
     *
     * @param level the level number
     * @return a Level object
     * @throws IOException         if the level is not found
     * @throws JsonSyntaxException {@inheritDoc}
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

    public static <T> T readJsonFromFile(File file, Class<T> type) throws IOException {
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
     * Helper method to check if a directory exists,
     * creates a new directory if it doesn't
     *
     * @param path the filepath to check
     * @throws IOException if there is an error creating the new directory
     */
    private static void checkDirectory(File path) throws IOException {
        if (!path.exists() && !path.mkdirs()) {
            throw new IOException("Error creating directory, check that you have permission");
        }

        assert path.exists();
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

        saveJson(savedState.toFile(), filename, maze);
    }

    private static <T> void saveJson(File file, String filename, T toSave) throws IOException {
        checkDirectory(file);

        OutputStream outputStream = new FileOutputStream(Paths.get(file.getPath(), filename).toFile());
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        gson.toJson(toSave, bufferedWriter);
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
     * @throws IOException if there is an error reading from file
     */
    public static Maze loadGameState() throws IOException {
        File recentSave = getRecentSave();
        return readJsonFromFile(recentSave, Maze.class);
    }

    /**
     * Loads a the saved game state from a string into a Maze object
     *
     * @param state a json string representing the maze state
     * @return the saved maze object
     */
    public static Maze loadGameState(String state) {
        return readJsonFromString(state, Maze.class);
    }

    /**
     * Gets the most recent save from the save game state directory.
     *
     * @return the most recent file, cannot be null
     * @throws FileNotFoundException if the directory is empty
     */
    private static File getRecentSave() throws FileNotFoundException {
        File[] directoryList = savedState.toFile()
                .listFiles((dir, name) -> name.matches("\\d{8}-\\d{6}-game-state.json"));

        if (directoryList == null || directoryList.length == 0) {
            throw new FileNotFoundException("Directory '" + savedState + "' is empty.");
        } else {
            List<File> sortedFiles = Arrays.stream(directoryList)
                    .sorted(Comparator.comparing(File::getName).reversed())
                    .collect(Collectors.toList());

            assert sortedFiles.get(0) != null;
            return sortedFiles.get(0);
        }
    }

    /**
     * Utility class for saving and loading game state
     */
    private static class Settings {
        int currentLevel;
        String lastSaveType;

        Settings(int currentLevel, String lastSaveType) {
            this.currentLevel = currentLevel;
            this.lastSaveType = lastSaveType;
        }
    }

    /**
     * Helper method to load a settings object from json
     *
     * @return the gamed saved game settings
     * @throws IOException {@inheritDoc}
     */
    private static Settings loadSaveSettings() throws IOException {
        File file = Paths.get(savedState.toString(), "save-settings.json").toFile();
        return readJsonFromFile(file, Settings.class);
    }

    /**
     * Helper method to save a settings object to json
     *
     * @param settings the settings object to save
     * @throws IOException {@inheritDoc}
     */
    private static void setSaveSettings(Settings settings) throws IOException {
        saveJson(savedState.toFile(), "save-settings.json", settings);
    }

    /**
     * Sets the highest level in json
     *
     * @param currentLevel the level number
     * @throws IOException {@inheritDoc}
     */
    public static void setCurrentLevel(int currentLevel) throws IOException {
        Preconditions.checkArgument(currentLevel > 0, "Level cannot be null");
        Settings settings = new Settings(currentLevel, getLastSaveType());
        setSaveSettings(settings);
    }

    /**
     * Sets the
     *
     * @param saveType a String to set the last save type to
     * @throws IOException {@inheritDoc}
     */
    public static void setSaveType(String saveType) throws IOException {
        Settings settings = new Settings(getCurrentLevel(), saveType);
        setSaveSettings(settings);
    }

    /**
     * Get the type of the last save from json file
     *
     * @return the type of save, if doesn't exist returns "default"
     */
    public static String getLastSaveType() {
        try {
            Settings settings = loadSaveSettings();
            if (settings != null && settings.lastSaveType != null) {
                return settings.lastSaveType;
            }
        } catch (IOException ignored) {
        }
        return "default";
    }

    /**
     * Get the highest level that has been reached by the player
     *
     * @return an integer representing the level number
     */
    public static int getCurrentLevel() {
        try {
            Settings settings = loadSaveSettings();
            if (settings != null && settings.currentLevel >= 1) {
                return settings.currentLevel;
            }
        } catch (IOException ignored) {
        }
        return 1;
    }

    /**
     * Gets the maximum number of levels in the game.
     * Assumes all the levels are in sequential order
     * e.g. 1, 2, 3 etc.
     *
     * @return the count of the levels in the game
     */
    public static int getNumberOfLevels() {
        File[] directoryList = levels.toFile()
                .listFiles((dir, name) -> name.matches("level\\d.*.json"));

        if (directoryList == null) {
            return 0;
        } else {
            return directoryList.length;
        }
    }
}
