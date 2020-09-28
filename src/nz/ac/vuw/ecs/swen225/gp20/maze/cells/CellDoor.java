package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellDoor extends Cell{
	
	public CellDoor(int xpos, int ypos, String c) {		
		name="door";
		x=xpos;
		y=ypos;
		isSolid=true;
		isTreasure=false;
		hasPickup=false;
		isOpenable=true;
		color=c;
		animated=false;
		}

}
