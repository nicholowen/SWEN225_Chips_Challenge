package nz.ac.vuw.ecs.swen225.gp20.render.sprites;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Animation;
import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.image.BufferedImage;

/**
 * Represents the info tile.
 *
 * @author Owen Nicholson 300120635
 */
public class Info {

  Animation animation = new Animation();

  public Info(Assets assets) {
    animation.setImage(assets.getAsset("info")[0][0]);
  }


  public BufferedImage getImage() {
    return animation.getImage();
  }

}
