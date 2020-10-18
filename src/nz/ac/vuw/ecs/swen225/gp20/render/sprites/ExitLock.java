package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the exit lock of the level.
 *
 * @author Owen Nicholson 300120635
 */
public class ExitLock extends Sprite {

  BufferedImage[] sprites;
  int x, y;

  public ExitLock(int x, int y) {

    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.


    this.x = x;
    this.y = y;

    // calls on asset class to get the frames for this object.
    sprites = Assets.EXITLOCK[0];
    animation.setFrames(sprites);
    animation.setDelay(2);

  }

  /**
   * Updates the frame of the object by progressing the counter in its animation object.
   */
  public void update() { animation.update(); }

  /**
   * retrieves the image for the current frame it's in
   *
   * @return Current animation frame
   */
  public BufferedImage getImage() {
    BufferedImage image = animation.getFrame();
    return image;
  }
  /**
   * Get x coordinate of this object (same as the corresponding cell)
   * @return x
   */
  public int getX(){ return x; }
  /**
   * Get y coordinate of this object (same as the corresponding cell)
   * @return y
   */
  public int getY(){ return y; }

}