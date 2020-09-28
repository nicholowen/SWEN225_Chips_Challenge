package nz.ac.vuw.ecs.swen225.gp20.maze;

import java.awt.Point;

public class Actor {
	private String name;
	private boolean isPlayer;
	private String direction;
	private boolean isMoving;
	private int x;
	private int y;
	private int moveProgress;//Number of ticks the actor's been moving
	private int ticksToMove;//Total number of ticks it takes to move
	
	
	public Actor(boolean isPlayer, String nameReference, int xpos, int ypos, int speed) {
		x=xpos;
		y=ypos;
		this.isPlayer=isPlayer;
		name=nameReference;
		ticksToMove=speed;
		direction="down";//Generic starting direction
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
			x=(int) (x+dirFromString(direction).getX());
			y=(int) (y+dirFromString(direction).getY());
		}

	}
	
	/**
	 * Returns a Point with the x and y change that an inputed move would have.
	 * null or "none" results in no change, but still returns a point.
	 * @param s
	 * @return
	 */
	public Point dirFromString(String s) {
		switch(s){	
		case "up":
			return new Point(0,-1);
		case "down":
			return new Point(0,1);
		case "left":
			return new Point(-1,0);
		case "right":
			return new Point(1,0);
		default:
		case "none":
			return new Point(0,0);
		}
		
	}
	
	public int getX() {return x;}
	public int getY() {return y;}

	

	/**
	 *
	 */
	public void move(String dir){
		if(isMoving)
			return;//If already moving, then ignore the suggestion to move again.
		
		direction=dir;
		isMoving=true;
		moveProgress=0;//Reset move progress.
	}

	public String getName() {
		return name;
	}
}
