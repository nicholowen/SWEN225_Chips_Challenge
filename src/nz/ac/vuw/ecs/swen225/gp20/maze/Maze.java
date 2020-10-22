package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp20.persistence.*;
import nz.ac.vuw.ecs.swen225.gp20.persistence.level.*;

public class Maze {
	private Cell[][] board;
	private int timeRemaining;
	private boolean gameLost;//If true, the player is dead or the level is otherwise failed.
	private boolean gameWon;//If true, the player's succeeded and completed the level.
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

	//Recording
	private Direction recordedMove;
	
	
	/**
	 * This module handles the maze, collision, actors and inventory.
	 * Loads a maze of the given number. Completely resets every time a maze is loaded.
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
		gameLost=false;
		gameWon=false;
		currentTreasureLeft=toLoad.properties.chipsInLevel;
		currentTreasureCollected=0;
		creatures=new ArrayList<>();//Init arraylist that NPCs will be put on
		exitList=new ArrayList<>();
		playerInventory=new HashMap<>();//Reset the player's inventory
		currentLevel=levelToLoad;
		timeRemaining = toLoad.properties.timeLimit;
		oomphCounter=0;
		recordedMove=null;
		System.out.println("Loaded arbitrary values");
		
		//Load board
		board = toLoad.getBoard();
		System.out.println("Loaded board");

		//Load player
		player=new Actor(true, "player", toLoad.getStartX(), toLoad.getStartY());
		System.out.println("Loaded player");

		//Load NPCs
		for(NonPlayableCharacter c:toLoad.getNonPlayableCharacters()){
			if(c.getType().equals("spider")){
				System.out.println("[DEBUG] loaded creature of type spider");
				creatures.add(new Actor(c.getType(), c.x, c.y, c.getPath()));

			} else{//For now, if it's not a spider, then it's a dirt block
				System.out.println("[DEBUG] loaded creature which wasn't a spider but instead was:"+c.getType());
				creatures.add(new Actor(c.getType(), c.x, c.y));
			}
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

	public Actor getPlayer(){
		return player;
	}

	public boolean getGameWon(){return gameWon;}

	public boolean getGameLost(){return gameLost;}
	
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
		recordedMove=null;//For Mel's original method of recording - scrap?
		Direction playerActuallyMoved=null;
		boolean creatureMoved=false;

		//Sound-and-movement related code
		if(oomphCounter>0)//Sound-related. Keeps track of the delay between certain sounds so that they don't stack.
			oomphCounter--;
		String soundEvent=null;//For the RenderTuple. Keeps track of whether or not a sound should be played on a given tick. A sound may be "over-written" in theory, but this should never happen in practice.
		//System.out.println("Maze is running Tick with the direction:"+movementDirection);
		boolean isWalkingIntoDoor=false;
		boolean isWalkingIntoExitDoor=false;
		boolean isCellSolid=false;
		if(movementDirection!=null && getCellFromDir(player,movementDirection.getDirection())!=null) {
			isWalkingIntoDoor = (getCellFromDir(player,movementDirection.getDirection()).getName().equals("door"));
			isWalkingIntoExitDoor= (getCellFromDir(player,movementDirection.getDirection()).getName().equals("exit lock"));//True if the player's about to walk into a door or exitLock
			isCellSolid = getCellFromDir(player,movementDirection.getDirection()).getIsSolid();
		}
		
		if(!player.getIsMoving() && movementDirection!=null && isMoveValid(player, movementDirection.getDirection()) && playerCanPushOnto(getCellFromDir(player,movementDirection.getDirection()), movementDirection)) {//If there's movement input and it's valid, move. Also unlocks doors and pushes dirt.
			//Checks that the player isn't already moving when updating the sound so that it doesn't stack.
				if((isWalkingIntoExitDoor || isWalkingIntoDoor) && isCellSolid){
					if(isWalkingIntoDoor)
						soundEvent="unlock";
					else
						soundEvent="exitlock";
				}
				else
					soundEvent="move";

			player.move(movementDirection);
			recordedMove=movementDirection;
			playerActuallyMoved=movementDirection;
		} else if(!player.getIsMoving() && movementDirection!=null && oomphCounter==0){//If there's movement input and it's invalid, play a sound to signify this. Oomphcounter is to stop the sounds from stacking
			if(isWalkingIntoDoor) 
				soundEvent="whawhaa";
			else
				soundEvent="oomph";
			oomphCounter=oomphDelay;
		}
		player.tick();

		for(Actor npc:creatures){//For every NPC (All actors except the player)
			npc.tick();//If necessary, tick them forward.
			if(npc.collidesWith(player)){
				soundEvent="death_sound";
				gameLost=true;//If they collide with the player and they'd kill the player, then kill the player.
			}


			if(npc.getName().equals("dirt")){
				if(npc.isPushable()){//If currently not "settled" or "filled in"
					Cell cellOver=board[npc.getX()][npc.getY()];
					if(cellOver.getName().equals("water")){//If it's hovering over water at present
						if(cellOver.killsPlayer(null)){//And finally, if the water is in "lethal mode"
							cellOver.nullify();//Make the water not lethal
							npc.fillInMode();//Make the dirt block unmoveable. This has now successfully made a bridge!
						}
					}
				}
			}//End of dirt filling water logic

			if(npc.getName().equals("spider")){//If it's a "spider"
				//Low complexity: simple, just patrol north-to-south and turn around if you hit something.
				if(!npc.getIsMoving()){//If not moving,
					creatureMoved=true;
					if(isMoveValid(npc, npc.getDirection().getDirection()) && !NPCBlocksPath(getCoordinateFromDir(npc, npc.getDirection().getDirection()))){//If the move's valid (checks terrain AND NPCs)
						npc.move(npc.getDirection());//Keep moving if it's clear!
					} else{//If it's not clear, turn the other way!
						npc.move(reverseDir(npc.getDirection()));
					}
				}
				//End of complexity 1. Passable, will improve upon.
				//TODO: Implement high-complexity movement with movement paths!
			}
		}


		//Collision check - see what's under the player's feet.
		Cell stoodOn=board[player.getX()][player.getY()];
		if(stoodOn.killsPlayer(playerInventory)) {
			soundEvent="death_sound";
			gameLost=true;
		} else if(stoodOn.getName().equals("exit")){//Win! Kind of.
			gameWon=true;
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
			board[player.getX()][player.getY()] = new Cell("free", player.getX(), player.getY());
		}
		boolean playerStandingOnInfo = (stoodOn.getName().equals("info"));//Check if the player's standing on an info tile
		if(gameWon && oomphCounter==0){//If the player should advance the level, IE, if they "win"
			soundEvent="awinrarisyou";//Sound signifying success
			//Small check to ensure that the sound doesn't repeat at unpleasant speed
			oomphCounter=-1;//Sound only plays once, then level changes. This ensures the sound won't play more than once.
		}

		return new RenderTuple(getActors(), getBoard(), getPlayerInventory(), playerStandingOnInfo, stoodOn.getInfo(), currentTreasureCollected ,currentTreasureLeft, soundEvent, playerActuallyMoved,creatureMoved);
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
	 * From a given actor and a given direction (as a point) return a poitn signifying the coordinates of the new cell
	 * @param a The actor whose coordinates are the starting point
	 * @param p A Point representing the change in x/y coordinates from the actor's current position
	 * @return
	 */
	public Point getCoordinateFromDir(Actor a, Point p){
		Cell newCell=getCellFromDir(a,p);
		return new Point(newCell.getX(),newCell.getY());
	}

