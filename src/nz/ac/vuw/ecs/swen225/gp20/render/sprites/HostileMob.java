package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the player. This Class contains the different orientations of the
 * player.
 * TODO: Include the animation frames for movement (running or jumping).
 *
 * @author Owen N
 */
public class HostileMob extends Sprite {

  BufferedImage[] sprites;
  Direction direction = Direction.DOWN;

  int x;
  int y;

  public HostileMob(int x, int y){

    super(); //Creates Sprite object for this, in turn creating an animation object which it can access.

    this.x = x;
    this.y = y;

    // calls on asset class to get the frames for this object.
    sprites = Assets.HOSTILEMOB[0];
    animation.setFrames(sprites);
    animation.setDelay(6);

  }

  /**
   * Updates the frame of the object
   */
  public void update(Direction direction){
    this.direction = direction;
  }

  /**
   * retrieves the image for the current frame it's in
   * @return Current animation frame
   */
  public BufferedImage getImage(){
    int frame = 3;
    switch(direction) {
      case UP:
        frame = 2;
        break;
      case DOWN:
        frame = 3;
        break;
      case LEFT:
        frame = 1;
        break;
      case RIGHT:
        frame = 0;
        break;
      default:
        break;
    }

    return animation.getFrame(frame);
  }

  public int getX() { return x; }
  public int getY() { return y; }


}
