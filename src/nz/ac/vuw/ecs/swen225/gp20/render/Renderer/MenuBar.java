package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class MenuBar extends JMenuBar {
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