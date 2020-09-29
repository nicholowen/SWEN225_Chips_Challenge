package test.nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SaveTest {

    private Main game = new Main(); // this causes error in CI as it opens the GUI.

    @Test
    public void simpleMoveRecording() {
        RecordAndPlay.recording(game, "save1move.json");
        game.movePlayer("up");
        RecordAndPlay.saveRecording(20);
        assert RecordAndPlay.getMoves().size() == 1;
        System.out.println("saved");
    }

    @Test
    public void playTest() {

    }
}
