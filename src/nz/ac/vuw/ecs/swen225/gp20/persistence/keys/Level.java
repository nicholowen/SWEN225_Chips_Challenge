package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelFileException;

import java.util.ArrayList;


public class Level {
    private String description;
    private int startX;
    private int startY;
    private int width;
    private int height;
    private ArrayList<Tile> grid;

    public String getDescription() {
        return description;
    }

    public ArrayList<Tile> getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void validate() throws LevelFileException {
        if (description == null || description.equals("")) {
            throw new LevelFileException("Level must contain description");
        }

        if (startX < 0 || startX >= width) {
            throw new LevelFileException("startX must be greater than 0 and less than width");
        }

        if (startY < 0 || startY >= height) {
            throw new LevelFileException("startY must be greater than 0 and less than height");
        }

        for (Tile tile : grid) {
            int x = tile.getX();
            int y = tile.getY();

            if (x < 0 || x >= width) {
                throw new LevelFileException(
                        "tile x coordinate must be greater than 0 and less than width (" + width + ")"
                );
            }

            if (y < 0 || y >= height) {
                throw new LevelFileException(
                        "tile y coordinate must be greater than 0 and less than height (" + height + ")"
                );
            }

            if (tile.getName() == null) {
                throw new LevelFileException("Name of tile cannot be null");
            } else if (tile.getName().equals("key") && (tile.getColor().equals("") || tile.getColor() == null)) {
                throw new LevelFileException("Key must have a color");
            } else if (tile.getName().equals("door") && (tile.getColor().equals("") || tile.getColor() == null)) {
                throw new LevelFileException("Door must have a color");
            }

            if (grid.stream().anyMatch(t -> t.getX() == x)) {
                throw new LevelFileException("Duplicate x coordinate in level at x=" + x);
            }

            if (grid.stream().anyMatch(t -> t.getY() == y)) {
                throw new LevelFileException("Duplicate y coordinate in level at y=" + x);
            }
        }
    }

    @Override
    public String toString() {
        return "Level{" +
                "description='" + description + '\'' +
                ", startX=" + startX +
                ", startY=" + startY +
                ", width=" + width +
                ", height=" + height +
                ", grid=" + grid.size() +
                '}';
    }
}

