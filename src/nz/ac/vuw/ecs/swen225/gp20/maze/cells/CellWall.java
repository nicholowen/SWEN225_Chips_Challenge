package nz.ac.vuw.ecs.swen225.gp20.maze.cells;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;

public class CellWall extends Cell{
	
	public CellWall(int xpos, int ypos) {		
		name="wall";
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
