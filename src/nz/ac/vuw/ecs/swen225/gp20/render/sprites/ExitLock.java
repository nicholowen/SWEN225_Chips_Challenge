package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the exit lock of the level.
 *
 * @author Owen Nicholson 300120635
 */
public class ExitLock {

  Animation animation = new Animation();
  BufferedImage[] sprites;

  public ExitLock(Assets assets) {

    // calls on asset class to get the frames for this object.
    sprites = assets.getAsset("exitLock")[0];
    animation.setFrames(sprites);
    animation.setDelay(2);
  }

  /**
   * Updates the frame of the object by progressing the counter in its animation object.
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