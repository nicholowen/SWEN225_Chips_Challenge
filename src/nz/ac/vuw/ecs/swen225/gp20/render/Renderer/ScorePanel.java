package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScorePanel extends JPanel {

  /*===============================================================
   * PLACE HOLDER - will show the score board, time, and inventory
   *===============================================================
   * contains some garbage code to draw (can see what I am doing)
   */

  private Image background;

  public ScorePanel() {
    setPreferredSize(new Dimension(300, 576));
    this.background = loadBackground();
    requestFocus();

  }


  private BufferedImage loadBackground(){
    return Assets.SCOREBACKGROUND[0][0];
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
      g.drawImage(background, 0, 0, this);

  }

}
