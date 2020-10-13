package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellExitLocked extends Cell{
	
	public CellExitLocked(int xpos, int ypos) {		
		name="exit lock";
		x=xpos;
		y=ypos;
		isSolid=true;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		animated=false;
		killsPlayer=false;
		}

}
