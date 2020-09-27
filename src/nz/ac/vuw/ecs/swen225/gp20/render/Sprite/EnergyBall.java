package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class EnergyBall extends Sprite{

  BufferedImage[] sprites;

  public EnergyBall(){

    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.

    int width = 64;
    int height = 64;

    // calls on asset class to get the frames for this object.
    sprites = Assets.ENERGYBALL[0];
    animation.setFrames(sprites);
    animation.setDelay(6);

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
    BufferedImage image = animation.getFrame();
    return image;
  }



}
