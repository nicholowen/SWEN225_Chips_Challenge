package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

import java.util.ArrayList;

public class NonPlayableCharacter extends Coordinate {
    private String type;
    private ArrayList<Coordinate> path;

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
