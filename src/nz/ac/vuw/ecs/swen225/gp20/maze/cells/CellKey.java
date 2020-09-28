package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellKey extends Cell{
	
	public CellKey(int xpos, int ypos, String c) {		
		name="key";
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=false;
		hasPickup=true;
		isOpenable=false;
		pickupName="key:"+c;
		color=c;
		animated=false;
		
		}

}
