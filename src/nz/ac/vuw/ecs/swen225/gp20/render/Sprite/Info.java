package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class Info {
  private BufferedImage image;
  private int x, y;

  public Info(int x, int y, BufferedImage image){
    this.x = x;
    this.y = y;
    this.image = image;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public BufferedImage getImage(){
    return image;
  }

}
