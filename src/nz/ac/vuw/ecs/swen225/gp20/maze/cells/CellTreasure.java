package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellTreasure extends Cell{
	
	public CellTreasure(int xpos, int ypos) {		
		name="treasure";
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=true;
		hasPickup=true;
		isOpenable=false;
		animated=false;
		killsPlayer=false;
		}

}
