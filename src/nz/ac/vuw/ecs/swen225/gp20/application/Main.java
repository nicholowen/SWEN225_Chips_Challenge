package nz.ac.vuw.ecs.swen225.gp20.application;

import java.io.IOException;

import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
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
    private Maze maze;
    private final GUI gui = new GUI(this);
    private static final Render render = new Render();

    private Direction direction = null;

    private int speed = 300;
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
        // Decides what state/level to load based on previous exit
        if (Persistence.getLastSaveType().equalsIgnoreCase("unfinished")) {
            this.loadUnfinished();
        } else if (Persistence.getLastSaveType().equalsIgnoreCase("resume")) {
            this.loadCurrentState();
        } else {
            this.loadLvl(1);
        }
        while (true) {
            currentState = gui.getGameState();
            // Plays the intro screen (logo fade)
            if (currentState == 0) {
                if (introCounter < 100) {
                    introCounter++;
                } else {
                    gui.setGameState(1);
                }
            }

            RenderTuple rt = maze.tick(direction);
            direction = gui.getDirection();
            if (gui.isRecording() && rt.playerMoved() != null) {
                RecordAndPlay.addPlayerMovement(rt.playerMoved().toString()); // Record player movements
            }
            if (currentState == 4) {
                gui.frame.requestFocusInWindow();
            }
            if (currentState == 4) {
                render.update(rt, maze.getTimeRemaining(), gui.getButtonSoundEvent(), gui.isRecording(),
                        gui.isReplaying(), this.getSpeedCategory());
            } else {
                render.update(currentState, gui.getButtonSoundEvent());
            }
            render.draw(gui.getImageGraphics(), currentState);
            gui.drawToScreen();

            long startTick = System.currentTimeMillis();
            while (true) {
                int tickDelay = 33;
                if (System.currentTimeMillis() >= startTick + tickDelay)
                    break; // wait 33 milli
            }
            if ((System.currentTimeMillis() >= start + delay) && currentState == 4 && !gui.isReplaying()) {
                start = System.currentTimeMillis();
                maze.tickTimeRemaining(); // timeRemaining goes down every second
            }
            if (maze.getGameWon()) {
                if (!maze.isLastLevel()) {
                    this.loadLvl(maze.getLevel() + 1); // load next level
                    this.saveUnfinished(); // update last unfinished level
                } else {
                    gui.setReplaying(false);
                    gui.stopRecording();
                    gui.setGameState(6); // no more levels to load - win state
                }
            }
            if (maze.getGameLost() || maze.getTimeRemaining() == 0) {
                gui.setReplaying(false);
                gui.stopRecording();
                this.saveUnfinished(); // update last unfinished level
                gui.setGameState(5); // lost state
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
    /**
     * Sets the current level in Persistence, and sets the save type to unfinished,
     * so when the game is run next time, the last unfinished level is loaded.
     * 
     */
    public void saveUnfinished() {
        try {
            Persistence.setSaveType("unfinished");
            Persistence.setCurrentLevel(maze.getLevel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the current state of the game in Persistence, and sets the save type to
     * resume, so when the game is run next time, this state is loaded.
     * 
     */
    public void saveCurrentState() {
        try {
            Persistence.setSaveType("resume");
            Persistence.saveGameState(maze);
        } catch (IOException e) {
            maze = new Maze(1);
        }
    }

    /**
     * Loads the last unfinished level.
     * 
     */
    public void loadUnfinished() {
        maze = new Maze(Persistence.getCurrentLevel());
        render.init(maze);
    }

    /**
     * Loads the specified level.
     * 
     * @param lvl to load
     */
    public void loadLvl(int lvl) {
        maze = new Maze(lvl);
        render.init(maze);
    }

    /**
     * Loads the last saved state. If there is an error, the last unfinished level
     * is loaded instead.
     * 
     */
    public void loadCurrentState() {
        try {
            maze = Persistence.loadGameState();
        } catch (IOException e) {
            maze = new Maze(Persistence.getCurrentLevel());
        }
        render.init(maze);
    }

    // =======================================================.
    // Recording and Replaying Methods
    // =======================================================.
    /**
     * Starts a new recording.
     * 
     */
    public void startRecord() {
        RecordAndPlay.startNewRecording(this, "recording");
    }

    /**
     * Stops and saves recording.
     * 
     */
    public void stopRecord() throws IOException {
        RecordAndPlay.save(this);
    }

    /**
     * Loads and replays saved recording.
     * 
     */
    public void replay() throws IOException, InterruptedException {
        if (!gui.isReplaying()) {
            gui.setReplaying(true);
            RecordAndPlay.load("recording", this);
        }
        RecordAndPlay.runReplay(this, speed);
        gui.setReplaying(false); // Replay is finished or canceled
        RecordAndPlay.resetRecording();
    }

    /**
     * Loads the saved recording if not already loaded. If recording is already
     * playing, this method allows player to take one recorded step.
     * 
     */
    public void step() throws IOException, InterruptedException {
        if (!gui.isReplaying()) {
            gui.setReplaying(true);
            RecordAndPlay.load("recording", this);
        }
        RecordAndPlay.playByStep(this);
        if (RecordAndPlay.getMoves().size() == 0) {
            gui.setReplaying(false);
        }
    }

    /**
     * Used by RecordAndPlay to force the player to move in a specific direction.
     * 
     * @return moved - wheather or not the move was executed
     * 
     */
    public boolean runMove() {
        boolean moved = false;
        if (RecordAndPlay.getMoves().size() > 0) {
            gui.setReplaying(true);
            gui.frame.requestFocusInWindow();
            // Updates maze with direction based on string returned by RecordAndPlay
            // Updates render with info in order to move the character
            if (RecordAndPlay.getMoves().get(0).equals("UP")) {
                render.update(maze.tick(Direction.UP), maze.getTimeRemaining(), gui.getButtonSoundEvent(),
                        gui.isRecording(), gui.isReplaying(), this.getSpeedCategory());
            } else if (RecordAndPlay.getMoves().get(0).equals("DOWN")) {
                render.update(maze.tick(Direction.DOWN), maze.getTimeRemaining(), gui.getButtonSoundEvent(),
                        gui.isRecording(), gui.isReplaying(), this.getSpeedCategory());
            } else if (RecordAndPlay.getMoves().get(0).equals("LEFT")) {
                render.update(maze.tick(Direction.LEFT), maze.getTimeRemaining(), gui.getButtonSoundEvent(),
                        gui.isRecording(), gui.isReplaying(), this.getSpeedCategory());
            } else if (RecordAndPlay.getMoves().get(0).equals("RIGHT")) {
                render.update(maze.tick(Direction.RIGHT), maze.getTimeRemaining(), gui.getButtonSoundEvent(),
                        gui.isRecording(), gui.isReplaying(), this.getSpeedCategory());
            }
            moved = true; // The move has been executed
        } else {
            gui.setReplaying(false); // There are no more moves to execute
        }
        return moved; // The move was not executed
    }

    // =======================================================.
    // Getters And Setters
    // =======================================================.
    /**
     * Gets the speed category for render.
     *
     * @return speed category 1 - slowest, 4 - fastest
     */
    private int getSpeedCategory() {
        switch (this.speed) {
        case 150:
            return 4;
        case 300:
            return 3;
        case 750:
            return 2;
        case 1000:
            return 1;
        }
        return 3;
    }

    /**
     * Gets the maze. 
     * Used by Persistence and RecordAndPlay
     *
     * @return the maze
     */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Set speed used for replaying a recording. (higher number -> longer wait ->
     * slower replay)
     *
     * @param speed - milliseconds to wait
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Get speed used for replaying a recording. (higher number -> longer wait ->
     * slower replay)
     *
     * @return speed - milliseconds to wait
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Used by Persistence and RecordAndPlay to set the time after loading state, or
     * after replaying a recording.
     *
     * @param timeRemaining
     */
    public void setTimeRemaining(int timeRemaining) {
        maze.setTimeRemaining(timeRemaining);
    }

    /**
     * Gets the time remaining to finish current level.
     *
     * @return timeRemaining
     */
    public int getTimeRemaining() {
        return maze.getTimeRemaining();
    }
}
