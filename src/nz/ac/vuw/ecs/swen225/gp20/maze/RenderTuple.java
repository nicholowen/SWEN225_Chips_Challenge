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
