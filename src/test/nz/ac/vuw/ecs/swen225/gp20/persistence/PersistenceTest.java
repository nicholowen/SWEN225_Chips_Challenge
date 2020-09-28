package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.persistence.LevelFileException;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class PersistenceTest {
    @Test
    public void persistenceTest01() throws FileNotFoundException, LevelFileException {
        Level level = Persistence.read(1);
    }

    @Test
    public void persistenceTest02() {
        String jsonString =
                "{" +
                "  \"description\": \"Level 1\"," +
                "  \"start\": {\"x\": 4, \"y\": 0}," +
                "  \"properties\": {" +
                "    \"chipsInLevel\": 0," +
                "    \"chipsRequired\": 0," +
                "    \"width\": 4," +
                "    \"height\": 1" +
                //"    \"timeLimit\": 360" +
                "  }," +
                "  \"grid\": [" +
                "      {\"x\": 0, \"y\": 0, \"name\": \"free\"}," +
                "      {\"x\": 1, \"y\": 0, \"name\": \"free\"}," +
                "      {\"x\": 2, \"y\": 0, \"name\": \"free\"}," +
                "      {\"x\": 3, \"y\": 0, \"name\": \"wall\"}" +
                "   ]" +
                "}";

        testHelper(jsonString, "Time limit must be greater than 0");
    }

    @Test
    public void persistenceTest03() {
        String jsonString =
                "{" +
                        "  \"description\": \"Level 1\"," +
                        "  \"start\": {\"x\": 4, \"y\": 0}," +
                        "  \"properties\": {" +
                        "    \"chipsInLevel\": 0," +
                        "    \"chipsRequired\": 0," +
                        "    \"width\": 4," +
                        "    \"height\": 1," +
                        "    \"timeLimit\": 360" +
                        "  }," +
                        "  \"grid\": [" +
                        "      {\"x\": 0, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 1, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 2, \"y\": 0, \"name\": \"free\"}," +
                        "      {\"x\": 4, \"y\": 0, \"name\": \"wall\"}" +
                        "   ]" +
                        "}";

        testHelper(jsonString, "Level must contain a tile at every coordinate");
    }

    private void testHelper(String jsonString, String message) {
        LevelFileException thrown = assertThrows(LevelFileException.class, () -> Persistence.read(jsonString));
        assertEquals(thrown.getMessage(), message);
    }
}
