package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class Player extends Sprite {

  BufferedImage[] sprites;
  String direction = "down";
  Cell[][] cell;

  public Player(){

      super(); //Creates Sprite object for this, in turn creating an animation object which it can access.

      int width = 64;
      int height = 64;

      // calls on asset class to get the frames for this object.
      sprites = Assets.PLAYER[0];
      animation.setFrames(sprites);
      animation.setDelay(6);

  }

  /**
   * Updates the frame of the object
   */
  public void update(String direction){
    this.direction = direction;
  }

  /**
   * retrieves the image for the current frame it's in
   * @return Current animation frame
   */
  public BufferedImage getImage(){
    int frame = 1;
    switch(direction) {
      case "up":
        frame = 0;
        break;
      case "down":
        frame = 1;
        break;
      case "left":
        frame = 2;
        break;
      case "right":
        frame = 3;
        break;
    }

    return animation.getFrame(frame);  }

}