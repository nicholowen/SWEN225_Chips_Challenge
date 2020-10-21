package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    @Test
    public void mazeTest01() {
        // tests that player cant move through wall
        Maze maze = new Maze(1);

        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);

        Actor player = maze.getPlayer();

        assertEquals(5, player.getX());
        assertEquals(6, player.getY());
    }

    @Test
    public void mazeTest02() {
        // tests that player cant open door without key
        Maze maze = new Maze(1);

        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);

        Actor player = maze.getPlayer();

        assertEquals(6, player.getX());
        assertEquals(8, player.getY());
    }

    @Test
    public void mazeTest03() {
        // tests that player can pickup key
        Maze maze = new Maze(1);

        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.UP);

        assertEquals(1, maze.getPlayerInventory().get("greenkey"));
        assertFalse(maze.getBoard()[8][5].hasPickup());
    }

    @Test
    public void mazeTest04() {
        // tests that player can open all doors and finish
        Maze maze = new Maze(1);
        Actor player = maze.getPlayer();

        moveActor(maze, Direction.RIGHT);

        // check pickup of key
        assertTrue(maze.getBoard()[8][5].hasPickup());
        moveActor(maze, Direction.UP);
        assertFalse(maze.getBoard()[8][5].hasPickup());

        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);

        // check pickup of orb
        assertTrue(maze.getBoard()[10][2].hasPickup());
        moveActor(maze, Direction.RIGHT);
        assertFalse(maze.getBoard()[10][2].hasPickup());

        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT); // pickup yellow key
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.DOWN);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.RIGHT);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);

        assertEquals(7, player.getX());
        assertEquals(2, player.getY());

        // check gameover here
    }

    private void moveActor(Maze maze, Direction direction) {
        maze.tick(direction);
        maze.tick(null);
        maze.tick(null);
        maze.tick(null);
        maze.tick(null);
        maze.tick(null);
    }
}