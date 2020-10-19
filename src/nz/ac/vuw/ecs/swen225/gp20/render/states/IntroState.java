package nz.ac.vuw.ecs.swen225.gp20.render.states;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Handles the intro fade in - fade out screen with team logo.
 *
 * Fade algorithm credit: Zequnyu - https://github.com/zequnyu
 * @author Owen Nicholson 300130653
 */
public class IntroState {

  Assets assets;

  BufferedImage logo;

  private int alpha;
  private int tick;

  public IntroState(Assets assets){
    this.assets = assets;
    init();
  }

  private void init(){
    this.logo = assets.getAsset("logo")[0][0];
  }

  /**
   * Changes the alpha channel used by the draw method.
   * Low alpha = transparent, high alpha = opaque
   */
  public void update() {
    int fade = 30;
    int length = 40;

    tick++;

    if(tick < fade) {
      alpha = (int)(255 - 255 * (1.0 * tick / fade));
      if(alpha < 0) alpha = 0;
    }

    if(tick > fade + length) {
      alpha = (int) (255 * (1.0 * tick - fade - length) / fade);
      if(alpha > 255) alpha = 255;
    }
  }

  public void draw(Graphics g) {
//    super.paintComponent(g);
    g.drawImage(logo, 0, 0, null);
    //draws black transparent/opaque rectangle over the logo
    g.setColor(new Color(0, 0, 0, alpha));
    g.fillRect(0, 0, logo.getWidth(), logo.getHeight());
  }

}
