package nz.ac.vuw.ecs.swen225.gp20.application;

import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;
import nz.ac.vuw.ecs.swen225.gp20.persistence.Persistence;
import nz.ac.vuw.ecs.swen225.gp20.recnplay.*;
import nz.ac.vuw.ecs.swen225.gp20.render.Render;

public class Main {
    private static Maze maze;//These are only static for now, cna change if it proves to be an issue.
    private static Render render;//That said, these are never going to be deleted and replaced, so static may be better.
    private static RecordAndPlay recNPlay;
    private static Persistence persist;


    public static void main(String[] args){
        persist = new Persistence();//Persistance may need to be a parameter for Maze/Record so set it up first! Change if necessary.
        maze = new Maze(persist);
        render = new Render();
        recNPlay = new RecordAndPlay();

    }


}
