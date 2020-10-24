package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a Key Card.
 * Key can be different colors and will use the correct images from the provided asset.
 *
 * @author Owen Nicholson 300120635
 */
public class Key {

  Animation animation = new Animation();

  public Key(Assets assets, String c) {

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
    // calls on asset class to get the frames for this object.
    animation.setFrames(assets.getAsset("key")[index]);
    animation.setDelay(4);
  }

  /**
   * Updates the frame of the object
   */
  public void update() {
    animation.update();
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
