package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellDirt extends Cell {

    public CellDirt(int xpos, int ypos) {
        name="dirt";
        x=xpos;
        y=ypos;
        isSolid=false;
        isTreasure=false;
        hasPickup=false;
        isOpenable=false;
        animated=false;
        killsPlayer=false;
    }





}
