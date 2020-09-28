package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

public class Main {
    private static final RecordAndPlay rnp = new RecordAndPlay();
    private static final Persistence persist = new Persistence();
    private static final Maze maze = new Maze(persist);
    private static final GUI gui = new GUI();
    private static final Render render = new Render(gui.getGamePanel(), gui.getScorePanel());

    private boolean gameEnded;
    private int timeRemaining;

    public Main() {

    }
    
    /**
     * Tick based loop. The main game runs on this loop.
     */
    public void play() {
        long start = System.currentTimeMillis();
        int delay = 1000;   // 1 Second
        timeRemaining = maze.loadMaze(1);
        render.init(maze.getBoard());
        while (!gameEnded) {
            render.update(maze.tick(gui.getDirection()), timeRemaining);
            long startTick = System.currentTimeMillis();
            while (true) {
                int tickDelay = 33;
                if (System.currentTimeMillis() >= startTick + tickDelay)
                    break; // wait 33 milli
            }
            while (gui.isPaused()) {
                boolean paused = gui.isPaused();
                System.out.println(paused);
                if (!paused) break;
            }
            if (System.currentTimeMillis() >= start + delay) {
                start = System.currentTimeMillis();
                timeRemaining--; // timeRemaining goes down every second
            }
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }
}
