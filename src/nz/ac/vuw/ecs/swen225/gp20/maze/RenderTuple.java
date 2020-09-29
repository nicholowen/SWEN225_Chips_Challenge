package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.HashMap;

public class RenderTuple {
	
	private Actor[] actorList;
	private Cell[][] board;
	private HashMap<String, Integer> inventory;
	private boolean playerStandingOnInfo;
	String info;
	
	
	public RenderTuple(Actor[] aList, Cell[][] b, HashMap<String, Integer> inv, boolean playerOnInfo, String info) {
		actorList=aList;
		board=b;
		playerStandingOnInfo=playerOnInfo;
		this.info=info;
		inventory=inv;
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
	
}
