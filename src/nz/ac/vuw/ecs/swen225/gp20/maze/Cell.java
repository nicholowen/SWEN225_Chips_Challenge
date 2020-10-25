package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.*;
import java.util.HashMap;
public class Cell {
	
	
	//Rendering and Animation
	protected String name;
	protected int x;//Assigned when the tile is made, this value is used only for the rendering.
	protected int y;//Assigned when the tile is made, this value is used only for the rendering.

	//Interaction
	protected boolean isSolid;//Checks whether or not the tile can CURRENTLY be passed through
	protected boolean isOpenable;//If true, it's possible for this cell to be opened or unlocked with the right tool!
	protected boolean hasPickup;
	protected boolean isTreasure;
	protected boolean killsPlayer;
	protected String pickupName;
	protected String color;
	protected String infoMessage;
	protected String protectiveItem;//The item which will protect a player from being killed if they stand on this tile. Null if there isn't one.

	//Tick-related functions.
	private int ticksRemaining;
	private boolean isTickable;//Whether or not this particular tile needs to be ticked

	
	/**
	 * Constructor establishes a "basic" cell. It can be walked through, cannot kill the player, etc.
	 * Can be used for: Exit, Exitlock, Free, Treasure, Wall, Water
	 * @param n
	 * @param xpos
	 * @param ypos
	 */

	public Cell(String n, int xpos, int ypos) {
		//Basic values
		name=n;
		x=xpos;
		y=ypos;

		//Default
		isSolid=false;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		killsPlayer=false;
		protectiveItem=null;
		color=null;
		infoMessage=null;
		pickupName=null;

		//Switch for special cases, based on name
		switch(n){
			case "water":
				killsPlayer=true;
				protectiveItem=null;
				break;
			case "wall":
				isSolid=true;
				break;
			case "treasure":
				isTreasure=true;
				hasPickup=true;
				break;
			case "exit lock":
				isSolid=true;
				break;

			default://Default, leave everything as-is. Used for the "free" and "exit" tiles
		}

	}

	/**
	 * Constructor for making a cell with a secondary value, such as a colour of infofield
	 * @param n
	 * @param xpos
	 * @param ypos
	 * @param s
	 */
	public Cell(String n, int xpos, int ypos, String s) {
	//Basic values
		name=n;
		x=xpos;
		y=ypos;

		//Default
		isSolid=false;
		isTreasure=false;
		hasPickup=false;
		isOpenable=false;
		killsPlayer=false;
		protectiveItem=null;
		color=null;
		infoMessage=null;
		pickupName=null;


		switch(n) {
			case "key":
				hasPickup=true;
				pickupName=s+"key";
				color=s;
				break;
			case "door":
				isSolid=true;
				isOpenable=true;
				color=s;
				break;
			case "info":
				infoMessage=s;
				break;
			default:
		}
	}
	
	/**
	 * Returns whether or not the cell is solid. A solid cell cannot be walked into/through.
	 * @return
	 */
	public boolean getIsSolid() {
		return isSolid;
	}

	/**
	 * Deprecated, use getName() instead.
	 * @return
	 */
	public String getRenderData() {
			return(name+":0");
	}

	/**
	 * @return X position
	 */
	public int getX(){
		  return x;
		}

	/**
	 * @return Y position
	 */
	public int getY(){
		  return y;
		}

	/**
	 * Gets the name of the pickup on this tile, if it has one
	 * @return name of the pickup/key
	 */
	public String getPickupName() {
		return pickupName;
	}

	public boolean hasPickup() {
		return hasPickup;
	}
	
	public boolean isOpenable() {
		return isOpenable;
	}

	public boolean isTreasure() {
		return isTreasure;
	}

	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return infoMessage;
	}

	public Point getCoordinate(){return new Point(x,y);}

	public boolean isTickable(){return isTickable;}

	/**
	 * Nullifies the threat. In other words, makes this tile unable to kill the player.
	 * ONLY TO BE USED IN SPECIFIC CIRCUMSTANCES. THIS CHANGE IS PERMANENT TO THE CELL.
	 * Nullifying boots and similar things should work seperately, not using this function.
	 */
	public void nullify(){
		killsPlayer=false;
	}
	
	/**
	 * Checks whether or not a certain tile kills the player based on their inventory.
	 * If they have the correct protective item, it doesn't kill them. Else, it does.
	 * @param inventory The player's current inventory
	 * @return
	 */
	public boolean killsPlayer(HashMap<String, Integer> inventory) {
		if(inventory==null)//If no inventory is sent, this is likely an internal check (IE, noe trying to kill the player)
			return killsPlayer;
		if(protectiveItem!=null) {//If there's a protective item associated with this (meaning it's lethal otherwise)
			return(!inventory.containsKey(protectiveItem));//Return false if the protective item is in the inventory, true if it's not.
		}
		return killsPlayer;
	}

	public void goopify(int timeToStayLethal){
		if(name.equals("free")) {//Only to this to free tiles!
			isTickable = true;
			ticksRemaining = timeToStayLethal;
			killsPlayer = true;
			name="goop";
		}
	}

	public void tick(){
		ticksRemaining--;
		if(ticksRemaining==0){//If it's fully "decayed"
			isTickable=false;
			killsPlayer=false;
			name="free";
		}


	}





}