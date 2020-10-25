package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Coordinate;

import java.awt.*;
import java.util.ArrayList;

public class Actor {
	protected String name;
	protected boolean isPlayer;
	protected Direction direction;
	protected boolean isMoving;
	protected int x;
	protected int y;
	protected int moveProgress;//Number of ticks the actor's been moving
	protected int ticksToMove;//Total number of ticks it takes to move
	protected boolean killsPlayer;
	protected boolean blocksMovement;
	protected boolean isPushable;

	//Hostile NPC pathfinding
	private int numberOfSegments;
	private int currentSegment;
	private ArrayList<PatrolSegment> segments;
	private boolean patrolingClockwise;

	//Rendering
	private boolean hasJustMoved;

	//NPC speed
	public static final int ACTOR_HOSTILE_MONSTER_SPEED=16;//Should be 16!
	public static final int PLAYER_SPEED=6;
	
	/**
	 * This constructor should be used for the player and the player alone. Creating more than one player may lead to issues.
	 * @param isPlayer Used to specify this contructor and ensure that an incorrect NPC isn't made into a player.
	 * @param nameReference name reference, if the player's given a unique name.
	 * @param xpos Starting x position
	 * @param ypos Starting y position
	 */
	public Actor(boolean isPlayer, String nameReference, int xpos, int ypos) {
		initDefaultValues();
		x=xpos;
		y=ypos;
		this.isPlayer=isPlayer;//Should never be false!
		name=nameReference;
		ticksToMove=PLAYER_SPEED;
		}

	/**
	 * Would have been the abstract class's "constructor" called with super(), but for some reason they don't load properly.
	 * Instead, bunch the default values into here.
	 */
	private void initDefaultValues(){
		killsPlayer=false;
		isMoving=false;
		hasJustMoved=false;//For rendering - may be better if it's true so it forces them all to be aligned on the first tick?
		direction=Direction.DOWN;//Generic starting direction
		isPushable=false;
		blocksMovement=false;//Creatures should be able to move into the player - players should be able to move into creatures. Both result in death.
	}

	/**
	 * Constructor for dirt, or other pushable entities. They have the same speed as the player.
	 * @param name
	 * @param x
	 * @param y
	 */
	public Actor(String name, int x, int y){
		initDefaultValues();
		isPlayer=false;
		this.x=x;
		this.y=y;
		this.name=name;
		this.ticksToMove=PLAYER_SPEED;
		blocksMovement=true;//By default, until it's pushed over "water", it blocks movement
		isPushable=true;//Can be moved by the player!
	}

	/**
	 * Constructor for a hostile mob - likely the "spider". Includes a list of coordinates which is it's patrol path.
	 * @param name
	 * @param x
	 * @param y
	 * @param path
	 */
	public Actor(String name, int x, int y, ArrayList<Coordinate> path){
		initDefaultValues();
		isPlayer=false;
		this.name=name;
		this.x=x;
		this.y=y;
		ticksToMove=ACTOR_HOSTILE_MONSTER_SPEED;
		killsPlayer=true;

		segments=buildSegments(path);
		patrolingClockwise=true;
		currentSegment=0;
	}

	/**
	 * Used when initializing an actor with a patrol path.
	 * Turns the coordinate points into "patrol segments" which the NPC can use to figure out which was it should move.
	 * @param path
	 * @return The finished arrayList of patrol segments.
	 */
	private ArrayList<PatrolSegment> buildSegments(ArrayList<Coordinate> path){
		ArrayList<PatrolSegment> toReturn = new ArrayList<>();
		for(int i=0; i<path.size()-1; i++){//For every coordinate on the path, except the last one.
			toReturn.add(new PatrolSegment(path.get(i),path.get(i+1)));
		}
		//For the last coordinate of the path, link it to the first!
		toReturn.add(new PatrolSegment(path.get(path.size()-1),path.get(0)));
		numberOfSegments=toReturn.size()-1;
		return toReturn;
	}

	/**
	 * Returns true if the actor is at the end of a given patrol segment, false if not.
	 * @return
	 */
	public boolean isAtEndOfSegment(){
		Point targetPos;
		if(patrolingClockwise)
			targetPos=segments.get(currentSegment).get2ndPoint();
		else
			targetPos=segments.get(currentSegment).get1stPoint();
		return((x==(int)targetPos.getX()) && (y==(int)targetPos.getY()));
	}

	/**
	 * Advances the patrol segment to the next one in line, dependant on whether or not the actor's going clockwise or anticlockwise.
	 * To be used in conjunction with isAtEndOfSegment
	 */
	public void advanceSegment(){
		if(patrolingClockwise){
			currentSegment++;
			if(currentSegment==numberOfSegments)//If we get past the end
				currentSegment=0;//Instead loop around to the start

		} else{//Patrolling anticlockwise, so go the other way!
			currentSegment--;
			if(currentSegment<0)//If we go below 0 (if we were at the "start")
				currentSegment=numberOfSegments-1;//Loop around to the end.

		}
	}

	/**
	 * Flip the movement from clockwise to anticlockwise or vise-versa
	 */
	public void reverseMovementDirection(){
		patrolingClockwise=!patrolingClockwise;
	}

	public Direction getNextMove(){
		return segments.get(currentSegment).getNextMove(patrolingClockwise);
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
	public int getspeed(){return ticksToMove;}

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

	/**
	 * The "fill in" mode, where this actor effectively becomes uninteractable with. Originally this would have been where it turned into a tile.
	 * However, to ensure that the "gaps" or "water" end up rendered properly, this happens instead.
	 * THIS INTERACTS WITH NOTHING OUTSIDE OF THE DIRT BLOCK.
	 * WATER BLOCK MUST BE CHANGED SEPERATELY WITH NULLIFY!
	 * Also make sure to check that the water hasn't already been "nullified"
	 */
	public void fillInMode(){
		if(this.getName().equals("dirt")) {//Safeguard, just in case this is called on something which isn't dirt.
			blocksMovement = false;
			isPushable = false;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public boolean isPlayer(){
		return isPlayer;
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
	public boolean hasJustMoved(){
		return hasJustMoved;
	}
	public void setHasJustMoved(boolean input){
		hasJustMoved=input;
	}

}
