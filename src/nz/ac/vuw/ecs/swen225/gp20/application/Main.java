package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

public class Main {
    private static Maze maze;// These are only static for now, cna change if it proves to be an issue.
    private static Render render;// That said, these are never going to be deleted and replaced, so static may be
                                 // better.
    private static RecordAndPlay rnp;
    private static Persistence persist;
    private static KeyListeners keyListeners;
    private static boolean gameEnded;
    private static int timeRemaining;

    public static void main(String[] args) {
        persist = new Persistence();// Persistance may need to be a parameter for Maze/Record so set it up first!
                                    // Change if necessary.
        maze = new Maze(persist);
        render = new Render();
//        rnp = new RecordAndPlay();
        keyListeners = new KeyListeners(render.getGp());
        play();
    }

    /**
     * Tick based loop. The main game runs on this loop.
     */
    public static void play() {
        long start = System.currentTimeMillis();
        int delay = 1000;   // 1 Second
        timeRemaining = maze.loadMaze(1);
        render.init(maze.getBoard());
        while (true) {
            System.out.println(timeRemaining);
            if (gameEnded)
                break;
            maze.tick(keyListeners.getDirection());
            render.update(new RenderTuple(maze.getActors(), maze.getBoard()));
            long startTick = System.currentTimeMillis();
            while (true) {
                int tickDelay = 33;
                if (System.currentTimeMillis() >= startTick + tickDelay)
                    break; // wait 33 milli
            }
            if (System.currentTimeMillis() >= start + delay) {
                start = System.currentTimeMillis();
                timeRemaining--; // timeRemaining goes down every second
            }
        }
    }
}
