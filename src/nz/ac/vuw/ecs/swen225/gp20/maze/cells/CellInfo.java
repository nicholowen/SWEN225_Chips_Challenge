package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellInfo extends Cell{
	
	public CellInfo(int xpos, int ypos) {		
		name="info";
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		animated=false;
		}

}
