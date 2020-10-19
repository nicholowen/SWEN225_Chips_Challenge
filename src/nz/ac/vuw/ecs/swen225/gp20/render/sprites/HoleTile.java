package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents a wall tile. The wall type will depend on adjacent cells and if there are walls there.
 *
 *
 */
public class HoleTile extends Sprite{

  BufferedImage[] sprites;

  public HoleTile(Assets assets){
    super();
    sprites = assets.getAsset("hole")[0];

  }

  /**
   * Sets the image for this tile depending on neighbouring walls
   * > seems to have an issue with this algorithm where it's not doing what I expected.
   * @param north Tile to the north
   * @param east Tile to the east
   * @param south Tile to the south
   * @param west Tile to the west
   */
  public void setWallType(Cell north, Cell east, Cell south, Cell west){
    boolean n, e, s, w;
    n = e = s = w = false;
    //checks to see if the neighbouring tile is a hole
    if(north != null && (getInfo("name", north).equals("water"))) n = true;
    if(east != null && (getInfo("name", east).equals("water"))) e = true;
    if(south != null && (getInfo("name", south).equals("water"))) s = true;
    if(west != null && (getInfo("name", west).equals("water"))) w = true;


    //Only enough tiles to cover level 2
    if(!n && !e &&  s &&  w) animation.setImage(sprites[0]);
    if( n && !e && !s &&  w) animation.setImage(sprites[1]);
    if(!n &&  e &&  s && !w) animation.setImage(sprites[2]);
    if( n &&  e && !s && !w) animation.setImage(sprites[3]);
    if(!n &&  e &&  s &&  w) animation.setImage(sprites[4]);
    if( n &&  e && !s &&  w) animation.setImage(sprites[5]);

  }

  public BufferedImage getImage(){
    return animation.getImage();
  }
  /* Index positions for wall orientation images
   * 0 - front
   * 1 - top
   * 2 - west_end
   * 3 - east_end
   * 4 - north_r
   * 5 - north_l
   * 6 - south_r
   * 7 - south_l
   * 8 - straight_t
   * 9 - west_t
   * 10 - north_t
   * 11 - south_t
   * 12 - east_t
   */
}
