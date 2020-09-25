package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class WallTile extends Sprite{

  BufferedImage image;
  BufferedImage[] sprites;

  public WallTile(){

    super();
    sprites = Assets.WALL[0];
    animation.setDelay(-1);

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
    //checks to see if the neighbouring tile is a wall or a door (walls link up to doors)
    if(north != null && (getInfo("name", north).equals("wall") || getInfo("name", north).equals("door"))) n = true;
    if(east != null && (getInfo("name", east).equals("wall") || getInfo("name", east).equals("door"))) e = true;
    if(south != null && (getInfo("name", south).equals("wall") || getInfo("name", south).equals("door"))) s = true;
    if(west != null && (getInfo("name", west).equals("wall") || getInfo("name", west).equals("door"))) w = true;


    //unsure if this is going to work as it is giving me warnings
    if(n && !e && s && !w) animation.setImage(img[0]);
    if(!n && e && !s && w) animation.setImage(img[1]);
    if(!n && e && !s && !w) animation.setImage(img[2]);
    if(!n && !e && !s && w) animation.setImage(img[3]);
    if(n && e && !s && !w) animation.setImage(img[5]);
    if(n && !e && !s && w) animation.setImage(img[10]);
    if(!n && e && s && !w) animation.setImage(img[4]);
    if(!n && !e && s && w) animation.setImage(img[7]);
    if(n && e && s && w) animation.setImage(img[8]);
    if(n && !e && s && w) animation.setImage(img[6]);
    if(n && e && !s && w) animation.setImage(img[9]);
    if(!n && e && s && w) animation.setImage(img[12]);
    if(n && !e && s && w) animation.setImage(img[11]);

  }

  public BufferedImage getImage(){
    BufferedImage image = animation.getImage();
    return image;
  }

//  @Override
//  public void update() {
//    //unused
//  }
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
