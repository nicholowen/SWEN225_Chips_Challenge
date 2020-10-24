package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the exit portal. This Class contains the animation frames of the object,
 * and the delay - the time it takes (number of ticks) for the next frame to be fetched.
 *
 * @author Owen Nicholson 300120635
 */
public class Exit {

  Animation animation = new Animation();
  BufferedImage[] sprites;

  public Exit(Assets assets) {

    // calls on asset class to get the frames for this object.
    sprites = assets.getAsset("exit")[0];
    animation.setFrames(sprites);
    animation.setDelay(3);

  }

  /**
   * Updates the frame of the object by progressing the counter in it's animation object.
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

