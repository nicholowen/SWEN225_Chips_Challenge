package nz.ac.vuw.ecs.swen225.gp20.render.panels;

import nz.ac.vuw.ecs.swen225.gp20.render.managers.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {
//button positions
// new game - 377, 280, 135, 21
// load - 377, 348, 135, 21
// replay - 377, 416, 135, 21
// exit - 377, 484, 135, 21

  BufferedImage bg;

  public MenuPanel(){
    this.bg = Assets.MENU;
  }

  public void update(){
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(bg, 0, 0, null);
  }
}
