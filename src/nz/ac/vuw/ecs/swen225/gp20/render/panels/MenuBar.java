package nz.ac.vuw.ecs.swen225.gp20.render.panels;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class renders the menu bar at the top of the screen.
 * Takes the image direct from the loader.
 */
public class MenuBar extends JMenuBar {
  BufferedImage background;

  public MenuBar() {
    this.background = Assets.MENUBARBG[0][0];
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(background, 0, 0, this);

  }
}