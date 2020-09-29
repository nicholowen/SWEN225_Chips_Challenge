package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.GUI;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import javax.json.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class that allows for game recording and replaying. Saves and records each movement
 * done throughout a particular game, and allows these to be played back from a saved file.
 *
 * @author Melissa Lok 300472230
 */

public class RecordAndPlay {
    private static ArrayList<Integer> actors = new ArrayList<>();
    private static Queue<Integer> previousActors;

    private static ArrayList<String> moves = new ArrayList<>();
    private static Queue<String> previousMoves = new ArrayDeque<>();

    private static long playbackSpeed = 123; // arbitrary number

    private static boolean isRecording;
    private static String saveFile;
    private static String gameState;
    private static boolean isRunning;
    private static int remainingTimeAfterRun;
    private static long startTime;

    public static Thread thread;

    /**
     * Called by main to start recording
     *
     * @param game     the game state
     * @param saveName file to be saved into
     */
    public static void recording(Main game, String saveName) {
        moves.clear();
        isRecording = true;
        saveFile = saveName;
        gameState = Persistence.getGameState(game);
    }

    /**
     * Method to save the recording of the game.
     * Main method calls this and passes the time in to record every tick.
     */
    public static void saveRecording(int timeRemaining) {
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (int i = 0; i < actors.size(); ++i) {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("actor", actors.get(i))
                    .add("moves", moves.get(i));
            array.add(builder.build());
                /* example output of array
                   [
                        "actor": 1, (aka the player)
                        "moves": ["North", "East", "East", "North", "West"]
                   ]
                 */
        }

        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("game", gameState.toString())
                .add("moves", array) // output: {"moves": ["North", "East", "East", "North", "West"]}
                .add("timeRemaining", timeRemaining); // time passed from Main

        // save moves to the file
        try (Writer w = new StringWriter()) {
            Json.createWriter(w).write(builder.build());

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
                bw.write(w.toString());
                bw.close();
            } catch (IOException e) {
                throw new Error("Saving failed for movements");
            }

        } catch (IOException e) {
            throw new Error("Saving failed for movements");
        }
    }

    /**
     * loads a recording from a saved file
     *
     * @param saveFileName saved file name
     * @param game         the game
     */
    public static void loadRecording(String saveFileName, Main game) {
        JsonObject obj = null;

//        try {
//            Persistence.loadFileGameState(saveFileName, game);

        try {
            BufferedReader r = new BufferedReader(new FileReader(saveFileName));
            JsonReader jReader = Json.createReader(new StringReader(r.readLine()));
            r.close();
            obj = jReader.readObject();
        } catch (IOException e) {
            System.out.println("File reading error: " + e);
            return;
        }

        JsonArray allMoves = obj != null ? obj.getJsonArray("moves") : null;

        if (allMoves != null) {
            for (int i = 0; i < allMoves.size(); i++) {
                JsonObject obj2 = allMoves.getJsonObject(i);
                String dir = obj2.getString("moves");

                int actor = obj2.getInt("actor");
                actors.add(actor);

                switch (dir) {
                    case "up":
                        moves.add("up");
                        break;
                    case "down":
                        moves.add("down");
                        break;
                    case "left":
                        moves.add("left");
                        break;
                    case "right":
                        moves.add("right");
                        break;
                    default:
                        break;
                }
            }
        }

        // if there are moves left to be played, that means the replaying is still running
        if (moves.size() > 0) isRunning = true;
//        } catch (IOException e) {
//            System.out.println("Error: " + e);
//        }
    }

    /**
     * Stops the recording.
     */
    public static void stopRecording() {
        thread = null;
        saveFile = null;
        gameState = null;

        isRunning = false;

        moves.clear();
        actors.clear();
    }


    /**
     * This method replays the game step by step.
     *
     * @param game Game object
     */
    public static void playByStep(Main game) {
        try {
            if (isRunning && moves.size() > 0) {
                if (actors.get(0) == 0) { // if the first actor is the player
                    game.movePlayer(moves.get(0));

                    // take it out of the original moves and move it to
                    // previousMoves so the player can rewind
                    previousMoves.add(moves.get(0));
                    previousActors.add(actors.get(0));

                    moves.remove(0);
                    actors.remove(0);

                } else {
                    // in the future for level 2 mob movement
                }
            }
        } catch (IndexOutOfBoundsException ignore) {
            // swallowed
        }
    }

    /**
     * Runs the replay on a separate thread
     *
     * @param game the game
     */
    public static void runReplay(Main game) {

        // anonymous class replaced with lambda for readability
        Runnable runnable = () -> {
            while (moves.size() > 0) {
                try {
                    if (actors.get(0) == 0 && actors.size() > 0) {
                        Thread.sleep(playbackSpeed);
                    }
                    playByStep(game);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e);
                }
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    //==================================================
    //            GETTERS, SETTERS & MISC
    //==================================================

    /**
     * Gets the thread
     *
     * @return thread of recnplay
     */
    public static Thread getThread() {
        return thread;
    }

    /**
     * Gets the moves of the actors
     *
     * @return moves
     */
    public static ArrayList<String> getMoves() {
        return moves;
    }

    /**
     * Gets the actors in the game
     *
     * @return actors
     */
    public static ArrayList<Integer> getActors() {
        return actors;
    }

    /**
     * Setting the playback delay time
     *
     * @param t delay time in milliseconds.
     */
    public static void setPlaybackSpeed(long t) {
        playbackSpeed = t;
    }

    /**
     * Get state of the playback.
     *
     * @return true if the recording is being played, false otherwise
     */
    public static boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Adds to the history of actions.
     *
     * @param dir direction of movement
     */
    public static void addMovement(String dir) {
        moves.add(dir);
        actors.add(0); // add the player
    }
}

/*
        Record and Replay Games
        The record and replay module adds functionality to record game play,
        and stores the recorded games in a file (in JSON format).
        It also adds the dual functionality to load a recorded game, and to replay it.
        The user should have controls for replay: step-by-step, auto-reply, set replay speed.
        Note that this is different from the persistence module: here, not just the current game state is saved,
        but also its history (i.e., each turn or Chap and any other actors).

            features implemented (manual tested):
            1. step-by-step (somewhat_
            2. auto-reply ()
            3. set replay speed
 */

// todo: save every tick
//          more elegant way: hit record, save the state as it is, and then take in just the movements of the user