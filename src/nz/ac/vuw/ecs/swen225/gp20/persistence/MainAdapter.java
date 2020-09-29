package nz.ac.vuw.ecs.swen225.gp20.persistence;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import nz.ac.vuw.ecs.swen225.gp20.application.Main;
import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

import java.io.IOException;

/**
 * Specific to read and write methods to serialise and deserialize the Main class to json
 *
 * @author Campbell Whitworth 300490070
 */
public class MainAdapter extends TypeAdapter<Main> {

    @Override
    public void write(JsonWriter jsonWriter, Main game) throws IOException {
        jsonWriter.beginObject();

        Maze maze = game.getMaze();
        jsonWriter.name("level").value(maze.getLevel());
        jsonWriter.name("timeRemaining").value(game.getTimeRemaining());

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

//        writer.name("name").value(actor.getName());
        writer.name("x").value(actor.getX());
        writer.name("y").value(actor.getY());

        writer.endObject();
    }

    @Override
    public Main read(JsonReader jsonReader) throws IOException {
        Persistence persist = new Persistence();
        //return new Maze(persist);
        return null;
    }
}
