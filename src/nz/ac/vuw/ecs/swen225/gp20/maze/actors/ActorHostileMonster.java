package nz.ac.vuw.ecs.swen225.gp20.maze.actors;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Coordinate;

import java.util.ArrayList;

public class ActorHostileMonster extends Actor {

    public static final int ACTOR_HOSTILE_MONSTER_SPEED=15;

    public ActorHostileMonster(String name, int x, int y, ArrayList<Coordinate> path){
        super(false, name, x, y, ACTOR_HOSTILE_MONSTER_SPEED);
        killsPlayer=true;

    }



}
