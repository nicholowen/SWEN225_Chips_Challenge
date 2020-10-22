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
    public static final Path resources = Paths.get("resources");
    public static final Path recordings = Paths.get(resources.toString(), "recordings");

    private static ArrayList<Integer> actors = new ArrayList<>();
    private static ArrayList<String> moves = new ArrayList<>();

    private static long playbackSpeed = 123; // arbitrary number
    private static int remainingTimeAfterRun;
    private static int tick = 0;

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
    public static void startNewRecording(Main game, String saveName) {
        moves.clear();
        saveFile = saveName + ".json";
        currentlyRecording = true;
//        gameState = Persistence.getGameState(maze); // save the current state the game is currently in during recoding

        game.saveCurrentState();

//        Persistence.saveGameState(Persistence.getGameState(maze));
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
                    // save the current game state as is
//                    .add("game", gameState)

                    // example output: {"moves": ["UP", "LEFT", "LEFT", "DOWN", "RIGHT"]}
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
    public static void load(String saveFileName, Main game) throws IOException, InterruptedException {
        JsonObject obj;

//        Persistence.loadGameState(gameState);
//        Persistence.readJsonFromFile(Paths.get(recordings.toString(), "recording.json").toFile(), RecordAndPlay.class);
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
        System.out.println("moves:" + moves);

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

    /*
        to run this, have a button in GUI or somewhere where main can call
        Recordnplay.setPlaybackSpeed(time) where
        time:
            100  = 0.1s (slow)
            200  = 0.2s
            500  = 0.5s
            1000 = 1.0s (fast)
     */
    public static void playByStep(Main game) {
        try {
            if (isRunning && moves.size() > 0) {
                if (actors.get(0) == 0) {

                    // if the first actor is the player
                    game.runMove(moves.get(0));

                    moves.remove(0);
                    actors.remove(0);

                    synchronized (RecordAndPlay.class) {
                        RecordAndPlay.class.wait(playbackSpeed);
                    }

                    // no use for saving popped/removed moves into another
                    // temp list because we don't need back stepping.

//            } else {
//                // in the future for level 2 mob movement
//                if (moves.size() > 0) playByStep(game);
                }

                if (moves.size() == 0) {
                    isRunning = false;
                    game.setTimeRemaining(remainingTimeAfterRun);
                }
                game.runMove();
            }
        } catch (IndexOutOfBoundsException | InterruptedException ignore) {
        }
    }

    /**
     * Runs the replay on a separate thread
     *
     * @param game the game
     */
    public static void runReplay(Main game) {
        game.setSpeed((int) (1000 / playbackSpeed));
        while (moves.size() > 0) {
            System.out.println(moves);
            if (actors.size() > 0 && actors.get(0) == 0) {
                try {
                    Thread.sleep(playbackSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            playByStep(game);
        }
        game.setTimeRemaining(remainingTimeAfterRun);
        isRunning = false;
    }

    // MIGHT BE LEX'S TICKS MESSING THE MOVES.

    //==================================================
    //            GETTERS, SETTERS & MISC
    //==================================================

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
     * @return true if recoding is running, false if not.
     */
    public static boolean getIsRunning() {
        return isRunning;
    }

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
     * @param dir
     * @param enemyNumber
     */
    public static void addEnemyMovement(String dir, int enemyNumber) {
        if (currentlyRecording) {
            moves.add(dir);
            actors.add(enemyNumber);
        }
    }

    /**
     * Keep track of time syncing
     */
    public static void addOneTick() {
        tick += 1;
    }

    public static void getDirection() {

    }

    public static void main(String[] args) {
        Main stuff = new Main();
        moves.add("UP");
//        RecordAndPlay.load("testRecording.txt", stuff);
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
            1. step-by-step (80% done, only enemies left)
            2. auto-reply (done done) // call runReplay without setting playback speed.
            3. set replay speed (done) // call runReplay and set playback speed.
 */