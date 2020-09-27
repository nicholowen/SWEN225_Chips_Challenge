package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
//import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Character;
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
	 * @return Time limit in seconds of the level in question.
	 */
	public int loadMaze(int levelToLoad) {
		Level toLoad;
		try {
			toLoad=loader.read(levelToLoad);
		} catch(IOException | LevelFileException e) {
			System.out.println("CRITICAL ERROR LOADING LEVEL "+levelToLoad+" :"+e);
			return 0;//If loading the next level went wrong then don't bother doing anything else as it'll result in a crash.
		}
		
		//Load board
		board = new Cell[toLoad.getWidth()][toLoad.getHeight()];//Set up the board dimensions
		for(Tile t:toLoad.getGrid()){//For every tile on the map to load
			board[t.getX()][t.getY()] = new Cell(t.getName(), t.getX(), t.getY());//TODO:Implement or scrap Colour
		}//At this stage, all tiles are loaded (?)
		
		//Load player
		player=new Actor(true, "player", toLoad.getStartX(), toLoad.getStartY(), 15);//Player takes 15 ticks to move.
		
		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		/*
		for(Character c:toLoad.getCharacters()){
			if(c.getName().equals("chap") || c.getName().equals("player")){//If it's the player //TODO Decide on player name, "player" or "chap"
				player=new Actor(true, "player");//TODO safeguard that only one player can be loaded at once.
			} else{
				creatures.add(new Actor(false, c.getName(), c.getX(), c.getY()));
			}

		}
		*/
		return toLoad.getTimeLimit();

	}
	
	
	
	
	
	
	
	/**
	   * Enum of directions to go from a cell to cell. Mainly used by other classes.
	   */
	  public enum Direction {
	    Up, Down, Left, Right;
	  }
	  
	
	public Cell[][] getBoard(){
		return board;
	}
	
	/**
	 * Returns all of the actors on the board, including the player.
	 * @return
	 */
	public Actor[] getActors() {
		Actor[] toReturn = new Actor[creatures.size()+1];//All of the creatures, plus the player
		for(int i=0; i<creatures.size(); i++) {
			toReturn[i]=creatures.get(i);
		}
		toReturn[creatures.size()]=player;
		return toReturn;
	}
	
	
	
	
	/**
	 * One run of the core game "loop", which has the option of taking a movement.
	 * Should be run repeatedly from Application.
	 * @param movementDirection
	 */
	public RenderTuple tick(String movementDirection) {
		if(movementDirection!=null && isMoveValid(player, player.dirFromString(movementDirection)))
		player.move(movementDirection);
		player.tick();
		//TODO:Update all NPCs+Player if moving
		//TODO:Run collision detection between player and NPCs, see if an NPC is colliding with the player. If so, game over. NPCs can collide with eachother harmlessly.
		//TODO:Update all animated tiles (?) Maybe do this when Render requests tiles to draw.	
		return new RenderTuple(getActors(), getBoard());//TEMP
	}
	
	/**
	 * Given a character and a direction, check if it's a valid or invalid move.
	 * @param a
	 * @param p
	 * @return
	 */
	public boolean isMoveValid(Actor a, Point p) {
		int xToCheck=(int) (a.getX()+p.getX());
		int yToCheck=(int) (a.getY()+p.getY());
		
		if(xToCheck<0||xToCheck>=board.length||yToCheck<0||yToCheck>=board[0].length)
			return false;//Out-of-bounds, cannot walk through.
		
		Cell toCheck=board[xToCheck][yToCheck];
		return toCheck.getIsSolid();
		
		
		
	}
	
	
	
	
	
	
	
}
