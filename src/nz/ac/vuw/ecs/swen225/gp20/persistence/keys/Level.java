package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;


import java.util.ArrayList;


public class Level {
    private String description;
    private int width;
    private int height;
    private Start start;
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

    public Start getStart() {
        return start;
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
                ", start=" + start +
                ", grid=" + grid.size() +
                '}';
    }
}

