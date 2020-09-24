package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

import nz.ac.vuw.ecs.swen225.gp20.maze.Cell;
import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.EnergyBall;
import nz.ac.vuw.ecs.swen225.gp20.render.Sprite.KeyCard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

  //  Assets assets = new Assets();

  EnergyBall eb;
  KeyCard kcg;

  public GamePanel() {
    setPreferredSize(new Dimension(500, 500));
    setBackground(Color.gray);
    setFocusable(true);
    requestFocus();
    init();
  }

  /**
   * Initialises all objects - if they are created in other classes, can reference them here with getters.
   * I would assume all objects will be kept in arrays..?
   */
  private void init() {
    BufferedImage image = new BufferedImage(500, 500, 1);
    eb = new EnergyBall(null);
    kcg = new KeyCard(null);
    Graphics g = image.getGraphics();

  }

  public void update(Cell[][] cells){

    //get player position

    //iterate through

    for(int i = 0; i < cells.length; i++){
      for(int j = 0; j < cells[i].length; j++){
        //if it's a wall, draw
      }
    }
    /*TODO: Update all objects in game - iterate through the tiles around player.
            Update all doors (if closed, won't update, if open, updating it's key frame).
            Update all animated objects such as Balls, Keys etc.
            Update player depending on facing direction and if moving or not.
     */

    eb.update();
    kcg.update();

    // TODO repaint();
  }

  /**
   * Test method for animations - based of system time. can swap out when tick system is implemented (runs at 30FPS)
   */
  public void run() {
    while(true) {
      Cell[][] cell = new Cell[0][0];
      this.update(cell);
      repaint();
      long start = System.currentTimeMillis();
      while (true) {
        long delay = 30;
        if (System.currentTimeMillis() >= start + delay) {
          break;
        }

      }
    }
  }



  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // draw each set of images in turn
    // Tiles -> Doors -> Balls/Key/Items -> Player
    g.drawImage(eb.getImage(), 0, 0, this);
    g.drawImage(kcg.getImage(), 64, 0, this);

  }

}