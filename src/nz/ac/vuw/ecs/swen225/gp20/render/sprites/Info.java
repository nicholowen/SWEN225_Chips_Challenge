package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the Information screen on the 'score panel'.
 * Get's updated and information rendered when the player is on the info cell.
 *
 * @author Owen Nicholson 300120635
 */
public class Info {
  private BufferedImage image;
  private int x, y;

  public Info(Assets assets, int x, int y, BufferedImage image) {
    this.x = x;
    this.y = y;
    this.image = image;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public BufferedImage getImage() {
    return image;
  }

}
