package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.HashMap;

public class RenderTuple {
	
	private Actor[] actorList;
	private Cell[][] board;
	private HashMap<String, Integer> inventory;
	private boolean playerStandingOnInfo;
	private String info;
	private int treasureTaken;
	private int treasureLeft;
	
	/**
	 * The RenderTuple is designed to be passed to the renderer, and contains all of the information that the renderer needs to draw the maze each tick.
	 * @param aList All actors on screen
	 * @param b The board, all cells in a 2D array
	 * @param inv Player inventory
	 * @param playerOnInfo Whether or not the player is standing on an "Info" tile
	 * @param info If the player is standing on an info tile, the info to be displayed.
	 * @param treasureCollected The current amount of treasure the player has collected.
	 * @param treasureLeft The current amount of treasure that is left for the player to collect.
	 */
	public RenderTuple(Actor[] aList, Cell[][] b, HashMap<String, Integer> inv, boolean playerOnInfo, String info, int treasureCollected, int treasureLeft) {
		actorList=aList;
		board=b;
		playerStandingOnInfo=playerOnInfo;
		this.info=info;
		inventory=inv;
		treasureTaken=treasureCollected;
		this.treasureLeft=treasureLeft;
	}
	
	public Actor[] getActors() {
		return actorList;
	}
	
	public Cell[][] getCells(){
		return board;
	}
	
	public HashMap<String, Integer> getInventory() {
		return inventory;
	}
	public boolean isPlayerOnInfo() {
		return playerStandingOnInfo;
	}
	public String getInfo() {
		return info;
	}

	public int getTreasureCollected() {
		return treasureTaken;
	}

	public int getTreasureLeft() {
		return treasureLeft;
	}
	
}
