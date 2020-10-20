package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp20.maze.actors.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.cells.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.*;

public class Maze {
	private Cell[][] board;
	private int timeRemaining;
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
	public Maze(int level) {
		loadMaze(level);
	}

	/**
	 * Uses the Persistence module to load a maze from file, then sets the current board to match.
	 * Completely wipes any data when used, re-initializing the player inventory, treasure count, everything is reset.
	 * @param levelToLoad Number of the level to load.
	 * @return Time limit in seconds of the level in question. -1 if an error occurred while loading.
	 */
	private void loadMaze(int levelToLoad) {
		System.out.println("Loadmaze was called, trying to load maze:"+levelToLoad);
		Level toLoad;
		try {
			toLoad=Persistence.read(levelToLoad);
		} catch(IOException e) {
			System.out.println("CRITICAL ERROR LOADING LEVEL "+levelToLoad+" :"+e);
			return;//If loading the next level went wrong then don't bother doing anything else as it'll result in a crash.
		}
		//Resetting/initializing
		System.out.println("Managed to read the file successfully!");
		gameOver=false;
		currentTreasureLeft=toLoad.properties.chipsInLevel;
		currentTreasureCollected=0;
		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		exitList=new ArrayList<>();
		playerInventory=new HashMap<>();//Reset the player's inventory
		currentLevel=levelToLoad;
		timeRemaining = toLoad.properties.timeLimit;
		oomphCounter=0;
		System.out.println("Loaded arbitrary values");
		
		//Load board
		board = toLoad.getBoard();
		System.out.println("Loaded board");

		//Load player
		player=new Actor(true, "player", toLoad.getStartX(), toLoad.getStartY(), 6);//Player takes 6 ticks to move.
		System.out.println("Loaded player");

		//Load NPCs
		for(NonPlayableCharacter c:toLoad.getNonPlayableCharacters()){
				creatures.add(new ActorHostileMonster(c.getType(), c.x, c.y, c.getPath()));
		}
		System.out.println("Loaded NPCs!");
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
	public RenderTuple tick(Direction movementDirection) {
		boolean shouldAdvanceLevel=false;//Change to true if player completes the level this tick.
		if(oomphCounter>0)//Sound-related. Keeps track of the delay between certain sounds so that they don't stack.
			oomphCounter--;
		String soundEvent=null;//For the RenderTuple. Keeps track of whether or not a sound should be played on a given tick. A sound may be "over-written" in theory, but this should never happen in practice.
		//System.out.println("Maze is running Tick with the direction:"+movementDirection);
		boolean isWalkingIntoDoor=false;
		if(movementDirection!=null) {
		isWalkingIntoDoor = (getCellFromDir(player,movementDirection.getDirection()) instanceof CellDoor)
				|| (getCellFromDir(player,movementDirection.getDirection()) instanceof CellExitLocked);//True if the player's about to walk into a door or exitLock
		} 
		
		if(movementDirection!=null && isMoveValid(player, movementDirection.getDirection())) {//If there's movement input and it's valid, move.
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

		for(Actor npc:creatures){//For every NPC (All actors except the player)
			if(npc instanceof ActorNeutralDirt){
				ActorNeutralDirt castedToDirt = (ActorNeutralDirt) npc;
				if(castedToDirt.isPushable()){//If currently not "settled" or "filled in"
					Cell cellOver=board[castedToDirt.getX()][castedToDirt.getY()];
					if(cellOver instanceof CellWater){//If it's hovering over water at present
						if(cellOver.killsPlayer(null)){//And finally, if the water is in "lethal mode"
							cellOver.nullify();//Make the water not lethal
							castedToDirt.fillInMode();//Make the dirt block unmoveable. This has now successfully made a bridge!
						}
					}
				}
			}//End of dirt filling water logic

			if(npc instanceof ActorHostileMonster){//If it's a "spider"




			}
		}

		//TODO:Run collision detection between player and NPCs, see if an NPC is colliding with the player. If so, game over. NPCs can collide with eachother harmlessly.
		
		//Collision check - see what's under the player's feet.
		Cell stoodOn=board[player.getX()][player.getY()];
		if(stoodOn.killsPlayer(playerInventory)) {
			soundEvent="dyingnoises";
			gameOver=true;
		} else if(stoodOn instanceof CellExit){//Win! Kind of.
			shouldAdvanceLevel=true;
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
		if(shouldAdvanceLevel){//If the player should advance the level, IE, if they "win"
			soundEvent="awinrarisyou";//Sound signifying success
			//TODO: Indicate to Application/Main to load the next level!
		}

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
		if(toCheck.isOpenable()&&toCheck.isSolid) {//If the checked tile is a door (And solid - meaning it's closed)
			String keycardName = (toCheck.getColor()+"key");
			if(playerInventory.containsKey(keycardName)) {//If true, then it means there's at least one matching keycard
				removeFromInventory(keycardName);
				//board[xToCheck][yToCheck]=new CellFree(xToCheck,yToCheck);//TODO: Once animated door frames are available, make the door open slowly rather than instantly.
				toCheck.isSolid=false;//Make it so that the door's no longer solid, meaning that it acts as if it's "open" but can be walked through.
				return true;//Immediately walk onto the space where the door used to be. Will be FALSE if doors have animations which force the player to wait.
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

	public void tickTimeRemaining() {
		timeRemaining--;
	}

	public int getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
	}


}