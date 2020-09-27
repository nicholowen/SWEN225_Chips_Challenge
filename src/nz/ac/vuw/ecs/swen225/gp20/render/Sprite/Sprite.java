package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Renderer.Animation;

public class Sprite {


  Animation animation;

  public Sprite(){
    animation = new Animation();
  }

  //public abstract void update();
  //public abstract BufferedImage getImage();
//  public void update(){}


  /**
   * Gets meta data from cell
   * @param identifier determines which part of the metadata to extract
   * @param cell the cell with the meta data
   * @return if identifier is "name" return the name of the cell, otherwise return the animation frame (for syncing)
   */
  public String getInfo(String identifier, Cell cell){
    String[] metadata = cell.getRenderData().split(":");
    if(identifier.equals("name")) return metadata[0];
    else return metadata[1];
  }
}