	public Direction reverseDir(Direction d){
		switch(d){
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
		}
		return null;//Should never get here!
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

		if(!a.isPlayer()){//If not the player, then they can't open or unlock anything, so just return the solidness. Else, attempt to unlock/open
			return !toCheck.getIsSolid();
		}

		if(toCheck.isOpenable()&&toCheck.isSolid) {//If the checked tile is a door (And solid - meaning it's closed)
			String keycardName = (toCheck.getColor()+"key");
			if(playerInventory.containsKey(keycardName)) {//If true, then it means there's at least one matching keycard
				removeFromInventory(keycardName);
				//board[xToCheck][yToCheck]=new CellFree(xToCheck,yToCheck);//TODO: Once animated door frames are available, make the door open slowly rather than instantly.
				toCheck.isSolid=false;//Make it so that the door's no longer solid, meaning that it acts as if it's "open" but can be walked through.
				return true;//Immediately walk onto the space where the door used to be. Will be FALSE if doors have animations which force the player to wait.
			}
			return false;//If there was no matching keycard, return false.
			
		} else if(toCheck.getName().equals("exit lock")){//If it's an exit door/lock, see if the player has enough chips.
			if(currentTreasureLeft==0) {//If all treasure's been collected
				board[xToCheck][yToCheck]=new Cell("free",xToCheck,yToCheck);
				return true;//Immediately walk onto the space where the lock used to be!
			} else {//Not all treasure's been collected, so it's solid.
				return false;
			}
			
			
		} else{//Not a door
		
		return !toCheck.getIsSolid();
		}
	}

