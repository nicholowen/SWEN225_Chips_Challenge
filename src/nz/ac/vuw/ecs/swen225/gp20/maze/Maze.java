package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp20.maze.cells.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Level;
import nz.ac.vuw.ecs.swen225.gp20.persistence.keys.Tile;

public class Maze {
	private static Cell[][] board;
	private boolean gameOver;//If true, the player is dead or the level is otherwise failed.
	private int currentLevel;//Iterate every time a level is complete
	private int currentTreasureLeft;
	private int currentTreasureCollected;
	//private ArrayList<String> playerInventory;
	private HashMap<String, Integer> playerInventory;
	
	//Board logic
	private int boardHeight;
	private int boardWidth;
	private ArrayList<Cell> exitList;
	
	//Actor logic
	private Actor player;//Player character, there is only one.
	private ArrayList<Actor> creatures;//Hostile NPCs
	
	//Sound logic
	private int oomphCounter;//Forces a small delay between "oomph" sounds to stop them from stacking up in a deafening way.
	private final int oomphDelay=10;//Time in ticks between each "oomph" sound if the player repeatedly tries to cause it.
	
	
	/**
	 * This module handles the maze, collision, actors and inventory.
	 * Loads maze "1" by default. Completely resets every time a maze is loaded.
	 * @author Lex Ashurst 300431928
	 */
	public Maze() {
		loadMaze(1);
	}

