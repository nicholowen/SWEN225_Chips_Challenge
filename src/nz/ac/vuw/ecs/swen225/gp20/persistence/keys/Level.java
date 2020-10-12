package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelFileException;

import java.util.ArrayList;

class Npc extends Coordinate {
    private String type;
    private ArrayList<Coordinate> path;

    private String getType() {
        return type;
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Npc{" +
                "type='" + type + '\'' +
                ", path=" + path.size() +
                '}';
    }
}

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

    private ArrayList<Tile> grid;
    private ArrayList<Npc> npc;

    public ArrayList<Npc> getNpc() {
        return npc;
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

    public void validate() throws LevelFileException {
        if (description == null || description.equals("")) {
            throw new LevelFileException("Level must contain description");
        }

        if (properties == null){
            throw new LevelFileException("JSON is missing key 'properties'");
        }

        if (properties.timeLimit == 0) {
            throw new LevelFileException("Time limit must be greater than 0");
        }

        if (start.x < 0 || start.y >= properties.width) {
            throw new LevelFileException("Start x must be greater than 0 and less than width (" + properties.width + ")");
        }

        if (start.y < 0 || start.y >= properties.height) {
            throw new LevelFileException("Start y must be greater than 0 and less than height (" + properties.height + ")");
        }

        for (int y = 0; y < properties.height; y++) {
            for (int x = 0; x < properties.width; x++) {
                int finalX = x;
                int finalY = y;
                if (grid.stream().noneMatch(tile -> tile.getX() == finalX && tile.getY() == finalY)){
                    throw new LevelFileException("Level must contain a tile at every coordinate");
                }
            }
        }

        int chipCount = 0;
        for (Tile tile : grid) {
            int x = tile.getX();
            int y = tile.getY();

            if (x < 0 || x >= properties.width) {
                throw new LevelFileException(
                        "Tile x coordinate must be greater than 0 and less than width (" + properties.width + ")"
                );
            }

            if (y < 0 || y >= properties.height) {
                throw new LevelFileException(
                        "Tile y coordinate must be greater than 0 and less than height (" + properties.height + ")"
                );
            }

            if (tile.getName() == null) {
                throw new LevelFileException("Name of tile cannot be null");
            }

            switch (tile.getName()){
                case "key":
                    if (tile.getColor().equals("") || tile.getColor() == null) {
                        throw new LevelFileException("Key must have a color");
                    }
                    break;
                case "door":
                    if (tile.getColor().equals("") || tile.getColor() == null){
                        throw new LevelFileException("Door must have a color");
                    }
                    break;
                case "info":
                    if (tile.getHelp().equals("") || tile.getHelp() == null) {
                        throw new LevelFileException("Info must have help");
                    }
                    break;
                case "treasure":
                    chipCount++;
                default:
                    break;
            }

            if (grid.stream()
                    .filter(tile1 -> !tile1.equals(tile))
                    .anyMatch(t -> t.getX() == x && t.getY() == y)) {
                throw new LevelFileException("Duplicate tile coordinate in level at x=" + x + ", y=" + y);
            }
        }

        if (chipCount != properties.chipsInLevel){
            throw new LevelFileException("Number of chips is not consistent with 'properties.chips'");
        }

        if (properties.chipsRequired < 0 || properties.chipsInLevel < 0 ||
                properties.chipsRequired > properties.chipsInLevel) {
            throw new LevelFileException("Chips required must be greater than zero and less than chips in level");
        }
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

