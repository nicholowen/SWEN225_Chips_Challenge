package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;

public enum Direction {
    UP(0,-1),
    DOWN(0,1),
    LEFT(-1,0),
    RIGHT(1,0),
    NONE(0, 0);

    private final Point direction;

    Direction(int x, int y){
        this.direction = new Point(x, y);
    }

    public Point getDirection() {
        return direction;
    }
}
