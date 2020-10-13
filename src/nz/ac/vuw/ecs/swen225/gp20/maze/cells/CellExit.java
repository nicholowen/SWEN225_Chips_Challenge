package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellExit extends Cell{
	

	/**
	 * An exit portal which is open/active.
	 * @param xpos
	 * @param ypos
	 */
	public CellExit(int xpos, int ypos) {		
		name="exit";
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
