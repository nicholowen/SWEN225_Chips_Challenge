package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a Key Card. This Class contains the animation frames of the object,
 * and the delay - the time it takes (number of ticks) for the next frame to be fetched.
 */
public class KeyCard extends Sprite{

  BufferedImage[] sprites;

  public KeyCard() {
    super();

    int width = 64;
    int height = 64;

    // calls on asset class to get the frames for this object.
    sprites = Assets.KEYCARD_G[0];
    animation.setFrames(sprites);
    animation.setDelay(4);

  }

  /**
   * Updates the frame of the object
   */
  public void update(){
    animation.update();
  }

  /**
   * retrieves the image for the current frame it's in
   * @return Current animation frame
   */
  public BufferedImage getImage(){
    return animation.getFrame();
  }
}