	/**
	 * Checks through the list of all NPCs to see if there's an NPC infront of the player which might block their movement.
	 * Then checks if that NPC can be pushed by the player.
	 * If it can be pushed by the player, then they push it. If it blocks them, then player doesn't move. If for whatever the reason it doesn't block them, carry on.
	 * NOTE! This needs to be checked repeatedly. Some NPCs go into "background mode", which is to say, the "sunk dirt". Those need to be ignored by this.
	 * @param c The cell that the player's attempting to move to
	 * @param d The direction of the movement!
	 * @return
	 */
	public boolean playerCanPushOnto(Cell c, Direction d){
		if(NPCBlocksPath(new Point(c.getX(),c.getY()))){//If anything's in the way, then
			for(Actor a:creatures) {//For every NPC - we need to check all of them after all.
				if (a.getX() == c.getX() && a.getY() == c.getY()){//If it's on the tile we're looking at
					System.out.println("[DEBUG]Found an NPC blocking our path on the tile x:"+a.getX()+", y:"+a.getY()+", name:"+a.getName());
					if (a.blocksMovement() && !a.isPushable)
						return false;//If we can't get past it, immediately return false.
					else if (a.isPushable()) {
						System.out.println("[DEBUG]We know that the NPC is pushable! Checking if the space behind it is clear");
						Cell farSide = getCellFromDir(a, d.getDirection());//Cell on the far side of the one we're pushing.
						if(farSide.isSolid || NPCBlocksPath(new Point(farSide.getX(), farSide.getY()))) {
							System.out.println("[DEBUG]We were unable to push the NPC!");
							return false;//If there's no empty space on the far side, then don't push it.
						}
						//Else, we move what we're pushing!
						System.out.println("[DEBUG]Pushing NPC!");
						a.move(d);
						return true;
					}
				}
			}

		}
		return true;
	}

	/**
	 * Checks if an NPC which can't be immediately walked on is at a particular point.
	 * This doesn't check anything as to whether or not it's pushable.
	 * Returns true if so, false otherwise. Not to be used directly by the player!
	 * @param p
	 * @return
	 */
	public boolean NPCBlocksPath(Point p){
		for(Actor a:creatures){
			if( (a.getX()==p.getX()) && (a.getY()==p.getY()) && a.blocksMovement())
				return true;
		}
		return false;//Only return false if absoloutely nothing could block the movement here.
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

	/**
	 * Returns the direction the player successfully started to move in, or null if they didn't.
	 * @return
	 */
	public Direction getPlayerMovementForRecording(){
		return recordedMove;
	}

}