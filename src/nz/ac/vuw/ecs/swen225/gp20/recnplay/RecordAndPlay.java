package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import java.io.Writer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 * Class that allows for game recording and replaying. Saves and records each movement
 * done throughout a particular game, and allows these to be played back from a saved file.
 *
 * @author Melissa Lok 300472230
 */

public class RecordAndPlay {
    private static ArrayList<Integer> actors = new ArrayList<>();
    private static ArrayList<String> moves = new ArrayList<>();

    private static long playbackSpeed = 123; // arbitrary number
    private static int remainingTimeAfterRun;

    private static String saveFile;
    private static String gameState;

    private static boolean isRunning;
    private static boolean currentlyRecording;

    public static Thread thread;

    /**
     * Called by main to start recording
     *
     * @param maze     the maze state
     * @param saveName file to be saved into
     */
    public static void newSaveOfRecording(Maze maze, String saveName) {
        moves.clear();
        saveFile = saveName;
        currentlyRecording = true;
        gameState = Persistence.getGameState(maze); // save the current state the game is currently in during recoding
    }

    /**
     * Method to save the recording of the game.
     * Main method calls this and passes the time in to record every tick.
     */
    public static void save(int timeRemaining) {
        if (currentlyRecording) {

            JsonArrayBuilder array = Json.createArrayBuilder();

            // save player and player movements now
            for (int i = 0; i < actors.size(); ++i) {
                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("actor", actors.get(i))
                        .add("move", moves.get(i));
                array.add(builder.build());
            }

            // build the object and att it to every single tick using time remaining fed from Main
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("game", gameState)
                    // save the current game state as is

                    .add("moves", array)
                    // example output: {"moves": ["North", "East", "East", "North", "West"]}

                    .add("timeRemaining", timeRemaining);
            // time passed from Main (the final time after running all the moves)

            // save moves -> file
            try (Writer w = new StringWriter()) {
                Json.createWriter(w).write(builder.build());

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
                    bw.write(w.toString());
                    bw.close();
                } catch (IOException e) {
                    throw new Error("Writing failed for movements");
                }

            } catch (IOException e) {
                throw new Error("Saving failed for movements");
            }
            currentlyRecording = false;
        }
    }

    /**
     * loads a recording from a saved file
     *
     * @param saveFileName saved file name
     * @param game         the game
     */
    public static void load(String saveFileName, Main game) {
        JsonObject obj = null;

        try {
            Persistence.loadGameState();

            // load moves of player into array
            actors.clear();
            moves.clear();

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
                            moves.add("UP");
                            break;
                        case "down":
                            moves.add("DOWN");
                            break;
                        case "left":
                            moves.add("LEFT");
                            break;
                        case "right":
                            moves.add("RIGHT");
                            break;
                        default:
                            break;
                    }
                }
            }

            // if there are moves left to be played, that means the replaying is still running
            if (moves.size() > 0) isRunning = true;

            // update the time remaining after run
            remainingTimeAfterRun = obj != null
                    ? obj.getInt("timeRemaining") : 0;

//            game.updateGUI();

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }


    /**
     * Stops the recording.
     */
    public static void stopRecording() {
        thread = null;
        saveFile = null;
        gameState = null;

        isRunning = false;
        currentlyRecording = false;

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
                if (actors.get(0) == 0) {
                    // if the first actor is the player
                    game.movePlayer(moves.get(0));
                    moves.remove(0);
                    actors.remove(0);

                } else {
                    // in the future for level 2 mob movement
                    if (moves.size() > 0) playByStep(game);
                }

                if (moves.size() > 0) {
                    isRunning = false;
//                    game.setTimeRemaining(remainingTimeAfterRun);
                }
//                game.updateGUI();
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

//        game.setFPS((int) (1000 / playbackSpeed));

        // anonymous class replaced with lambda for readability
        Runnable runnable = () -> {
            while (moves.size() > 0) {
                try {
                    if (actors.get(0) == 0 && actors.size() > 0)
                        Thread.sleep(playbackSpeed);
                    playByStep(game);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e);
                }
            }
            isRunning = false;
//            game.setTimeRemaining(remainingTimeAfterRun);
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
        if (currentlyRecording) {
            moves.add(dir);
            actors.add(0); // add the player
        }
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
            1. step-by-step (80% done)
            2. auto-reply (done done) // call runReplay without setting playback speed.
            3. set replay speed (done) // call runReplay and set playback speed.
 */