package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a door object. There are two types of doors, vertical and not vertical.
 * Normal doors are when there are walls next to the door.
 * Vertical doors are when there are walls above and below the door.
 *
 * @author Owen Nicholson 300120635
 */
public class Door {

  Animation animation = new Animation();
  boolean vertical;
  boolean isSolid;

  public Door(Assets assets, String c, boolean vertical, boolean isSolid) {

    this.isSolid = isSolid;

    int index = 0;
    switch (c) {
      case "red":
        index = 0;
        break;
      case "green":
        index = 1;
        break;
      case "blue":
        index = 2;
        break;
      case "yellow":
        index = 3;
        break;
      default:
        break;

    }
    this.vertical = vertical;
    BufferedImage[] sprites;
    if (!vertical) {
      sprites = assets.getAsset("door")[index];
    } else {
      sprites = assets.getAsset("vdoor")[index];
    }
    animation.setFrames(sprites);
    animation.setSingleAnimation(true);
    animation.setDelay(-1);

  }

  /**
   * Updates the frame of the current animation.
   * If it is closed it won't animate. If open, will only animate once.
   */
  public void update() {
    if (!isSolid) {
      animation.setDelay(1);
      animation.setSingleAnimation(true);
    }
    animation.update();
  }

  /**
   * Gets the current frame of animation.
   * @return BufferedImage of the current frame.
   */
  public BufferedImage getImage() {
    return animation.getFrame();
  }

  /**
   * Checks to see if it is a vertically aligned door.
   * @return True if the door has a wall above or below it.
   */
  public boolean isVertical() {
    return vertical;
  }

  /**
   * Updates the sprite to it's new status.
   * @param isSolid True if the door is solid (un-walkable)
   */
  public void setIsSolid(boolean isSolid){
    this.isSolid = isSolid;
  }


}
