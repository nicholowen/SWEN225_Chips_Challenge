package nz.ac.vuw.ecs.swen225.gp20.render.managers;

import java.awt.image.BufferedImage;

/**
 * Each sprite object gets and instance of this class.
 * Sets the frame of the image depending on the counter (updated every tick).
 * Delay represents the number of ticks it will take to set the next frame.
 *
 * Credit: Zequnyu - https://github.com/zequnyu
 *
 * @author Owen Nicholson 300130635
 */
public class Animation {

  private BufferedImage[] frames;
  private BufferedImage image;
  private int delay;
  private int currentFrame;
  private int numFrames;

  private boolean singleAnimation;

  int count = 0;

  /**
   * Initialises the animation frames, length, and initialises the starting frame (0);
   *
   * @param frames BufferedImage array which contains all images frames in the animation.
   */
  public void setFrames(BufferedImage[] frames) {
    this.frames = frames.clone();
    currentFrame = 0;
    numFrames = frames.length;
  }

  /**
   * Sets new frames without changing the current frame.
   * This changes the row position of the animation.
   * @param frames The row of the image array (usually a different direction).
   */
  public void setNewFrames(BufferedImage[] frames){
    this.frames = frames.clone();
  }

  /**
   * Sets the image explicitly. Used for non-animated objects.
   *
   * @param image single image.
   */
  public void setImage(BufferedImage image) {
    this.image = image;
  }

  /**
   * Sets the delay for the frame.
   *
   * @param delay how many ticks per animation frame
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  public void setSingleAnimation(boolean singleAnimation) {
    this.singleAnimation = singleAnimation;
  }

  /**
   * Increments the frame number of the animation depending on the tick delay.
   * Will revert back to zero to count again when this happens.
   */
  public void update() {
    if (delay == -1) return;
    count++;
    if (count == delay) {
      currentFrame++;
      count = 0;
    }
    if (currentFrame == numFrames) {
      if (singleAnimation) {
        delay = -1;
        currentFrame = numFrames-1;
        return;
      }
      currentFrame = 0;
    }
  }

  /**
   * Retrieves the image of the current frame
   *
   * @return Current frame of the animated object.
   */
  public BufferedImage getFrame() {
    return frames[currentFrame];
  }

  /**
   * Gets the image set by the setImage(), a singular image for non-animated objects
   *
   * @return a 'permanent' image set by the sprite object.
   */
  public BufferedImage getImage() {
    return image;
  }



}
