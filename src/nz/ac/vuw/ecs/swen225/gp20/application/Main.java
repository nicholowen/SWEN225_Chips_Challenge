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
    private static Maze maze = new Maze();
    private final GUI gui = new GUI(this);
    private static final Render render = new Render();

    private boolean gameEnded;
    private int timeRemaining;
    private Direction direction = null;
    private boolean paused = false;

    private int fps = 200;
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
//        Maze maze = new Maze(Persistence.getHighestLevel());
        
        try {
            Persistence.setSaveType("resume");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        if (Persistence.getSaveType().equalsIgnoreCase("unfinished")) {
            this.loadUnfinished();
        }
        if (Persistence.getSaveType().equalsIgnoreCase("resume")) {
            this.loadCurrentState();
        } else {
            this.loadLvl1();
        }
        render.init(maze);
        while (true) {
            currentState = gui.getGameState();
            if (currentState == 0) {
//                render.update(0);
                if (introCounter < 100) {
                    introCounter++;
                    System.out.println(introCounter + ":" + currentState);
                } else {
                    gui.setGameState(1);
                }
            }

            if (!gameEnded) {
                direction = gui.getDirection();
                if (gui.isRecording() && direction != null) {
                    RecordAndPlay.addMovement(direction.toString());
                }
                if (currentState == 4)
                    gui.frame.requestFocusInWindow();
                if (currentState == 4)
                    render.update(maze.tick(direction), timeRemaining, gui.getButtonSoundEvent());
                else
                    render.update(currentState, gui.getButtonSoundEvent());
                render.draw(gui.getImageGraphics(), currentState);
                gui.drawToScreen();

                long startTick = System.currentTimeMillis();
                while (true) {
                    int tickDelay = 33;
                    if (System.currentTimeMillis() >= startTick + tickDelay)
                        break; // wait 33 milli
                }
                if ((System.currentTimeMillis() >= start + delay) && currentState == 4) {
                    start = System.currentTimeMillis();
                    timeRemaining--; // timeRemaining goes down every second
                }
            }
            else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }

    // =======================================================.
    // Saving and Loading Methods
    // =======================================================.
    public void saveUnfinished() {
        try {
            Persistence.setSaveType("unfinished");
            Persistence.setHighestLevel(maze.getLevel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLvl1() {
        try {
            Persistence.setSaveType("lvl1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentState() {
        try {
            Persistence.setSaveType("resume");
            Persistence.saveGameState(maze);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUnfinished() {
        timeRemaining = maze.loadMaze(Persistence.getHighestLevel());
    }

    public void loadLvl1() {
        timeRemaining = maze.loadMaze(1);
    }

    public void loadCurrentState() {
        try {
            maze = Persistence.loadGameState();
            render.init(maze);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =======================================================.
    // Recording and Replaying Methods
    // =======================================================.
    public void startRecord() {
        RecordAndPlay.startNewRecording(maze, "lol");
    }

    public void stopRecord() {
        RecordAndPlay.stopRecording();
        RecordAndPlay.save(this);
    }

    public void replay() {
        RecordAndPlay.load("lol", this);
        RecordAndPlay.setPlaybackSpeed(fps);
        RecordAndPlay.runReplay(this);
    }

    public void runMove() {
        // Go UP
        if (direction.equals("UP")) {
            render.update(maze.tick(Direction.UP), timeRemaining, gui.getButtonSoundEvent());
        }
        // Go DOWN
        if (direction.equals("DOWN")) {
            render.update(maze.tick(Direction.DOWN), timeRemaining, gui.getButtonSoundEvent());
        }
        // Go LEFT
        if (direction.equals("LEFT")) {
            render.update(maze.tick(Direction.LEFT), timeRemaining, gui.getButtonSoundEvent());
        }
        // Go RIGHT
        if (direction.equals("RIGHT")) {
            render.update(maze.tick(Direction.RIGHT), timeRemaining, gui.getButtonSoundEvent());
        }
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
