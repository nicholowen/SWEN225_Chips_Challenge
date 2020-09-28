package test.nz.ac.vuw.ecs.swen225.gp20.maze;

import com.google.gson.JsonSyntaxException;
import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelFileException;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import org.junit.Test;

import java.io.FileNotFoundException;

public class MazeTest {
    Persistence persist = new Persistence();

    @Test()
    public void mazeTest01() {
        try {
            Level level = persist.read(1);
        } catch (FileNotFoundException | JsonSyntaxException | LevelFileException e) {
            System.out.println("here1");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test(expected = LevelFileException.class)
    public void mazeTest02() throws LevelFileException {
        String jsonString =
                "{" +
                        " \"description\": \"Level 1\"," +
                        "  \"width\": 15," +
                        "  \"height\": 14," +
                        "  \"startX\": 7," +
                        "  \"startY\": 6," +
                        "  \"grid\": [" +
                        "      {\"x\": 0, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 1, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 2, \"y\": 0, \"name\": \"wall\"}" +
                        "  ]" +
                        "}";

        try {
            Level level = persist.read(jsonString);
        } catch (JsonSyntaxException | LevelFileException e) {
            System.out.println("here2");
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
