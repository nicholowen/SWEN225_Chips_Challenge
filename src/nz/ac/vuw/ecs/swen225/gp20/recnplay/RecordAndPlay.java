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
import java.util.ArrayList;

public class RecordAndPlay {
    private static ArrayList<Integer> actors = new ArrayList<>();
    private static ArrayList<String> moves = new ArrayList<>();
    private static long delayTime = 123; // arbitrary number

    private static String saveFile;
    private static String gameState;
    private static boolean isRecording;
    private static boolean isRunning;
    private static long startTime;

    public static void save(Main game, String saveName) {
        saveFile = saveName;
        isRecording = true;
        moves.clear();
        gameState = getGameState(game);
    }

    /**
     * Method to save the recording of the game.
     */
    public static void saveRecording(Main game) {
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
                .add("game", gameState)
                .add("moves", array) // output: {"moves": ["North", "East", "East", "North", "West"]}
                .add("timeRemaining", game.getTimeRemaining());

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

    public static void loadRecording(String saveFileName) {
        JsonObject obj = null;

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

            // if there are moves left to be played, that means the replaying is still running
            if (moves.size() > 0) isRunning = true;
        }
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

                }
            }
        } finally { //catch (IOException e) {}
        }
    }

    /**
     * Stops the recording.
     */
    public static void stopRecording() {
        isRunning = false;
        isRecording = false;
        saveFile = null;

        moves.clear();
        actors.clear();
    }

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
     * @param dt delay time in milliseconds.
     */
    public static void setDelayTime(long dt) {
        delayTime = dt;
    }

    /**
     * Get state of the playback.
     *
     * @return true if the recording is being played, false otherwise
     */
    public static boolean getIsRunning() {
        return isRunning;
    }

    public static boolean getIsBeingRecorded() {
        return isRecording;
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

    public static void setTimeRemaining() {

    }

    /**
     *
     * @param game
     * @return
     */
    public static String getGameState(Main game) {
        String jsonGame;

        // Json dump board
        Json.createObjectBuilder();
        JsonObjectBuilder builder;

        // Dump game info
        builder = Json.createObjectBuilder()
                .add("timeLeft", game.getTimeRemaining());

        // Compose game section
        try (Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(builder.build());
            jsonGame = writer.toString();
        } catch (IOException e) {
            throw new Error("Failed to parse game");
        }
        return jsonGame;
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
            1. step-by-step
            2. auto-reply
            3. set replay speed

 */