package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;

import java.io.Writer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
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
    private static final Path resources = Paths.get("resources");
    private static final Path recordings = Paths.get(resources.toString(), "recordings");

    private static final ArrayList<Integer> actors = new ArrayList<>();
    private static final ArrayList<String> moves = new ArrayList<>();

    private static long playbackSpeed;
    private static int remainingTimeAfterRun;

    private static boolean isRunning;
    private static boolean currentlyRecording;

    private static String saveFile;

    /**
     * Called by main to start recording
     *
     * @param game     the game
     * @param saveName file to be saved into
     */
    public static void startNewRecording(Main game, String saveName) {
        moves.clear();
        saveFile = saveName + ".json";
        currentlyRecording = true;
        game.saveCurrentState();
    }

    /**
     * Method to save the recording of the game.
     * Main method calls this and passes the time in to record every tick.
     *
     * @param game get the time remaining from the game
     */
    public static void save(Main game) throws IOException {
        if (currentlyRecording) {

            JsonArrayBuilder array = Json.createArrayBuilder();

            // save player and player movements
            for (int i = 0; i < actors.size(); ++i) {
                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("actor", actors.get(i))
                        .add("move", moves.get(i));
                array.add(builder.build());
            }

            // build the object and att it to every single tick using time remaining fed from Main
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    // example output: {moves: [UP, LEFT, LEFT, DOWN, RIGHT]}
                    .add("moves", array)

                    // time passed from Main (the final time after running all the moves)
                    .add("timeRemaining", game.getTimeRemaining());

            // save moves -> file
            Writer w = new StringWriter();
            Json.createWriter(w).write(builder.build());

            BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(recordings.toFile().getPath(), saveFile).toFile()));
            bw.write(w.toString());
            bw.close();

            currentlyRecording = false;
        }
    }

    /**
     * loads a recording from a saved file
     *
     * @param saveFileName saved file name
     * @param game         the game
     */
    public static void load(String saveFileName, Main game) throws IOException {
        JsonObject obj;
        game.loadCurrentState();

        // clear these lists, and add moves from load file
        actors.clear();
        moves.clear();

        BufferedReader r = new BufferedReader(new FileReader(Paths.get(recordings.toFile().getPath(), saveFileName).toFile() + ".json"));
        JsonReader jReader = Json.createReader(new StringReader(r.readLine()));
        r.close();
        obj = jReader.readObject();

        JsonArray allMoves = Optional.ofNullable(obj).map(object -> object.getJsonArray("moves")).orElse(null);

        if (allMoves != null) {
            for (int i = 0; i < allMoves.size(); ++i) {
                JsonObject obj2 = allMoves.getJsonObject(i);
                String dir = obj2.getString("move");

                int actor = obj2.getInt("actor");
                actors.add(actor);

                switch (dir) {
                    case "UP":
                        moves.add("UP");
                        break;
                    case "DOWN":
                        moves.add("DOWN");
                        break;
                    case "LEFT":
                        moves.add("LEFT");
                        break;
                    case "RIGHT":
                        moves.add("RIGHT");
                        break;
                    default:
                        break;
                }
            }
        }
        // System.out.println("moves:" + moves);

        // if there are moves left to be played, that means the replaying is still running
        if (moves.size() > 0) isRunning = true;

        // update the time remaining after run
        remainingTimeAfterRun = Optional.ofNullable(obj).map(jsonObject -> jsonObject.getInt("timeRemaining")).orElse(0);

        game.runMove();
    }

    /**
     * Reset the recording.
     */
    public static void resetRecording() {
        saveFile = null;

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
    public static void playByStep(Main game) throws InterruptedException {
        try {
            if (isRunning && moves.size() > 0) {

                // if the first actor is the player
                if (actors.get(0) == 0) {
                    moves.remove(0);
                    actors.remove(0);

                /* Make game wait as long as the playback
                   speed before continuing another move */
                    synchronized (RecordAndPlay.class) {
                        RecordAndPlay.class.wait(200);
                    }
                }
//            else {
//                // in the future for level 2 mob movement
//                if (moves.size() > 0) playByStep(game);
//            }
            }

            if (moves.size() == 0) {
                isRunning = false;
                game.setTimeRemaining(remainingTimeAfterRun);
            }
            game.runMove();
        } catch (IndexOutOfBoundsException swallow) {
            // Ignored.
        }
    }

    /**
     * Runs the replay on a separate thread
     *
     * @param game the game
     */
    public static void runReplay(Main game, int playbackSpeed) throws InterruptedException {
        while (moves.size() > 0) {
            if (actors.size() > 0 && actors.get(0) == 0) {
                try {
                    Thread.sleep(playbackSpeed);
                } catch (InterruptedException ignore) {
                    // Swallowed;
                }
            }
            playByStep(game);
        }

        /* set the time back into the time remaining after running
           to reset it for the player to continue with the game */
        game.setTimeRemaining(remainingTimeAfterRun);
        isRunning = false;
    }

    //=============================================================================//
    //                           GETTERS, SETTERS & MISC                           //
    //=============================================================================//

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
     * @param speed delay time in milliseconds.
     */
    public static void setPlaybackSpeed(long speed) {
        playbackSpeed = speed;
    }

    /**
     * Get state of the playback.
     *
     * @return true if recording is running, false if not.
     */
    public static boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Get state of whether or not if the game is currently recording.
     *
     * @return true if currently recording, false if not.
     */
    public static boolean getRecording() {
        return currentlyRecording;
    }

    /**
     * Adds to the history of player actions.
     *
     * @param dir direction of movement
     */
    public static void addPlayerMovement(String dir) {
        if (currentlyRecording) {
            moves.add(dir);
            actors.add(0); // add the player
        }
    }

    /**
     * Adds to the history of enemy actions.
     *
     * @param dir         direction of enemy movement
     * @param enemyNumber which enemy (multiple)
     */
    public static void addEnemyMovement(String dir, int enemyNumber) {
        if (currentlyRecording) {
            moves.add(dir);
            actors.add(enemyNumber);
        }
    }
}