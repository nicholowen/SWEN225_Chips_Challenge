package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellWater extends Cell{
	
	public CellWater(int xpos, int ypos) {	
		name="water";
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		animated=false;
		killsPlayer=true;
		}


}