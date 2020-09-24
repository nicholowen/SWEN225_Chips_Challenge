package nz.ac.vuw.ecs.swen225.gp20.maze;

public class Actor {
	private String name;
	private boolean isPlayer;
	private String direction;
	private boolean isMoving;
	private int moveProgress;//Number of ticks the actor's been moving
	private int ticksToMove;//Total number of ticks it takes to move
	
	
	public Actor(boolean isPlayer, String nameReference) {
		this.isPlayer=isPlayer;
		name=nameReference;
		direction="down";//Generic starting direction
	}
	
	public boolean getIsMoving(){
		return isMoving;
	}

	/**
	 * Advances the actor's movement by one "tick".
	 * @return The direction which the board should move the actor, if it's completed a movement cycle.
	 */
	public String tick(){





	}

	/**
	 *
	 */
	public void move(){

	}



	
}
