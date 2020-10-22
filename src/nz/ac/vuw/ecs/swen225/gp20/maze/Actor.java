package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Coordinate;

import java.awt.Point;
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

	public static final int ACTOR_HOSTILE_MONSTER_SPEED=15;
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
