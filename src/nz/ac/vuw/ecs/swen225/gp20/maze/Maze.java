package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import nz.ac.vuw.ecs.swen225.gp20.maze.cells.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

public class Maze {
	private static Cell[][] board;
	private int currentLevel;//Iterate every time a level is complete
	private int currentTreasureLeft;
	private int currentTreasureCollected;
	private ArrayList<String> playerInventory;
	
	//Board logic
	private int boardHeight;
	private int boardWidth;
	private ArrayList<Cell> exitList;
	
	
	//Actor logic
	private Actor player;//Player character, there is only one.
	private ArrayList<Actor> creatures;//Hostile NPCs
	
	/**
	 * Initializes a maze. The maze will not load a level until prompted.
	 */
	public Maze() {
	}


	/**
	 * Uses the Persistence module to load a maze from file, then sets the current board to match.
	 * @param levelToLoad Number of the level to load.
	 * @return Time limit in seconds of the level in question. -1 if an error occured while loading.
	 */
	public int loadMaze(int levelToLoad) {
		Level toLoad;
		try {
			toLoad=Persistence.read(levelToLoad);
		} catch(IOException | LevelFileException e) {
			System.out.println("CRITICAL ERROR LOADING LEVEL "+levelToLoad+" :"+e);
			return -1;//If loading the next level went wrong then don't bother doing anything else as it'll result in a crash.
		}
		//Resetting/initializing
		currentTreasureLeft=0;
		currentTreasureCollected=0;
		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		exitList=new ArrayList<>();
		playerInventory=new ArrayList<>();//Reset the player's keys!
		currentLevel=levelToLoad;
		
		//Load board
		board = new Cell[toLoad.getWidth()][toLoad.getHeight()];//Set up the board dimensions
		for(Tile t:toLoad.getGrid()){//For every tile on the map to load
			//board[t.getX()][t.getY()] = new Cell(t.getName(), t.getX(), t.getY());
			String tileName=t.getName();
			switch(tileName) {
			case "free":
				board[t.getX()][t.getY()] = new CellFree(t.getX(),t.getY());
				break;
			case "wall":
				board[t.getX()][t.getY()] = new CellWall(t.getX(),t.getY());
				break;
			case "treasure":
				currentTreasureLeft++;
				board[t.getX()][t.getY()] = new CellTreasure(t.getX(),t.getY());
				break;
			case "key":
				board[t.getX()][t.getY()] = new CellKey(t.getX(),t.getY(), t.getColor());
				break;
			case "door":
				board[t.getX()][t.getY()] = new CellDoor(t.getX(),t.getY(), t.getColor());
				break;
			case "info":
				board[t.getX()][t.getY()] = new CellInfo(t.getX(),t.getY());//TODO: Implement Info.
				break;
			case "exit":
				CellExit exit = new CellExit(t.getX(),t.getY());
				 exitList.add(exit);
				 board[t.getX()][t.getY()] = exit;
				
			case "exit lock":
				 CellExitLocked exitLocked = new CellExitLocked(t.getX(),t.getY());
				 exitList.add(exitLocked);
				 board[t.getX()][t.getY()] = exitLocked;
				break;
			}//End of switch
		}//At this stage, all tiles are loaded
		
		//Load player
		player=new Actor(true, "player", toLoad.getStartX(), toLoad.getStartY(), 6);//Player takes 6 ticks to move.
		
		
		/*
		for(Character c:toLoad.getCharacters()){
				creatures.add(new Actor(false, c.getName(), c.getX(), c.getY()));
		}
		*/

		return toLoad.getTimeLimit();

	}


//
//	public static void saveGameState(ChapsChallenge game, String name) {
//		String jsonGame = getGameState(game);
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
//			writer.write(jsonGame);
//			writer.close();
//		} catch (IOException e) {
//			System.out.println("Error saving game" + e);
//		}
//	}
	
	
	
	/**
	   * Enum of directions to go from a cell to cell. Mainly used by other classes.
	   */
	  public enum Direction {
	    Up, Down, Left, Right;
	  }
	  
	
	public static Cell[][] getBoard(){
		return board;
	}

	public static int getBoardSize() {
	  	return board.length;
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
	
	public int getLevel() {
		return currentLevel;
	}
	
	public ArrayList<String> getPlayerInventory(){
		return playerInventory;
	}
	
	
	/**
	 * One run of the core game "loop", which has the option of taking a movement.
	 * Should be run repeatedly from Application.
	 * @param movementDirection
	 */
	public RenderTuple tick(String movementDirection) {
		//System.out.println("Maze is running Tick with the direction:"+movementDirection);
		if(movementDirection!=null && isMoveValid(player, player.dirFromString(movementDirection)))
		player.move(movementDirection);
		player.tick();
		//TODO: Tick all NPCs once they're implemented.
		//TODO:Run collision detection between player and NPCs, see if an NPC is colliding with the player. If so, game over. NPCs can collide with eachother harmlessly.
		
		//Collision check
		Cell stoodOn=board[player.getX()][player.getY()];
		if(stoodOn.hasPickup()) {//The tile has a pickup. Could be a treasure or a keycard.
			if(stoodOn.isTreasure()) {//If treasure, increment counters, ignore inventory.
				currentTreasureCollected++;
				currentTreasureLeft--;
			} else {//If not treasure, add item to inventory
				playerInventory.add(stoodOn.getColor());//TODO: If items other than keys are added, account for it
			}
			//Nomatter what the pickup was, replace it with an empty tile
			board[player.getX()][player.getY()] = new CellFree(player.getX(), player.getY());
			
			
			
		}
		
		
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
		
		if(toCheck.isOpenable()) {
			for(String s:playerInventory) {//Check every key the player has
				if(s.equals(toCheck.getColor())) {//If the key's the same colour as the door
					board[xToCheck][yToCheck]=new CellFree(xToCheck,yToCheck);//TODO: Once animated door frames are available, make the door open slowly rather than instantly.
					return true;
				}
			}
			return !toCheck.getIsSolid();
			
		} else {
		return !toCheck.getIsSolid();
		}
	}


}
