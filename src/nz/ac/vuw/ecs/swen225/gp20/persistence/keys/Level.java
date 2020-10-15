package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

class Coordinate {
    public int x;
    public int y;

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class Properties {
    public int chipsInLevel;
    public int chipsRequired;
    public int width;
    public int height;
    public int timeLimit;

    @Override
    public String toString() {
        return "Properties{" +
                "chipsInLevel=" + chipsInLevel +
                ", chipsRequired=" + chipsRequired +
                ", width=" + width +
                ", height=" + height +
                ", timeLimit=" + timeLimit +
                '}';
    }
}

public class Level {
    private String description;
    private Coordinate start;
    private Properties properties;

    private ArrayList<Tile> grid = new ArrayList<>();
    private ArrayList<NonPlayableCharacter> nonPlayableCharacters = new ArrayList<>();

    public ArrayList<NonPlayableCharacter> getNonPlayableCharacters() {
        return nonPlayableCharacters;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Tile> getGrid() {
        return grid;
    }

    public int getWidth() {
        return properties.width;
    }

    public int getHeight() {
        return properties.height;
    }

    public int getStartX() {
        return start.x;
    }

    public int getStartY() {
        return start.y;
    }

    public int getTimeLimit(){
        return properties.timeLimit;
    }

    public int getChipsInLevel(){
        return properties.chipsInLevel;
    }

    public int getChipsRequired(){
        return properties.chipsRequired;
    }

    private void validateCharacter(NonPlayableCharacter character) {
        System.out.println(character);
    }

    private void validateTile(Tile tile) {
        Preconditions.checkArgument(tile.x >= 0 && tile.x < properties.width,
                "Tile x coordinate must be greater than 0 and less than width (" + properties.width + ")");

        Preconditions.checkArgument(tile.y >= 0 && tile.y < properties.height,
                "Tile y coordinate must be greater than 0 and less than height (" + properties.height + ")");

        if (tile.getTerrain() != null){
            switch (tile.getTerrain()){
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

        if (tile.getObject() != null){
            switch (tile.getObject()){
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

