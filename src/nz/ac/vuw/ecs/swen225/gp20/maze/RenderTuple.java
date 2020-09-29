package nz.ac.vuw.ecs.swen225.gp20.maze;

public class RenderTuple {
	
	private Actor[] actorList;
	private Cell[][] board;
	private String[] inventory;
	private boolean playerStandingOnInfo;
	String info;
	
	
	public RenderTuple(Actor[] aList, Cell[][] b, String[] inv, boolean playerOnInfo, String info) {
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
	
	public String[] getInventory() {
		return inventory;
	}
	public boolean isPlayerOnInfo() {
		return playerStandingOnInfo;
	}
	public String getInfo() {
		return info;
	}
	
}
