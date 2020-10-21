package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents an moving object. I.e. player, hostile mob, movable block etc.
 */
public class ActorSprite extends Sprite {

  private BufferedImage[][] sprites;
  private Actor actor;

  public ActorSprite(Actor actor, Assets assets) {

    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.

    this.actor = actor;
    // calls on asset class to get the frames for this object.
    if (actor.getName().equals("player")) {
      sprites = assets.getAsset("player");
    } else if (actor.isPushable()) {
      sprites = assets.getAsset("dirt");
    } else sprites = assets.getAsset("hostileMob");

    animation.setFrames(sprites[0]);
    animation.setDelay(6);

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
   * Updates the frame of the object
   */
  public void update() {

    if (!actor.isPushable()){
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
//    }
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

}
