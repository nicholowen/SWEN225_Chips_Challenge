package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellFree extends Cell{
	
	public CellFree(int xpos, int ypos) {		
		name="free";
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		animated=false;

		}

}
