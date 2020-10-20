package nz.ac.vuw.ecs.swen225.gp20.maze.actors;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Coordinate;

import java.util.ArrayList;

public class ActorNeutralDirt extends Actor {

    public ActorNeutralDirt(int x, int y){
        super(false, "dirt", x, y, 0);
        killsPlayer=false;
        blocksMovement=true;//By default, until it's pushed over "water", it blocks movement
        isPushable=true;//Can be moved by the player!
    }

    /**
     * The "fill in" mode, where this actor effectively becomes uninteractable with. Originally this would have been where it turned into a tile.
     * However, to ensure that the "gaps" or "water" end up rendered properly, this happens instead.
     * THIS INTERACTS WITH NOTHING OUTSIDE OF THE DIRT BLOCK.
     * WATER BLOCK MUST BE CHANGED SEPERATELY WITH NULLIFY!
     * Also make sure to check that the water hasn't already been "nullified"
     */
    public void fillInMode(){
        blocksMovement=false;
        isPushable=false;
    }


}
