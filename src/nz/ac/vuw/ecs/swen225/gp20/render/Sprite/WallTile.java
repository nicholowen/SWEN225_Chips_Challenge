package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class WallTile {

  BufferedImage image;

  public WallTile(){

  }

  /**
   * Sets the image for this tile depending on neighbouring walls
   * @param north Tile to the north
   * @param east Tile to the east
   * @param south Tile to the south
   * @param west Tile to the west
   */
  public void setWallType(Cell north, Cell east, Cell south, Cell west){
    boolean n, e, s, w;
    n = e = s = w = false;
    BufferedImage[] img = Assets.WALL[0];
    //checks to see if the neighvbouring tile is a wall
    if(north != null && north.getName().equals("wall")) n = true;
    if(east != null && east.getName().equals("wall")) e = true;
    if(south != null && south.getName().equals("wall")) s = true;
    if(west != null && west.getName().equals("wall")) w = true;


    //unsure if this is going to work as it is giving me warnings
    if(!n && e && !s && w) this.image = img[0];
    else if(n && !e && s && !w) this.image = img[1];
    else if(!n && e && !s && !w) this.image = img[2];
    else if(!n && !e && !s && w) this.image = img[3];
    else if(n && e && !s && !w) this.image = img[4];
    else if(n && !e && !s && w) this.image = img[5];
    else if(!n && e && s && !w) this.image = img[6];
    else if(!n && !e && s && w) this.image = img[7];
    else if(n && e && s && w) this.image = img[8];
    else if(n && !e && s && w) this.image = img[9];
    else if(n && e && !s && w) this.image = img[10];
    else if(!n && e && s && w) this.image = img[11];
    else this.image = img[12];

  }

  public BufferedImage getImage(){
    return image;
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
