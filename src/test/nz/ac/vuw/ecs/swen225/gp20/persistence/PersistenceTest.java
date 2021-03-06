package test.nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Level;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
    @Test
    public void persistenceTest01() {
        JsonObject level = createLevel(
                0,
                0,
                5,
                5,
                8,
                8,
                60);

        testCorrect(level);
    }

    @Test
    public void persistenceTest02() {
        JsonObject level = createLevel(
                -1,
                -1,
                5,
                5,
                10,
                10,
                60);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest03() {
        JsonObject level = createLevel(
                0,
                0,
                6,
                5,
                10,
                10,
                60);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest04() {
        JsonObject level = createLevel(
                10,
                0,
                4,
                5,
                10,
                10,
                60);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest05() {
        JsonObject level = createLevel(
                10,
                0,
                4,
                5,
                10,
                10,
                -1);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest06() {
        JsonObject level = createLevel(
                9,
                0,
                4,
                0,
                10,
                10,
                60);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest07() {
        JsonObject level = createLevel(
                9,
                0,
                0,
                0,
                10,
                10,
                60);

        testIncorrect(level);
    }

    @Test
    public void persistenceTest08() {
        JsonObject level = createLevel(
                9,
                0,
                1,
                1,
                10,
                10,
                60);

        testCorrect(level);
    }

    @Test
    public void persistenceTest09() {
        JsonObject level = createLevel(
                "A Level",
                9,
                0,
                1,
                1,
                10,
                10,
                60);

        Level levelObj = Persistence.read(level.toString());

        assertEquals(levelObj.getDescription(), "A Level");
        assertEquals(levelObj.getStartX(), 9);
        assertEquals(levelObj.getStartY(), 0);
        assertEquals(levelObj.properties.chipsInLevel, 1);
        assertEquals(levelObj.properties.chipsRequired, 1);
        assertEquals(levelObj.properties.width, 10);
        assertEquals(levelObj.properties.height, 10);
        assertEquals(levelObj.properties.timeLimit, 60);
    }

    @Test
    public void persistenceTest10() {
        assertThrows(FileNotFoundException.class, () -> Persistence.read(99));
    }

    @Test
    public void persistenceTest11() {
        assertThrows(JsonSyntaxException.class, () -> Persistence.read("Invalid Json"));
    }

    @Test
    public void persistenceTest12() {
        File path = new File("resources/saved-state");
        File[] directoryList= path.listFiles((dir, name) -> name.matches("\\d{8}-\\d{6}-game-state.json"));

        if (directoryList == null || directoryList.length == 0) {
            assertThrows(IOException.class, Persistence::loadGameState);
        } else {
            try {
                Persistence.loadGameState();
            } catch (IOException e) {
                throw new Error("Persistence.loadGameState() should not throw IOException");
            }
        }
    }

    @Test
    public void persistenceTest13() {
        JsonObject level = createLevel(
                9,
                0,
                4,
                4,
                10,
                10,
                60);

        JsonArray nonPlayableCharacters = new JsonArray();
        JsonObject npc1 = new JsonObject();
        npc1.addProperty("x", 1);
        npc1.addProperty("y", 1);
        npc1.addProperty("type", "spider");

        JsonArray path = new JsonArray();

        JsonObject coordinate1 = new JsonObject();
        coordinate1.addProperty("x", 2);
        coordinate1.addProperty("y", 1);
        path.add(coordinate1);

        JsonObject coordinate2 = new JsonObject();
        coordinate2.addProperty("x", 1);
        coordinate2.addProperty("y", 1);
        path.add(coordinate2);

        npc1.add("path", path);

        nonPlayableCharacters.add(npc1);
        level.add("nonPlayableCharacters", nonPlayableCharacters);

        testCorrect(level);
    }

    @Test
    public void persistenceTest14() throws IOException {
        // check can save and load the maze from file
        Maze mazeA = new Maze(1);
        Persistence.saveGameState(mazeA);
        Persistence.loadGameState();
    }

    @Test
    public void persistenceTest15() {
        // check can save and load the maze from string
        Maze mazeA = new Maze(1);
        String mazeJson = Persistence.getGameState(mazeA);
        Persistence.loadGameState(mazeJson);
    }

    @Test
    public void persistenceTest16() throws IOException {
        // check saving highest level
        Persistence.setCurrentLevel(1);
        assertEquals(1, Persistence.getCurrentLevel());

        assertThrows(IllegalArgumentException.class, () -> Persistence.setCurrentLevel(-1));
        assertEquals(1, Persistence.getCurrentLevel());
    }

    @Test
    public void persistenceTest17() throws IOException {
        // check saving of save type
        Persistence.setSaveType("some type");
        assertEquals("some type", Persistence.getLastSaveType());

        Persistence.setSaveType(null);
        assertEquals("default", Persistence.getLastSaveType());
    }

    private JsonObject createLevel(int startX, int startY, int chipsInLevel, int chipsRequired,
                                   int width, int height, int timeLimit) {

        return createLevel("description", startX, startY, chipsInLevel, chipsRequired, width, height, timeLimit);
    }

    private JsonObject createLevel(String description, int startX, int startY, int chipsInLevel, int chipsRequired,
                                   int width, int height, int timeLimit) {

        JsonObject level = setupLevel(description, startX, startY, chipsInLevel, chipsRequired, width, height, timeLimit);
        JsonArray grid = createGrid(width, height, chipsInLevel);

        level.add("grid", grid);

        return level;
    }

    private JsonObject setupLevel(String description, int startX, int startY, int chipsInLevel, int chipsRequired,
                                  int width, int height, int timeLimit) {
        JsonObject level = new JsonObject();

        level.addProperty("description", description);

        JsonObject start = new JsonObject();
        start.addProperty("x", startX);
        start.addProperty("y", startY);
        level.add("start", start);

        JsonObject properties = new JsonObject();
        properties.addProperty("chipsInLevel", chipsInLevel);
        properties.addProperty("chipsRequired", chipsRequired);
        properties.addProperty("width", width);
        properties.addProperty("height", height);
        properties.addProperty("timeLimit", timeLimit);

        level.add("properties", properties);

        return level;
    }

    private JsonArray createGrid(int width, int height, int chipsInLevel) {
        JsonArray grid = new JsonArray();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid.add(createCell(i, j, null, null));
            }
        }

        for (int i = 0; i < chipsInLevel; i++) {
            JsonElement cell = grid.get(i);
            int x = cell.getAsJsonObject().get("x").getAsInt();
            int y = cell.getAsJsonObject().get("y").getAsInt();
            JsonElement terrain = cell.getAsJsonObject().get("terrain");
            String terrainString;

            if (terrain instanceof JsonNull) terrainString = null;
            else terrainString = terrain.getAsString();

            grid.set(i, createCell(x, y, "treasure", terrainString));
        }

        return grid;
    }

    private JsonObject createCell(int x, int y, String object, String terrain) {
        JsonObject cell = new JsonObject();

        cell.addProperty("x", x);
        cell.addProperty("y", y);
        cell.addProperty("object", object);
        cell.addProperty("terrain", terrain);

        return cell;
    }

    private void testCorrect(JsonObject level) {
        try {
            Persistence.read(level.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Test shouldn't throw exception");
        }
    }

    private void testIncorrect(JsonObject level) {
        try {
            Persistence.read(level.toString());
            throw new Error("Test should throw exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
