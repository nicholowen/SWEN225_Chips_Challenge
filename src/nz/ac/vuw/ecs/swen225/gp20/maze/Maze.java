package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.ArrayList;

import nz.ac.vuw.ecs.swen225.gp20.persistence.*;

public class Maze {
	Cell[][] Board;
	private int currentLevel;//Iterate every time a level is complete
	private int currentTreasureLeft;
	private int currentTreasureCollected;
	private boolean playerHasKey1;
	private boolean playerHasKey2;
	
	//Board logic
	private int boardHeight;
	private int boardWidth;
	
	
	//Actor logic
	private Actor player;//Player character, there is only one.
	private ArrayList<Actor> creatures;//Hostile NPCs
	
	/**
	 * Initializes a maze. The maze will not load a level until prompted.
	 */
	public Maze(Persistence persist) {
		
	}
	
	
	/**
	 * Uses the Persistence module to load a maze from file, then sets the current board to match.
	 * @param levelToLoad
	 */
	public void loadMaze(int levelToLoad) {
		
	}
	
	
	/**
	 * One run of the core game "loop", which has the option of taking a movement.
	 * Should be run repeatedly from Application, or whatever is in control of running the game otherwise.
	 * @param movementDirection
	 */
	public void tick(String movementDirection) {
		//TODO:If given move command and not currently moving, set player to move animation. +Check if player move is valid!
		//TODO:Check if player/actors have finished move animations. If so, set them to their new location.
		//TODO:Update all animated tiles (?) Maybe do this when Render requests tiles to draw.	
		//TODO:
	}
	
	
	
	
	
	
	
}
