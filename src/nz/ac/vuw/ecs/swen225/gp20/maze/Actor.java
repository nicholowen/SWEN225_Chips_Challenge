package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;

public class Actor {
	private String name;
	private boolean isPlayer;
	private Direction direction;
	private boolean isMoving;
	private int x;
	private int y;
	private int moveProgress;//Number of ticks the actor's been moving
	private int ticksToMove;//Total number of ticks it takes to move
	private boolean killsPlayer;
	private boolean blocksMovement;
	private boolean isPushable;
	
	/**
	 * This constructor should be used for the player and the player alone - anything else should have a custom class.
	 * @param isPlayer
	 * @param nameReference
	 * @param xpos
	 * @param ypos
	 * @param speed
	 */
	public Actor(boolean isPlayer, String nameReference, int xpos, int ypos, int speed) {
		x=xpos;
		y=ypos;
		this.isPlayer=isPlayer;
		if(isPlayer) {
			this.killsPlayer=false;
		}
		name=nameReference;
		ticksToMove=speed;
		direction=Direction.DOWN;//Generic starting direction
	}
	
	public boolean getIsMoving(){
		return isMoving;
	}

	/**
	 * Advances the actor's movement by one "tick".
	 * If it's completed a movement cycle, then it changes coordinates accordingly.
	 */
	public void tick(){
		if(!isMoving)
			return;
		
		moveProgress++;
		if(moveProgress>=ticksToMove) {
			isMoving=false;
			x=(int) (x+direction.getDirection().getX());
			y=(int) (y+direction.getDirection().getY());
		}

	}
	
	/**
	 * Compares two actors and returns true if they both share the same coordinates (IE, if they collide)
	 * @param o The second actor to compare with this one
	 * @return True if the actors collide, false otherwise
	 */
	public boolean collidesWith(Actor o) {
		return(this.x==o.getX()&&this.y==o.getY());
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	

	/**
	 *
	 */
	public void move(Direction dir){
		if(isMoving)
			return;//If already moving, then ignore the suggestion to move again.
		
		direction=dir;
		isMoving=true;
		moveProgress=0;//Reset move progress.
	}

	public Direction getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public boolean killsPlayer() {
		return killsPlayer;
	}

	public boolean blocksMovement() {
		return blocksMovement;
	}

	public boolean isPushable() {
		return isPushable;
	}
}
