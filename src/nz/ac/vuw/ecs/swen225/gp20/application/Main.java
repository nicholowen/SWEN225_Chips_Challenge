package nz.ac.vuw.ecs.swen225.gp20.application;

import java.io.IOException;

import com.google.gson.annotations.JsonAdapter;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.MainAdapter;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

@JsonAdapter(MainAdapter.class)
/**
 * This class handles the main loop where the game runs. It also sends all the
 * info needed to different classes every tick.
 * 
 * @author Maiza Rehan 300472305
 *
 */
public class Main {
    private static final RecordAndPlay rnp = new RecordAndPlay();
    private static final Persistence persist = new Persistence();
    private static final Maze maze = new Maze();
    private static final GUI gui = new GUI();
    private static final Render render = new Render(gui.getGamePanel(), gui.getScorePanel());

    private boolean gameEnded;
    private int timeRemaining;
    private String direction = null;

    public Main() {

    }

    /**
     * Tick based loop. The main game runs on this loop.
     */
    public void play() {
        long start = System.currentTimeMillis();
        int delay = 1000; // 1 Second
        timeRemaining = maze.loadMaze(1);
        render.init(maze.getBoard());
        while (!gameEnded) {
            checkUpdates();
            render.update(maze.tick(direction), timeRemaining);

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

    /**
     * Checks for user input updates from the GUI class. Handles saving, loading,
     * updating direction and pausing behaviours.
     */
    public void checkUpdates() {
        while (gui.isPaused()) {
            boolean paused = gui.isPaused();
            System.out.println(paused);
            if (!paused)
                break;
        }
        // Update direction
        direction = gui.getDirection();
        // Check if user wants to save
        if (gui.isSaving()) {
//            try {
//                persist.saveGameState(this);
            System.out.println("Saving");
            gui.saved(true);
//            } catch (IOException e) {
//                gui.saved(true);    // true for now (will change later)
//                System.out.println("Error With Saving State");
//            }
        }
        // Check if user wants to load game, and which kind of state
        if (gui.getLoadState() != null) {
            if (gui.getLoadState().equalsIgnoreCase("resume")) {
                System.out.println("resume");
                gui.setLoadState(null);
                // RESUME A GAME
            } else if (gui.getLoadState().equalsIgnoreCase("unfinished")) {
                System.out.println("unfinished");
                // LOAD GAME AT LAST UNFINISHED LEVEL
                gui.setLoadState(null);
            } else if (gui.getLoadState().equalsIgnoreCase("lvl 1")) {
                System.out.println("lvl 1");
                // LOAD GAME AT LEVEL 1
                gui.setLoadState(null);
            }
        }

    }

    /**
     * Gets the maze.
     *
     * @return the maze
     */
    public Maze getMaze() {
        return maze;
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }

    // ======================================
    // ONLY FOR RECNPLAY TESTING PURPOSES
    // ======================================

    public void movePlayer(String direction) {
        RecordAndPlay.addMovement(direction);
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }
}
