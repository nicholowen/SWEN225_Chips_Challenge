package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import java.util.ArrayList;

public class Npc extends Coordinate {
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
