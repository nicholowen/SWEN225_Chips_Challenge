package nz.ac.vuw.ecs.swen225.gp20.recnplay;

// TODO somehow make the import classes nicer :(
//      this is so hard to follow especially when
//      saving it to the fileName string @ line 13
import nz.ac.vuw.ecs.swen225.gp20.application.Main;

public class Record {
    private int count = -1;
    private long gameEndTime;
    private boolean currentlyRecording;

    private String fileName;
    private Main game;

    public Record(Main game) {
        this.game = game;
        currentlyRecording = false;
    }

    // GETTERS AND SETTERS
    /**
     * Checks if the game is currently being recorded.
     * @return true if recording, false otherwise
     */
    public boolean currentlyRecording() {
        return currentlyRecording;
    }

    /**
     * If the game is not being recorded, set it to start recording.
     * @param recording start recording if true, false otherwise
     */
    public void setRecording(boolean recording) {
        currentlyRecording = recording;
    }

    /**
     * Returns count used for naming record file easily.
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the game's ending time.
     * @param time
     */
    public void setGameEndTime(long time) {
        gameEndTime = time;
    }

    /**
     * Gets the ending time of the recording.
     * @return gameEndTime
     */
    public long getGameEndTime() {
        return gameEndTime;
    }

    /**
     * This method is called when the game is needed to be recorded.
     */
    // TODO 1. find out what the path name is -- for saving the record 
    //      2. need to set the recording method in game to true to start recording
    //      3. ???
    //      4. profit
    public void startRecording() {
        count++;
        setRecording(true);
        fileName = ""; // in json format
    }

    /**
     * Call this method to stop recording the game.
     */
    // TODO 1. set game's recording to false
    public void stopRecording() {
        setRecording(false);
    }
}
