package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import java.awt.image.BufferedImage;

public class Animation {

  BufferedImage[] frames;
  BufferedImage image;
  public int delay;
  public int currentFrame;
  public int numFrames;

  int count = 0;

  /**
   * Initialises the animation frames, length, and initialises the starting frame (0);
   * @param frames BufferedImage array which contains all images frames in the animation.
   */
  public void setFrames(BufferedImage[] frames){
    this.frames = frames;
    currentFrame = 0;
    numFrames = frames.length;
  }

  public void setImage(BufferedImage image){
    this.image = image;
  }
  /**
   * Sets the delay for the frame.
   * @param delay how many ticks per animation frame
   */
  public void setDelay(int delay){
    this.delay = delay;
  }

  /**
   * Increments the frame number of the animation.
   */
  public void update() {
    if(delay == -1) return;
    count++;
    if(count == delay) {
      currentFrame++;
      count = 0;
    }
    if(currentFrame == numFrames) {
      currentFrame = 0;
    }
  }

  /**
   * Retrieves the image of the current frame
   * @return Current frame of the animated object.
   */
  public BufferedImage getFrame(){
    return frames[currentFrame];
  }
  public BufferedImage getFrame(int i){ return frames[i]; }
  public BufferedImage getImage() { return image; }


}
