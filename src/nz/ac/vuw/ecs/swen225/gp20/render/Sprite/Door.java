package nz.ac.vuw.ecs.swen225.gp20.render.Sprite;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import java.awt.image.BufferedImage;

public class Door extends Sprite {

  // TODO: Make this similar to the other animated objects, including update()
  //  method. Animate door opening or closing depending on when it was opened/closed.

  public BufferedImage getImage() {
    return Assets.DOOR[0][0];
  }


}
