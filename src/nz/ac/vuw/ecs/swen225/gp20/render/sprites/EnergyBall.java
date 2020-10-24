package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a 'treasure' that we have called energy ball. This Class contains the animation frames of the object,
 * and the delay - the time it takes (number of ticks) for the next frame to be fetched.
 *
 * @author Owen Nicholson 300120635
 */
public class EnergyBall {

  Animation animation = new Animation();

  public EnergyBall(Assets assets) {

    // calls on asset class to get the frames for this object.
    animation.setFrames(assets.getAsset("energy")[0]);
    animation.setDelay(6);

  }

  /**
   * Updates the frame of the object by progressing the counter in it's animation object.
   */
  public void update() {
    animation.update();
  }

  /**
   * retrieves the image for the current frame it's in
   * @return Current animation frame
   */
  public BufferedImage getImage() {
    return animation.getFrame();
  }


}
