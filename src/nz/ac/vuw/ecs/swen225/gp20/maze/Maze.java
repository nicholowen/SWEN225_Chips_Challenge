package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.io.IOException;
import java.util.ArrayList;

import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Character;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

public class Maze {
	Cell[][] board;
	private int currentLevel;//Iterate every time a level is complete
	private int currentTreasureLeft;
	private int currentTreasureCollected;
	private boolean playerHasKey1;
	private boolean playerHasKey2;
	
	//Board logic
	private Persistence loader;
	private int boardHeight;
	private int boardWidth;
	
	
	//Actor logic
	private Actor player;//Player character, there is only one.
	private ArrayList<Actor> creatures;//Hostile NPCs
	
	/**
	 * Initializes a maze. The maze will not load a level until prompted.
	 */
	public Maze(Persistence persist) {
		loader=persist;
	}
	
	
	/**
	 * Uses the Persistence module to load a maze from file, then sets the current board to match.
	 * @param levelToLoad
	 */
	public void loadMaze(int levelToLoad) {
		Level toLoad;
		try {
			toLoad=loader.read(levelToLoad);
		} catch(IOException | LevelFileException e) {
			System.out.println("CRITICAL ERROR LOADING LEVEL "+levelToLoad+" :"+e);
			return;//If loading the next level went wrong then don't bother doing anything else as it'll result in a crash.
		}

		board = new Cell[toLoad.getWidth()][toLoad.getHeight()];//Set up the board dimensions
		for(Tile t:toLoad.getGrid()){//For every tile on the map to load
			board[t.getX()][t.getY()] = new Cell(t.getName());//TODO:Implement or scrap Colour
		}//At this stage, all tiles are loaded (?)

		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		for(Character c:toLoad.getCharacters()){
			if(c.getName().equals("chap") || c.getName().equals("player")){//If it's the player //TODO Decide on player name, "player" or "chap"
				player=new Actor(true, "player");//TODO safeguard that only one player can be loaded at once.
			} else{
				creatures.add(new Actor(false, c.getName()));
			}

		}




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
