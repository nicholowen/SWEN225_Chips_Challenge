package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.render.Assets;

import javax.swing.*;
import java.awt.*;

class MenuBar extends JMenuBar {
  Image background;

  public MenuBar() {
    this.background = new ImageIcon(Assets.MENUBARBG[0][0]).getImage();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(background, 0, 0, this);

  }
}