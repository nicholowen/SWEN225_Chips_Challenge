package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class Door extends Sprite {

  // TODO: Make this similar to the other animated objects, including update()
  //  method. Animate door opening or closing depending on when it was opened/closed.

  private String color;
  private BufferedImage[] sprites;
  boolean vertical = false;

  public Door(String c, boolean vertical){

    super();

    this.color = c;
    int index = 0;
      switch (c) {
        case "red":
          index = 0;
          break;
        case "green":
          index = 1;
          break;
        case "blue":
          index = 2;
          break;
        case "yellow":
          index = 3;
          break;

    }
    this.vertical = vertical;
      if(!vertical){
        sprites = Assets.VDOOR[index];
      }else{
        sprites = Assets.DOOR[index];
      }

    animation.setFrames(sprites);
    animation.setSingleAnimation(true);
    animation.setDelay(-1);

  }

  public void update() { animation.update(); }

  public BufferedImage getImage() {
    return animation.getFrame();
  }

  public boolean isVertical(){
    return vertical;
  }


}
