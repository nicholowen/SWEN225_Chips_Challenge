package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Preconditions;
import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.*;

/**
 * Class to handle loading of json files into objects
 * that can be used by the rest of the game components
 *
 * @author Campbell Whitworth 300490070
 */
public class Level {
    private String description;
    private Coordinate start;
    public Properties properties;
    private ArrayList<Tile> grid = new ArrayList<>();
    private ArrayList<NonPlayableCharacter> nonPlayableCharacters = new ArrayList<>();

    private Cell[][] board;

    /* GETTERS */
    public ArrayList<NonPlayableCharacter> getNonPlayableCharacters() {
        return nonPlayableCharacters;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Tile> getGrid() {
        return grid;
    }

    public int getStartX() {
        return start.x;
    }

    public int getStartY() {
        return start.y;
    }

    public Cell[][] getBoard() {
        if (board == null) loadBoard();
        return board;
    }

    private void loadBoard() {
        board = new Cell[properties.width][properties.height];
        for (Tile tile : grid) {
            String terrain = tile.getTerrain();
            String object = tile.getObject();

            if (terrain == null && object == null) {
                board[tile.x][tile.y] = new CellFree(tile.x, tile.y);
                continue;
            }

            if (terrain == null) {
                switch (object) {
                    case "treasure":
                        board[tile.x][tile.y] = new CellTreasure(tile.x, tile.y);
                        break;
                    case "key":
                        board[tile.x][tile.y] = new CellKey(tile.x, tile.y, tile.getColor());
                        break;
                    default:
                        break;
                }

                continue;
            }

            switch (terrain) {
                case "wall":
                    board[tile.x][tile.y] = new CellWall(tile.x, tile.y);
                    break;
                case "door":
                    board[tile.x][tile.y] = new CellDoor(tile.x, tile.y, tile.getColor());
                    break;
                case "info":
                    board[tile.x][tile.y] = new CellInfo(tile.x, tile.y, tile.getHelp());
                    break;
                case "exit":
                    CellExit exit = new CellExit(tile.x, tile.y);
                    board[tile.x][tile.y] = exit;
                    break;
                case "exit lock":
                    CellExitLocked exitLocked = new CellExitLocked(tile.x, tile.y);
                    board[tile.x][tile.y] = exitLocked;
                    break;
                case "water":
                    board[tile.x][tile.y] = new CellWater(tile.x, tile.y);
                    break;
            }
        }
    }

    private boolean checkX(int x) {
        return x >= 0 && x < properties.width;
    }

    private boolean checkY(int y) {
        return y >= 0 && y < properties.height;
    }

    private boolean checkXY(int x, int y) {
        return checkX(x) && checkY(y);
    }

    /* METHODS TO VALIDATE JSON */
    private void validateCharacter(NonPlayableCharacter character) {
        List<String> allowed = List.of("spider");
        Preconditions.checkArgument(allowed.contains(character.getType()),
                "NonPlayableCharacter 'type' cannot be " + character.getType() + " or null must be one of " + allowed);

        Preconditions.checkArgument(checkXY(character.x, character.y),
                "Character (" + character + ") outside board");

        ListIterator<Coordinate> iter = character.getPath().listIterator();
        while (iter.hasNext()) {
            Coordinate coordinate = iter.next();

            Preconditions.checkArgument(checkXY(coordinate.x, coordinate.y), "Path outside board");

            if (!iter.hasPrevious()) {
                Preconditions.checkArgument(character.x == coordinate.x || character.y == coordinate.y,
                        "Path must be a straight line");
            }

            if (!iter.hasNext()) {
                Preconditions.checkArgument(coordinate.x == character.x && coordinate.y == character.y,
                        "Last coordinate in path must be starting position");
            }

            if (iter.hasPrevious()) {
                Coordinate prev = iter.previous();
                Preconditions.checkArgument(prev.x == coordinate.x || prev.y == coordinate.y,
                        "Path must be a straight line");

                iter.next();
            }
        }
    }

    private void validateTile(Tile tile) {
        Preconditions.checkArgument(checkX(tile.x),
                "Tile x coordinate must be greater than 0 and less than width (" + properties.width + ")");

        Preconditions.checkArgument(checkY(tile.y),
                "Tile y coordinate must be greater than 0 and less than height (" + properties.height + ")");

        if (tile.getTerrain() != null) {
            switch (tile.getTerrain()) {
                case "door":
                    Preconditions.checkNotNull(tile.getColor(),
                            "Tile with terrain 'door' must have 'color'");
                    break;
                case "info":
                    Preconditions.checkNotNull(tile.getHelp(),
                            "Tile with terrain 'info' must have 'help'");
                    break;
                default:
                    List<String> allowed = List.of("door", "info", "wall", "exit", "exit lock", "water");
                    Preconditions.checkArgument(allowed.contains(tile.getTerrain()) || tile.getTerrain() == null,
                            "Terrain cannot be " + tile.getTerrain() + ", must be null or one of " + allowed);
                    break;
            }
        }

        if (tile.getObject() != null) {
            switch (tile.getObject()) {
                case "key":
                    Preconditions.checkNotNull(tile.getColor(),
                            "Tile with object 'key' must have 'color'");
                    break;
                default:
                    List<String> allowed = List.of("key", "treasure", "dirt");
                    Preconditions.checkArgument(allowed.contains(tile.getObject()) || tile.getObject() == null,
                            "Terrain cannot be " + tile.getObject() + ", must be null or one of " + allowed);
                    break;
            }
        }

        boolean isDuplicateCoordinate = grid.stream()
                .filter(tile1 -> !tile1.equals(tile))
                .noneMatch(t -> t.x == tile.x && t.y == tile.y);
        Preconditions.checkArgument(isDuplicateCoordinate,
                "Duplicate tile coordinate in level");
    }

    /**
     * Method to check if the json is valid
     */
    public void validate() {
        Preconditions.checkNotNull(description, "Level must contain description");
        Preconditions.checkNotNull(properties, "Level must contain properties");

        Preconditions.checkArgument(
                properties.timeLimit >= 0,
                "Time limit must be greater than 0");

        Preconditions.checkArgument(
                start.x >= 0 && start.x < properties.width,
                "Start x cannot be negative and must be less than width (" + properties.width + ")");

        Preconditions.checkArgument(
                start.y >= 0 && start.y < properties.height,
                "Start y must be greater than 0 and less than height (" + properties.height + ")");

        Preconditions.checkArgument(
                properties.chipsRequired > 0 && properties.chipsInLevel > 0 && properties.chipsRequired >= properties.chipsInLevel,
                "Chips required must be greater than zero and less than chips in level");

        long chipCount = grid.stream()
                .filter(tile -> tile.getObject() != null)
                .filter(tile -> tile.getObject().equals("treasure"))
                .count();
        Preconditions.checkArgument(chipCount == properties.chipsInLevel,
                "Number of chips in level is not consistent with 'properties.chips'");

        for (int y = 0; y < properties.height; y++) {
            for (int x = 0; x < properties.width; x++) {
                int finalX = x;
                int finalY = y;
                Preconditions.checkArgument(grid.stream().anyMatch(tile -> tile.x == finalX && tile.y == finalY),
                        "Level must contain a tile at every coordinate");
            }
        }

        grid.forEach(this::validateTile);
        nonPlayableCharacters.forEach(this::validateCharacter);
    }

    @Override
    public String toString() {
        return "Level {\n" +
                "\tdescription='" + description + "'\n" +
                "\tstart=" + start + "\n" +
                "\tproperties=" + properties + "\n" +
                "\tgrid=" + grid.size() + "\n" +
                '}';
    }
}

