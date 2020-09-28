package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.IOException;

public class MazeAdapter extends TypeAdapter<Maze> {

    @Override
    public void write(JsonWriter jsonWriter, Maze maze) throws IOException {
        jsonWriter.beginObject();

        jsonWriter.name("level").value(maze.getLevel());

        jsonWriter.name("actors");
        writeActorsArray(jsonWriter, maze.getActors());

        jsonWriter.endObject();
    }

    /**
     * Loops through all the actors in the game and writes them
     *
     * @param writer a json writer to write to
     * @param actors array of actors in the game
     * @throws IOException {@inheritDoc}
     */
    private void writeActorsArray(JsonWriter writer, Actor[] actors) throws IOException {
        writer.beginArray();
        for (Actor actor : actors) {
            writeActor(writer, actor);
        }
        writer.endArray();
    }

    /**
     * Writes an actor to an object in json
     *
     * @param writer a json writer to write to
     * @param actor the actor
     * @throws IOException {@inheritDoc}
     */
    private void writeActor(JsonWriter writer, Actor actor) throws IOException {
        writer.beginObject();

        writer.name("name").value(actor.getName());
        writer.name("x").value(actor.getX());
        writer.name("y").value(actor.getY());

        writer.endObject();
    }

    @Override
    public Maze read(JsonReader jsonReader) throws IOException {
        Persistence persist = new Persistence();
        return new Maze(persist);
    }
}
