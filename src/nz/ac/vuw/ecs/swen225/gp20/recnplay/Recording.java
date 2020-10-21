package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.util.HashMap;
import java.util.Map;

public class Recording {
    private final Map<Long, Direction> moves = new HashMap<>();
    private final Maze maze;

    public Recording(Maze maze) {
        this.maze = maze;
    }

    public void addMove(long tick, Direction direction) {
        moves.put(tick, direction);
    }

    public Map<Long, Direction> getMoves() {
        return moves;
    }

    public Maze getMaze() {
        return maze;
    }
}