	/**
	 * Uses the Persistence module to load a maze from file, then sets the current board to match.
	 * Completely wipes any data when used, re-initializing the player inventory, treasure count, everything is reset.
	 * @param levelToLoad Number of the level to load.
	 * @return Time limit in seconds of the level in question. -1 if an error occurred while loading.
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
		gameOver=false;
		currentTreasureLeft=0;
		currentTreasureCollected=0;
		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		exitList=new ArrayList<>();
		playerInventory=new HashMap<>();//Reset the player's inventory
		currentLevel=levelToLoad;
		oomphCounter=0;
		//helpMessage=toLoad.getHelp(); //TODO: Have getHelp implemented in Persistence/Level
		
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
				board[t.getX()][t.getY()] = new CellInfo(t.getX(),t.getY(), t.getHelp());
				break;
			case "exit":
				CellExit exit = new CellExit(t.getX(),t.getY());
				 exitList.add(exit);
				 board[t.getX()][t.getY()] = exit;
				 break;
			case "exit lock":
				 CellExitLocked exitLocked = new CellExitLocked(t.getX(),t.getY());
				 exitList.add(exitLocked);
				 board[t.getX()][t.getY()] = exitLocked;
				break;
			case "water":
				board[t.getX()][t.getY()] = new CellWater(t.getX(),t.getY());
				break; 
			}//End of switch
		}//At this stage, all tiles are loaded
		
		//Load player
		player=new Actor(true, "player", toLoad.getStartX(), toLoad.getStartY(), 6);//Player takes 6 ticks to move.
		
		
		/*//Load NPCs
		for(Character c:toLoad.getCharacters()){
				creatures.add(new Actor(false, c.getName(), c.getX(), c.getY()));
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

	public  int getBoardSize() {
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
	
	public HashMap<String, Integer> getPlayerInventory() {
		return playerInventory;
	}
	
	
	/**
	 * One run of the core game "loop", which has the option of taking a movement.
	 * Should be run repeatedly from Application.
	 * @param movementDirection Input from the application indicating the direction of movement.
	 * @return RenderTuple A bundle of information to be passed to the renderer
	 */
	public RenderTuple tick(String movementDirection) {
		if(oomphCounter>0)//Sound-related. Keeps track of the delay between certain sounds so that they don't stack.
			oomphCounter--;
		String soundEvent=null;//For the RenderTuple. Keeps track of whether or not a sound should be played on a given tick. A sound may be "over-written" in theory, but this should never happen in practice.
		//System.out.println("Maze is running Tick with the direction:"+movementDirection);
		boolean isWalkingIntoDoor=false;
		if(movementDirection!=null) {
		isWalkingIntoDoor = (getCellFromDir(player,player.dirFromString(movementDirection)) instanceof CellDoor) 
				|| (getCellFromDir(player,player.dirFromString(movementDirection)) instanceof CellExitLocked);//True if the player's about to walk into a door or exitLock
		} 
		
		if(movementDirection!=null && isMoveValid(player, player.dirFromString(movementDirection))) {//If there's movement input and it's valid, move.	
			if(!player.getIsMoving()) {//Checks that the player isn't already moving when updating the sound so that it doesn't stack.
				if(isWalkingIntoDoor) 
					soundEvent="unlock";
				else
					soundEvent="move";
				}
			player.move(movementDirection);
		} else if(movementDirection!=null && oomphCounter==0){//If there's movement input and it's invalid, play a sound to signify this. Oomphcounter is to stop the sounds from stacking
			if(isWalkingIntoDoor) 
				soundEvent="whawhaa";
			else
				soundEvent="oomph";
			oomphCounter=oomphDelay;
		}
		player.tick();
		//TODO: Tick all NPCs once they're implemented.
		//TODO:Run collision detection between player and NPCs, see if an NPC is colliding with the player. If so, game over. NPCs can collide with eachother harmlessly.
		
		//Collision check - see what's under the player's feet.
		Cell stoodOn=board[player.getX()][player.getY()];
		if(stoodOn.killsPlayer(playerInventory)) {
			soundEvent="dyingnoises";
			gameOver=true;
		}
		if(stoodOn.hasPickup()) {//The tile has a pickup. Could be a treasure or a keycard.
			if(stoodOn.isTreasure()) {//If treasure, increment counters, ignore inventory.
				currentTreasureCollected++;
				currentTreasureLeft--;
				soundEvent="pickupshiny";
			} else {//If not treasure, add item to inventory
				addToInventory(stoodOn.getPickupName());//TODO: If items other than keys are added, account for it
				soundEvent="pickup";
			}
			//Nomatter what the pickup was, replace it with an empty tile
			board[player.getX()][player.getY()] = new CellFree(player.getX(), player.getY());
		}
		boolean playerStandingOnInfo = (stoodOn instanceof CellInfo);//Check if the player's standing on an info tile
		//if(soundEvent!=null)
		//System.out.println("DEBUG: Play sound:"+soundEvent);
		return new RenderTuple(getActors(), getBoard(), getPlayerInventory(), playerStandingOnInfo, stoodOn.getInfo(), currentTreasureCollected ,currentTreasureLeft, soundEvent, gameOver);
	}
	
	/**
	 * Simple way of adding to a character's inventory.
	 * If the item is present then increment it's count by 1, else add it to the inventory.
	 * @param s Name of the item being added to the inventory
	 */
	public void addToInventory(String s) {
		if(playerInventory.containsKey(s)) {//If the key is already on the hash map
			Integer i = playerInventory.get(s);
			i++;
			playerInventory.put(s, i);
		} else {//Else, the key's not on the hashmap, so add it
			playerInventory.put(s, 1);
		}
	}
	
	/**
	 * "Uses" a single item from the inventory. Removes it from the inventory(Keyset) if it was the last instance of that item.
	 * @param s
	 */
	public void removeFromInventory(String s) {
		Integer i = playerInventory.get(s);
		i--;
		if(i==0)
			playerInventory.remove(s);
		else{
			playerInventory.put(s, i);
		}
	}
	
	/**
	 * From a given actor and a given direction (as a point) return a cell, or null if there is no cell.
	 * @param a The actor whose coordinates are the starting point
	 * @param p A Point representing the change in x/y coordinates from the actor's current position
	 * @return
	 */
	public Cell getCellFromDir(Actor a, Point p) {
		int xToCheck=(int) (a.getX()+p.getX());
		int yToCheck=(int) (a.getY()+p.getY());
		
		if(xToCheck<0||xToCheck>=board.length||yToCheck<0||yToCheck>=board[0].length)
			return null;//Out-of-bounds, return null to avoid crashing
		
		return(board[xToCheck][yToCheck]);
	}
	
	/**
	 * Given a character and a direction, check if it's a valid or invalid move.
	 * Also unlocks doors if a character walks over/against it.
	 * @param a The actor who is attempting to make a move
	 * @param p A Point representing the change in x/y coordinates from the actor's current position
	 * @return
	 */
	public boolean isMoveValid(Actor a, Point p) {
		int xToCheck=(int) (a.getX()+p.getX());
		int yToCheck=(int) (a.getY()+p.getY());
		
		if(xToCheck<0||xToCheck>=board.length||yToCheck<0||yToCheck>=board[0].length)
			return false;//Out-of-bounds, cannot walk through.
		
		Cell toCheck=board[xToCheck][yToCheck];
		if(toCheck.isOpenable()) {//If the checked tile is a door
			String keycardName = (toCheck.getColor()+"key");
			if(playerInventory.containsKey(keycardName)) {//If true, then it means there's at least one matching keycard
				removeFromInventory(keycardName);
				board[xToCheck][yToCheck]=new CellFree(xToCheck,yToCheck);//TODO: Once animated door frames are available, make the door open slowly rather than instantly.
				return true;//Immediately walk onto the space where the door used to be. Will be FALSE once doors are animated.
			}
			return false;//If there was no matching keycard, return false.
			
		} else if(toCheck instanceof CellExitLocked){//If it's an exit door/lock, see if the player has enough chips.
			if(currentTreasureLeft==0) {//If all treasure's been collected
				board[xToCheck][yToCheck]=new CellFree(xToCheck,yToCheck);
				return true;//Immediately walk onto the space where the lock used to be!
				
			} else {//Not all treasure's been collected, so it's solid.
				return false;
			}
			
			
		} else{//Not a door
		
		return !toCheck.getIsSolid();
		}
	}


}