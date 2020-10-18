package nz.ac.vuw.ecs.swen225.gp20.application;

import java.io.IOException;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

/**
 * This class handles the main loop where the game runs. It also sends all the
 * info needed to different classes every tick.
 *
 * @author Maiza Rehan 300472305
 *
 */
public class Main {
    private static final RecordAndPlay rnp = new RecordAndPlay();
    private static final Maze maze = new Maze();
    private static final GUI gui = new GUI();
    private static final Render render = new Render();

    private boolean gameEnded;
    private int timeRemaining;
    private Direction direction = null;
    private boolean paused = false;

    private int fps = 30;
    private int introCounter;
    private int currentState;

    public Main() {

    }

    /**
     * Tick based loop. The main game runs on this loop.
     */
    public void play() {
        long start = System.currentTimeMillis();
        int delay = 1000; // 1 Second
        timeRemaining = maze.loadMaze(1);
        render.init(maze);
        while (true) {
            currentState = gui.getGameState();
            if(currentState == 0) {
//                render.update(0);
                if (introCounter < 100) {
                    introCounter++;
                    System.out.println(introCounter + ":" + currentState);
                } else {
                    gui.setGameState(1);
                }
            }

            if (!gameEnded && !paused) {
                if(currentState == 4) gui.frame.requestFocusInWindow();
                checkUpdates();
                if(currentState == 4) render.update(maze.tick(direction), timeRemaining, maze.getPlayerInventory());
                else render.update(currentState);
                render.draw(gui.getImageGraphics(), currentState);
                gui.drawToScreen();
                
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
            } else {
                checkUpdates();
               
            }
        }
    }

    /**
     * Checks for user input updates from the GUI class. Handles saving, loading,
     * updating direction and pausing behaviours.
     */
    public void checkUpdates() {
        // Update paused
        paused = gui.isPaused();
        // Update direction
        direction = gui.getDirection();
        // Check if user wants to save
        if (gui.isSaving()) {
            try {
                Persistence.saveGameState(maze);
                System.out.println("Saving");
                gui.saved(true);
            } catch (IOException e) {
                gui.saved(true); // true for now (will change later)
                System.out.println("Error With Saving State");
            }
        }

        if (gui.getLoadState() != null) {
            //Persistence.loadGameState();
            System.out.println(gui.getLoadState());
            gui.setLoadState(null);
        }
        // Check if user wants to load game, and which kind of state
//        if (gui.getLoadState() != null) {
//            if (gui.getLoadState().equalsIgnoreCase("resume")) {
//                System.out.println("resume");
//                gui.setLoadState(null);
//                // RESUME A GAME
//            } else if (gui.getLoadState().equalsIgnoreCase("unfinished")) {
//                System.out.println("unfinished");
//                // LOAD GAME AT LAST UNFINISHED LEVEL
//                gui.setLoadState(null);
//            } else if (gui.getLoadState().equalsIgnoreCase("lvl 1")) {
//                System.out.println("lvl 1");
//                // LOAD GAME AT LEVEL 1
//                gui.setLoadState(null);
//            }
//        }

    }
    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }

    // =======================================================.
    // Utility Methods for Persistence and RecnPlay
    // =======================================================.

    /**
     * Gets the maze.
     *
     * @return the maze
     */
    public Maze getMaze() {
        return maze;
    }

    
    /**
     * Set refresh-rate of replay in frames per second.
     *
     * @param fps frames per second.
     */
    public void setFPS(int fps) {
      this.fps = fps;
    }
    
    /**
     * Set the time remaining.
     *
     * @param timeRemaining the time remaining.
     */
    public void setTimeRemaining(int timeRemaining) {
      this.timeRemaining = timeRemaining;
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
