package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import javax.json.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class RecordAndPlay {
    private static ArrayList<Integer> actors = new ArrayList<>();

    //todo: figure out which class to talk to in order to save directions (i think best of from maze itself, could have a tile class)
    private static ArrayList<Actor.Direction> moves = new ArrayList<>(); // i'll actually need this from maze because it's just logic, and then it gets deserialized by persistence into JSON

    private static long delayTime = 123; // arbitrary number

    private static String saveFile;
    private static boolean isRecording;
    private static boolean isRunning;
    private static long startTime;

    /**
     * Method to save the recording of the game.
     */
    public static void saveRecording() {
        if (isRecording) {
            JsonArrayBuilder array = Json.createArrayBuilder();

            for (int i = 0; i < actors.size(); ++i) {
                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("actor", actors.get(i))
                        .add("moves", moves.get(i).toString());
                array.add(builder.build());
                /* example output of array
                   [
                        "actor": 1, (aka the player)
                        "moves": ["North", "East", "East", "North", "West"]
                   ]
                 */
            }

            JsonObjectBuilder builder = Json.createObjectBuilder()
                    // todo: add game state deserialized from persistence .add("game", gameState)
                    .add("moves", array)                            // output: {"moves": ["North", "East", "East", "North", "West"]}
//                    .add("timeRemaining", game.getTimeRemaining());
                    ;

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
                        moves.add(Actor.Direction.Up);
                        break;

                    case "down":
                        moves.add(Actor.Direction.Down);
                        break;

                    case "left":
                        moves.add(Actor.Direction.Left);
                        break;

                    case "right":
                        moves.add(Actor.Direction.Right);
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
    public static ArrayList<Actor.Direction> getMoves() {
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

    /**
     * Adds to the history of actions.
     *
     * @param dir direction of movement
     */
    public static void addMovement(Actor.Direction dir) {
        if (isRecording) {
            moves.add(dir);
            actors.add(0); // add the player
        }
    }
}