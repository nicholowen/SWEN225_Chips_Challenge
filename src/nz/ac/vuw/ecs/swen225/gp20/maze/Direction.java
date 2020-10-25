package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;

public enum Direction {
    UP(0,-1),
    DOWN(0,1),
    LEFT(-1,0),
    RIGHT(1,0);

    private final Point direction;

    Direction(int x, int y){
        this.direction = new Point(x, y);
    }

    public Point getDirection() {
        return direction;
    }

    /**
     * If for some reason you want to use a string to get the direction enum, this will do that. Returns null, takes uppercase only.
     * @param s Name of the direction, "UP", "DOWN", "LEFT", "RIGHT"
     * @return Corresponding Direction enum or null if input was invalid
     */
    public Direction get(String s){
        switch(s){
            case "UP":
                return Direction.UP;
            case "DOWN":
                return Direction.DOWN;
            case "LEFT":
                return Direction.LEFT;
            case "RIGHT":
                return Direction.RIGHT;
        }
        return null;
    }

}
