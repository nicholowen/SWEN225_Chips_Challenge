package nz.ac.vuw.ecs.swen225.gp20.render.panels;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Handles the intro fade in - fade out screen with team logo.
 *
 * Fade algorithm credit: Zequnyu - https://github.com/zequnyu
 * @author Owen N
 */
public class IntroPanel extends JPanel {

  BufferedImage logo;

  private int alpha;
  private int tick;

  public IntroPanel(){
    this.logo = Assets.LOGO;
  }

  /**
   * Changes the alpha channel used by the paintComponent method.
   * Low alpha = transparent, high alpha = opaque
   * @return false if fade cycle has finished.
   */
  public boolean update() {
    int fade = 120;
    int length = 190;

    tick++;

    if(tick < fade) {
      alpha = (int)(255 - 255 * (1.0* tick / fade));
      if(alpha < 0) alpha = 0;
    }

    if(tick > fade + length) {
      alpha = (int) (255 * (1.0 * tick - fade - length) / fade);
      if(alpha > 255) alpha = 255;
    }
    return tick <= 2 * fade + length;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.drawImage(logo, 0, 0, this);
    //draws black transparent/opaque rectangle over the logo
    g.setColor(new Color(0, 0, 0, alpha));
    g.fillRect(0, 0, logo.getWidth(), logo.getHeight());
  }

}
