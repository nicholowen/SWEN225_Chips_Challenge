package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
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

  public int x;
  public int y;

  public ActorSprite(Actor actor, Assets assets) {

    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.
    this.isPushable = actor.isPushable();
    this.actor = actor;
    this.assets = assets;
    // calls on asset class to get the frames for this object.
    if (actor.getName().equals("player")) {
      sprites = assets.getAsset("player");
    } else if (actor.getName().equals("dirt")){
      if (actor.isPushable()) {
        sprites = assets.getAsset("dirt");
      }else{
        sprites = assets.getAsset("dirtInactive");
      }
    } else sprites = assets.getAsset("hostileMob");

    animation.setFrames(sprites[0]);
    if (actor.getName().equals("dirt")) {
      animation.setDelay(6);
    } else {
      animation.setDelay(2);
    }

  }

  public void setVisiblePos(int x, int y){
    this.x = x;
    this.y = y;
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


  public void checkStateChange(){
    if(isPushable && !actor.isPushable()){
      isPushable = false;
      sprites = assets.getAsset("dirtInactive");
    }
  }

  /**
   * Updates the frame of the object
   */
  public void update() {
    checkStateChange();
    if (!actor.getName().equals("dirt") && !actor.isPushable()){
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
          break;
      }
    }
    animation.update();
  }


  public boolean getIsMoving() {
    return actor.getIsMoving();
  }

  public Direction getDirection() {
    return actor.getDirection();
  }

  /**
   * retrieves the image for the current frame it's in
   *
   * @return Current animation frame
   */
  public BufferedImage getImage() {
    return animation.getFrame();
  }

  public void setOffsetX(int x){
    this.offsetX = x;
  }

  public void setOffsetY(int y){
    this.offsetY = y;
  }

  public int getOffsetX(){
    return offsetX;
  }
  public int getOffsetY(){
    return offsetY;
  }

  public void draw(Graphics g, int x, int y){
    int speed;
    if(getIsMoving()) {
      if(actor.isPushable()) speed = 12;
      else speed = 5;
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

    }else{
      offsetX = offsetY = 0;
    }
    g.drawImage(getImage(), x + offsetX, y + offsetY, null);
  }
}
