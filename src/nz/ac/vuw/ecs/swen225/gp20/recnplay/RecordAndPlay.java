package nz.ac.vuw.ecs.swen225.gp20.recnplay;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecordAndPlay {
    public static final Path resources = Paths.get("resources");
    public static final Path recordings = Paths.get(resources.toString(), "recordings");

    RecordAndPlay() {

    }

    public static void save(Recording recording) throws IOException {
        Persistence.saveJson(recordings.toFile(), "recording.json", recording);
    }

    public static void load() throws IOException {
        Persistence.readJsonFromFile(Paths.get(recordings.toString(), "recording.json").toFile(), Recording.class);
    }

    public static void main(String[] args) throws IOException {
        RecordAndPlay.save(new Recording(new Maze(1)));
    }
}