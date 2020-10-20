package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a door object. There are two types of doors, vertical and not vertical.
 * Normal doors are when there are walls next to the door.
 * Vertical doors are when there are walls above and below the door.
 *
 * @author Owen Nicholson 300120635
 */
public class Door extends Sprite {

  // TODO: Make this similar to the other animated objects, including update()
  //  method. Animate door opening or closing depending on when it was opened/closed.

  boolean vertical;
  boolean isSolid;

  public Door(Assets assets, String c, boolean vertical, boolean isSolid) {

    super();

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
    System.out.println(sprites.length);
    animation.setFrames(sprites);
    animation.setSingleAnimation(true);
    animation.setDelay(-1);

  }

  public void update() {

    if (!isSolid) {
      animation.setDelay(1);
      animation.setSingleAnimation(true);
    }
    animation.update();
  }

  public BufferedImage getImage() {
    return animation.getFrame();
  }

  public boolean isVertical() {
    return vertical;
  }

  public void setIsSolid(boolean isSolid){
    this.isSolid = isSolid;
  }


}
