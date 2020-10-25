package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    @Test
    public void mazeTest01() {
        // tests that player cant move through wall
        Maze maze = new Maze(1);

        //at the beginning, game should be neither won nor lost!
        assertFalse(maze.getGameLost());
        assertFalse(maze.getGameWon());

        //Make sure this isn't the last level - it's level 1 after all
        assertFalse(maze.isLastLevel());

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
        assertTrue(maze.tick(null,true).getInventory().containsKey("yellowkey"));//Make sure the yellow key was picked up.
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

        //Make sure all keys were used up.
        assertFalse(maze.tick(null,true).getInventory().containsKey("yellowkey"));
        assertFalse(maze.tick(null,true).getInventory().containsKey("redkey"));
        assertFalse(maze.tick(null,true).getInventory().containsKey("greenkey"));
        assertFalse(maze.tick(null,true).getInventory().containsKey("bluekey"));

        RenderTuple r =maze.tick(null,true);//Check treausre values are correct
        assertEquals(0, r.getTreasureLeft());
        assertEquals(9 ,r.getTreasureCollected());

        moveActor(maze, Direction.UP);

        assertEquals(7, player.getX());
        assertEquals(2, player.getY());

        //Make sure the game was won
        assertTrue(maze.getGameWon());
    }

    @Test
    public void mazeTest05() {
        //Tests that level 2 loads, and allow the NPCs to run the full course of their pathfinding
        Maze maze = new Maze(2);
        assertEquals(18, maze.getPlayer().getX());//Starting coordinates correct
        assertEquals(5,maze.getPlayer().getY());

        for(int i=0; i<128; i++)//Ensure that NPC pathfinding/Patrol segments are working properly.
            maze.tick(null,true);

        assertTrue(maze.isLastLevel());
    }

    @Test
    public void mazeTest06() {
        Maze maze = new Maze(2);
        //at the beginning, game should be neither won nor lost!
        assertFalse(maze.getGameLost());
        assertFalse(maze.getGameWon());

        assertEquals(18, maze.getPlayer().getX());//Starting coordinates correct
        assertEquals(5,maze.getPlayer().getY());

        //Assert that the player cannot clip through a wall by sending multiple inputs at once.
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        assertEquals(18, maze.getPlayer().getX());
        assertEquals(5,maze.getPlayer().getY());


        RenderTuple t = maze.tick(Direction.LEFT,true);
        assertTrue(t.playerMoved().equals(Direction.LEFT));
        assertTrue(t.getSoundEvent().equals("move"));



        //Assert that the player cannot change direction (to the right) mid-move (going left).
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        assertEquals(17, maze.getPlayer().getX());
        assertEquals(5,maze.getPlayer().getY());

        //Assert that even if the player builds up "momentum" by actually moving that they cannot clip through a wall by pressing the movement keys mid-movement.
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        maze.tick(Direction.RIGHT,true);
        assertEquals(18, maze.getPlayer().getX());
        assertEquals(5,maze.getPlayer().getY());
        }


    @Test
    public void mazeTest07() {
        Maze maze = new Maze(2);
        RenderTuple t = maze.tick(null,true);
        assertTrue(t.creatureMoved());//Creatures should start to move on the first tick


        assertFalse(t.isPlayerOnInfo());
        moveActor(maze, Direction.LEFT);
        t=maze.tick(null,true);
        assertTrue(t.isPlayerOnInfo());
        System.out.println(t.getInfo());
        assertTrue(t.getInfo()!=null);

        t=maze.tick(null,true);

        for(Actor n:t.getActors()){//Three ticks in, all NPCs should be mid-way through their movement cycles.
            if(n.getName().equals("spider"))
                assertFalse(n.hasJustMoved());
        }

    }

    @Test
    public void mazeTest08() {
        Maze maze = new Maze(2);

        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.UP);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        moveActor(maze, Direction.LEFT);
        //Make sure player isn't dead.
        assertFalse(maze.getGameLost());
        assertFalse(maze.getGameWon());

        moveActor(maze, Direction.DOWN);//Push a dirt block down
        moveActor(maze, Direction.LEFT);//Push a dirt block over the void
        moveActor(maze, Direction.LEFT);//Stand over the hovering platform
        assertFalse(maze.getGameLost());
        assertFalse(maze.getGameWon());//Make sure we're not dead.

        //Ensure the "time remaining" function can be ticked down correctly.
        int x=maze.getTimeRemaining();
        maze.tickTimeRemaining();
        assertEquals(x-1, maze.getTimeRemaining());

        moveActor(maze, Direction.LEFT);//Jump into the void.
        assertTrue(maze.getGameLost());//Ensure that we "die" properly.
    }

    private void moveActor(Maze maze, Direction direction) {
        maze.tick(direction,true);
        maze.tick(null,true);
        maze.tick(null,true);
        maze.tick(null,true);
        maze.tick(null,true);
        maze.tick(null,true);
    }
}