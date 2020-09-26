package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
    public static boolean getIsRunning (){
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
                /* output of array
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
}
