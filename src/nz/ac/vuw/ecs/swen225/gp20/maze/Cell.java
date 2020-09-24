package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.Sprite;

public class Cell {
	private String name;
	
	//Rendering and Animation
	private Sprite animationObject;
	private int x;//Assigned when the tile is made, this value is used only for the rendering.
	private int y;//Assigned when the tile is made, this value is used only for the rendering.
	
	private int metaData;//Known also as an animation state, this keeps track of which animation frame it's in.
	private boolean animated;//If true, animates through multiple frames. If not, metaData stays at 0.
	private int numberOfFramesTotal; //Total number of animation frames that the tile has. If it has 0, it's just static (like the floor or walls)
	private int timeBetweenFrames;//The number of ticks between frames
	private int counter;//Current "tick" of the cell.
	
	//Interaction
	private boolean isSolid;//Checks whether or not the tile can CURRENTLY be passed through
	private boolean isOpenable;//If true, it's possible for this cell to be opened or unlocked with the right tool!
	
	
	/**
	 * This constructor takes a name and uses a list of preset tiles to set the properties of the tile.
	 * @param n
	 * @param xpos
	 * @param ypos
	 */
	public Cell(String n, int xpos, int ypos) {
		name=n;
		x=xpos;
		y=ypos;
		//TODO: String Switch, apply appropriate properties
		
		switch(n) {
		case "wall":
			isSolid=true;
			break;
		case "free":
			isSolid=false;
			break;
		default:
			System.out.println("Loaded unknown tile type in Cell.java. Loaded type:"+n);//TODO: Replace with proper exception
		
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
	 * Gets the string which signifies the state of the tile - including it's metadata or animation frame.
	 * Also advances the state of the tile by one tick.
	 * @return
	 */
	public String getRenderData() {
		if(animated)
			tick();//Advance the tile's animation one tick if it needs to be animated!
		return(name+":"+metaData);
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
	 * For use by the renderer only!
	 * @return
	 */
	public int getX(){
		  return x;
		}

	/**
	 * For use by the renderer only!
	 * @return
	 */
	public int getY(){
		  return y;
		}
	
	
	
}
