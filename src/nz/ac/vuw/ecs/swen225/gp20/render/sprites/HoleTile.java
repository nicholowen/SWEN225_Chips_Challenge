package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a wall tile. The wall type will depend on adjacent cells and if there are walls there.
 */
public class HoleTile {

  Animation animation = new Animation();
  BufferedImage[] sprites;

  public HoleTile(Assets assets) {
    sprites = assets.getAsset("water")[0];
  }

  /**
   * Sets the image for this tile depending on neighbouring cells.
   *
   * @param north Tile to the north
   * @param east  Tile to the east
   * @param south Tile to the south
   * @param west  Tile to the west
   */
  public void setWallType(Cell north, Cell east, Cell south, Cell west) {
    boolean n, e, s, w;
    n = e = s = w = false;
    //checks to see if the neighbouring tile is also water
    if (north != null && north.getName().equals("water")) n = true;
    if (east  != null &&  east.getName().equals("water")) e = true;
    if (south != null && south.getName().equals("water")) s = true;
    if (west  != null &&  west.getName().equals("water")) w = true;

    //Only enough tiles to cover level 2 map.
    if (!n && !e &&  s &&  w) animation.setImage(sprites[0]);
    if ( n && !e && !s &&  w) animation.setImage(sprites[1]);
    if (!n &&  e &&  s && !w) animation.setImage(sprites[2]);
    if ( n &&  e && !s && !w) animation.setImage(sprites[3]);
    if (!n &&  e &&  s &&  w) animation.setImage(sprites[4]);
    if ( n &&  e && !s &&  w) animation.setImage(sprites[5]);

  }

  public BufferedImage getImage() {
    return animation.getImage();
  }
}
