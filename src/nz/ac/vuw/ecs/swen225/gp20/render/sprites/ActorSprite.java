package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.RenderTuple;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents an moving object. I.e. player, hostile mob, movable block etc.
 */
public class ActorSprite extends Sprite {

  private BufferedImage[][] sprites;
  private Actor actor;

  private int offsetX;
  private int offsetY;
  private boolean isPushable;
  private Assets assets;
  private RenderTuple tuple;

  int speed = 0; //offset which draws the character a number of pixels in the direction it is facing/travelling

  public ActorSprite(Actor actor, Assets assets) {
    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.
    this.isPushable = actor.isPushable();
    this.actor = actor;
    this.assets = assets;
    // calls on asset class to get the frames for this object.
    if (actor.getName().equals("player")) {
      sprites = assets.getAsset("player");
      animation.setDelay(6);
      speed = 12;
    } else if (actor.getName().equals("dirt")){
      sprites = assets.getAsset("dirt");
      animation.setDelay(6);
      speed = 12;
    } else {
      sprites = assets.getAsset("hostileMob");
      animation.setDelay(2);
      speed = 4;
    }

    animation.setFrames(sprites[0]);
  }

  /**
   * Gets X coordinate of actor
   *
   * @return int - coordinate on map
   */
  public int getX() {
    return actor.getX();
  }

  /**
   * Gets Y coordinate of actor
   *
   * @return int - coordinate on map
   */
  public int getY() {
    return actor.getY();
  }

  /**
   * *Dirt Only*
   * changes the asset sheet from the 'inactive' hover tile to the active (and animated) hover tile.
   */
  public void checkStateChange(){
    if(isPushable && !actor.isPushable()){
      isPushable = false;
      sprites = assets.getAsset("dirtInactive");
      animation.setFrames(assets.getAsset("dirtInactive")[0]);
    }
  }

  /**
   * Gets if the actor is moving or not.
   * @return True if moving.
   */
  public boolean getIsMoving() {
    return actor.getIsMoving();
  }

  /**
   * Gets the direction of the actor.
   * @return Enum Direction.
   */
  public Direction getDirection() {
    return actor.getDirection();
  }

  /**
   * Sets the tuple for this object - sole purpose to retrieve
   * @param tuple
   */
  public void setTuple(RenderTuple tuple){
    this.tuple = tuple;
  }

  /**
   * retrieves the image for the current frame it's in
   *
   * @return Current animation frame
   */
  public BufferedImage getImage() {
    return animation.getFrame();
  }

  /**
   * Updates the frame of the object based on direction (Sprite asset has different direction animations on different rows)
   */
  public void update() {
    checkStateChange();
    if (!actor.getName().equals("dirt")){
      switch (actor.getDirection()) {
        case UP:
          animation.setNewFrames(sprites[0]);
          break;
        case DOWN:
          animation.setNewFrames(sprites[1]);
          break;
        case LEFT:
          animation.setNewFrames(sprites[2]);
          break;
        case RIGHT:
          animation.setNewFrames(sprites[3]);
          break;
        default:
          //should never reach here.
          break;
      }
    }
    animation.update();
  }

  /**
   * Draws the character on the main draw object. Offsets the character based on
   * if it is moving or not.
   * @param g Graphics object.
   * @param x X value of the draw position
   * @param y Y value of the draw position
   */
  public void draw(Graphics g, int x, int y){
    //resets the offset when the character has started it's move cycle.
    if(tuple != null && tuple.creatureMoved()){
      offsetX = 0;
      offsetY = 0;
    }
    if(getIsMoving()) {
      switch (getDirection()) {
        case UP:
          offsetY -= speed;
          break;
        case DOWN:
          offsetY += speed;
          break;
        case LEFT:
          offsetX -= speed;
          break;
        case RIGHT:
          offsetX += speed;
          break;
        default:
          break;
      }
    }
    else{
        offsetX = 0;
        offsetY = 0;

    }
    if(!actor.getName().equals("player")) {
      g.drawImage(getImage(), x + offsetX, y + offsetY, null);
    }else{
      g.drawImage(getImage(), x, y, null);
    }
  }
}
