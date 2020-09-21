package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;


import java.util.ArrayList;


public class Level {
    private String description;
    private int width;
    private int height;
    private ArrayList<Character> characters;
    private ArrayList<Tile> grid;

    public String getDescription() {
        return description;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<Tile> getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        return "Level{" +
                "description='" + description + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", characters=" + characters +
                ", grid=" + grid.size() +
                '}';
    }
}

