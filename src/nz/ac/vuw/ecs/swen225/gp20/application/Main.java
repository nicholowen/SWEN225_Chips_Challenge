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
    private static Maze maze;
    private final GUI gui = new GUI(this);
    private static Render render = new Render();

    private boolean gameEnded;
    private Direction direction = null;
    private boolean replaying = false;

    private int fps = 40;
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

        if (Persistence.getLastSaveType().equalsIgnoreCase("unfinished")) {
            this.loadUnfinished();
        }
        if (Persistence.getLastSaveType().equalsIgnoreCase("resume")) {
            this.loadCurrentState();
        } else {
            this.loadLvl(1);
        }
        while (true) {
            currentState = gui.getGameState();
            if (currentState == 0) {
                if (introCounter < 100) {
                    introCounter++;
                } else {
                    gui.setGameState(1);
                }
            }

            if (!gameEnded) {
                direction = gui.getDirection();
                if (gui.isRecording() && direction != null) {
                }
                if (currentState == 4)
                    gui.frame.requestFocusInWindow();
                if (currentState == 4)
                    render.update(maze.tick(direction), maze.getTimeRemaining(), gui.getButtonSoundEvent());
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
                    maze.tickTimeRemaining(); // timeRemaining goes down every second
                }
                if (maze.getGameWon()) {
                    if (!maze.isLastLevel()) {
                        this.loadLvl(maze.getLevel() + 1); // load next level
                    } else {
                        gui.setGameState(6); // no more levels to load - win state
                    }
                }
                if (maze.getGameLost() || maze.getTimeRemaining() == 0) {
                    this.saveUnfinished();  // update last unfinished level
                    gui.setGameState(5); // lost state
                }
            } else {
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
            Persistence.setCurrentLevel(maze.getLevel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentState() {
        try {
            Persistence.setSaveType("resume");
            Persistence.saveGameState(maze);
        } catch (IOException e) {
            maze = new Maze(1);
        }
    }

    public void loadUnfinished() {
        maze = new Maze(Persistence.getCurrentLevel());
        render.init(maze);
    }

    public void loadLvl(int lvl) {
        maze = new Maze(lvl);
        render.init(maze);
    }

    public void loadCurrentState() {
        try {
            maze = Persistence.loadGameState();
        } catch (IOException e) {
            maze = new Maze(1);
        }
        render.init(maze);
    }

    // =======================================================.
    // Recording and Replaying Methods
    // =======================================================.
    public void startRecord() {
        RecordAndPlay.startNewRecording(this, "recording");
    }

    public void stopRecord() throws IOException {
        RecordAndPlay.save(this);
    }

    public void replay() throws IOException, InterruptedException {
        RecordAndPlay.load("recording", this);
        RecordAndPlay.setPlaybackSpeed(fps);
        RecordAndPlay.runReplay(this);
        RecordAndPlay.resetRecording();
    }

    public void step() throws IOException, InterruptedException {
        if (!replaying) {
            replaying = true;
            RecordAndPlay.load("recording", this);
        }
        RecordAndPlay.playByStep(this);
        if (RecordAndPlay.getMoves().size() == 0) {
            replaying = false;
        }
    }

    public void runMove() {
        if (RecordAndPlay.getMoves().get(0).equals("UP")) {
            render.update(maze.tick(Direction.UP), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        } else if (RecordAndPlay.getMoves().get(0).equals("DOWN")) {
            render.update(maze.tick(Direction.DOWN), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        } else if (RecordAndPlay.getMoves().get(0).equals("LEFT")) {
            render.update(maze.tick(Direction.LEFT), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        } else if (RecordAndPlay.getMoves().get(0).equals("RIGHT")) {
            render.update(maze.tick(Direction.RIGHT), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        }
    }

    public void runMove(String dir) {
        if (dir.equals("UP")) {
            render.update(maze.tick(Direction.UP), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        }
        if (dir.equals("DOWN")) {
            render.update(maze.tick(Direction.DOWN), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        }
        if (dir.equals("LEFT")) {
            render.update(maze.tick(Direction.LEFT), maze.getTimeRemaining(), gui.getButtonSoundEvent());
        }
        if (dir.equals("RIGHT")) {
            render.update(maze.tick(Direction.RIGHT), maze.getTimeRemaining(), gui.getButtonSoundEvent());
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

    public int getFPS() {
        return this.fps;
    }

    public void movePlayer(String direction) {
        RecordAndPlay.addPlayerMovement(direction);
    }

    public void setTimeRemaining(int timeRemaining) {
        maze.setTimeRemaining(timeRemaining);
    }

    public int getTimeRemaining() {
        return maze.getTimeRemaining();
    }

    public void setSpeed(int speed) {
        this.fps = speed;
    }
}
