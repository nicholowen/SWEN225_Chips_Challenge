package nz.ac.vuw.ecs.swen225.gp20.maze;

public class RenderTuple {
	
	private Actor[] actorList;
	private Cell[][] board;
	
	
	public RenderTuple(Actor[] aList, Cell[][] b) {
		actorList=aList;
		board=b;
	}
	
	public Actor[] getActors() {
		return actorList;
	}
	
	public Cell[][] getCells(){
		return board;
	}
	
}
