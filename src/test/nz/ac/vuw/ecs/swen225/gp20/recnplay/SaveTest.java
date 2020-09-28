package test.nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.RecordAndPlay;
import org.junit.Test;

public class SaveTest {

    private Main game = new Main();

    @Test
    public void simpleMoveRecording() {
        RecordAndPlay.recording(game.getTimeRemaining(), "save1move.json");
        game.movePlayer("up");
        RecordAndPlay.saveRecording(game.getTimeRemaining());
        assert RecordAndPlay.getMoves().size() == 1;
        System.out.println("saved");
    }

    @Test
    public void playTest() {

    }
}
