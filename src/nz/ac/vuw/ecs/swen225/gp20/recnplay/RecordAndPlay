package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.ArrayList;

public class RecordAndPlay {
    private static ArrayList<Integer> actors = new ArrayList<>();

    //todo: figure out which class to talk to in order to save directions (i think best of from maze itself, could have a tile class)
//    private static ArrayList<Maze.Directions> moves = new ArrayList<>(); // i'll actually need this from maze because it's just logic, and then it gets deserialized by persistence into JSON

    private static long delayTime = 123; // arbitrary number

    private static String saveFile;
    private static boolean isRecording;
    private static boolean isRunning;

    /**
     * Setting the playback delay time
     * @param dt delay time in milliseconds
     */
    public static void setDelayTime(long dt) {
        delayTime = dt;
    }

    /**
     * Get state of the playback
     * @return true if the recording is being played, false otherwise
     */
    public static boolean getIsRunning (){
        return isRunning;
    }

    public static void saveReplay() {
        if (isRecording) {
            JsonArrayBuilder array = Json.createArrayBuilder();

        }
    }
}
