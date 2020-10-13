package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.Sprite;

public class Cell {
	
	
	//Rendering and Animation
	protected Sprite animationObject;
	protected String name;
	protected int x;//Assigned when the tile is made, this value is used only for the rendering.
	protected int y;//Assigned when the tile is made, this value is used only for the rendering.
	
	protected int metaData;//Known also as an animation state, this keeps track of which animation frame it's in.
	protected boolean animated;//If true, animates through multiple frames. If not, metaData stays at 0.
	protected int numberOfFramesTotal; //Total number of animation frames that the tile has. If it has 0, it's just static (like the floor or walls)
	protected int timeBetweenFrames;//The number of ticks between frames
	protected int counter;//Current "tick" of the cell.
	
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
	
	
	/**
	 * Constructor is no longer used as this class is a parent class - abstract in all but name.
	 * The only reason this class is not still abstract is because saving/loading can't serialize abstract classes.
	 * A default constructor here WOULD make it easier to work with other classes, but is not used to ensure that nobody accidentally initializes a "blank cell" now that it's not strictly abstract.
	 * @param n
	 * @param xpos
	 * @param ypos
	 */
	/*public Cell(String n, int xpos, int ypos) {		
		name=n;
		x=xpos;
		y=ypos;
		isSolid=false;
		isTreasure=true;
		hasPickup=true;
		isOpenable=false;
		pickupName=pickName;
		}
		
	}*/
	
	/**
	 * Returns whether or not the cell is solid. A solid cell cannot be walked into/through.
	 * @return
	 */
	public boolean getIsSolid() {
		return isSolid;
	}
	
	/**
	 * Gets the string which signifies the state of the tile - including it's metadata or animation frame.
	 * Also advances the state of the tile by one tick.
	 * @return
	 */
	public String getRenderData() {
		if(animated) {
			tick();//Advance the tile's animation one tick if it needs to be animated!
		return(name+":"+metaData);}
		else {
			return(name+":0");
		}
	}
	
	/**
	 * To be called every cycle/tick, ideally by the "GetCellNameToRender"
	 * Only called if the cell is animated!
	 */
	private void tick() {
		counter++;
		if(counter >= timeBetweenFrames) {//If the time's up on the current frame, transition to the next frame
			counter=0;//Reset current counter
			//Increment the frame by one
			if(metaData>=numberOfFramesTotal) {//If the animation is already at the "end" then loop it back around to the start
				metaData=0;
			} else {
				metaData++;//Increase the frame by one otherwise
			}
			
		}
		
		
	}
	
	//Rendering/animating getters and setters
	
	public void setAnimationObject(Sprite animationObject){
		  this.animationObject = animationObject;
		}

		public Sprite getAnimationObject(){
		  return animationObject;
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
	
	/**
	 * Checks whether or not a certain tile kills the player based on their inventory.
	 * If they have the correct protective item, it doesn't kill them. Else, it does.
	 * @param inventory The player's current inventory
	 * @return
	 */
	public boolean killsPlayer(HashMap<String, Integer> inventory) {
		if(protectiveItem!=null) {//If there's a protective item associated with this (meaning it's lethal otherwise)
			return(!inventory.containsKey(protectiveItem));//Return false if the protective item is in the inventory, true if it's not.
		}
		return killsPlayer;
	}
	
}