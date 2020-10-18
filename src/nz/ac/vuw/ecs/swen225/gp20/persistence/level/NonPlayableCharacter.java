package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

import java.util.ArrayList;

public class NonPlayableCharacter extends Coordinate {
    private final String type;
    private final ArrayList<Coordinate> path;

    NonPlayableCharacter(int x, int y, String type, ArrayList<Coordinate> path) {
        super(x, y);
        this.type = type;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "NonPlayableCharacter{" +
                "type='" + type + '\'' +
                ", path=" + path +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
