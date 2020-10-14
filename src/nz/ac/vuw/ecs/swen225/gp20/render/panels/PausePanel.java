package nz.ac.vuw.ecs.swen225.gp20.render.panels;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
//================
//button positions
// continue - 377, 280, 135, 21
// save - 377, 348, 135, 21

/**
 * Handles the pause screen.
 * @author Owen N
 */
public class PausePanel extends JPanel {

BufferedImage bg;

  public PausePanel(){
    this.bg = Assets.PAUSE;
  }

  /**
   * Updates, thus repaints the graphics object.
   * TODO: include button detection so the button can be animated (highlighted and/or depressed)
   */
  public void update(){
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(bg, 0, 0, this);
  }

}
