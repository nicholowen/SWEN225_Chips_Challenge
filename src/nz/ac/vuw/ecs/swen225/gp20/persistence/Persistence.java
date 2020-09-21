package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;


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
    public Level read(int level) throws FileNotFoundException, JsonSyntaxException, LevelFileException {
        File file = getFile(level);
        JsonReader reader = new JsonReader(new FileReader(file.getAbsoluteFile()));

        Level levelObj = gson.fromJson(reader, Level.class);
        return parseLevel(levelObj);
    }

    /**
     * Alternative read method used for testing purposes.
     *
     * @param json well formed JSON string
     * @return ?
     * @throws JsonSyntaxException {@inheritDoc}
     */
    public Level read(String json) throws JsonSyntaxException, LevelFileException {
        Level levelObj = gson.fromJson(json, Level.class);
        return parseLevel(levelObj);
    }

    private Level parseLevel(Level level) throws LevelFileException {
        System.out.println(level);

        String[][] board = new String[level.getWidth()][level.getHeight()];

        for (Tile tile : level.getGrid()) {
            int x = tile.getX();
            int y = tile.getY();

            if (tile.getName() == null) {
                throw new LevelFileException("Name of tile cannot be null");
            }

            if (board[x][y] != null) {
                throw new LevelFileException("Duplicate tile location in file");
            }

            board[tile.getX()][tile.getY()] = tile.getName();
        }

        if (containsNull(board)) {
            throw new LevelFileException("Width and height of the board does not match grid size");
        }

        if (level.getCharacters() == null || level.getCharacters().size() == 0) {
            throw new LevelFileException("Level contains no characters");
        }

        if (level.getCharacters().stream().noneMatch(x -> x.getName().equals("chap"))) {
            throw new LevelFileException("Level must contain a chap");
        }

        return level;
    }

    /**
     * Helper method that calls the recursive containsNull()
     */
    private boolean containsNull(String[][] board) {
        return containsNull(board, 0);
    }

    /**
     * Recursive method that checks if a board array contains any null values
     */
    private boolean containsNull(String[][] board, int row) {
        if (row >= board.length) return false;
        List<String> rowArray = Arrays.asList(board[row]);
        if (rowArray.contains(null)) return true;
        return containsNull(board, row + 1);
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
        } catch (FileNotFoundException | LevelFileException e) {
            e.printStackTrace();
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
        } catch (LevelFileException e) {
            e.printStackTrace();
        }
    }
}
