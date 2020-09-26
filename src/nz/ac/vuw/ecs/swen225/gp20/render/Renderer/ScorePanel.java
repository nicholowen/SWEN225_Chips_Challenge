package nz.ac.vuw.ecs.swen225.gp20.render.Renderer;

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

  File path = new File("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/");
  Canvas canvas;
  private BufferedImage image;
  private Image background;
  private static Graphics g;
  boolean thing = false;

  public ScorePanel() {
    setPreferredSize(new Dimension(300, 576));
    this.background = loadBackground();
    setFocusable(true);
    requestFocus();

    init();
  }

  private void init() {
    try {
      image = ImageIO.read(new File(path, "templogo.gif"));
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("Error loading graphics.");
      System.exit(0);
    }
    g = image.getGraphics();
  }

  private BufferedImage loadBackground(){
    File path = new File("src/nz/ac/vuw/ecs/swen225/gp20/render/Resources/");
    try{
      BufferedImage bf = ImageIO.read(new File(path, "scorepanelbackground.gif"));
      return bf;
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(background, 0, 0, this);
    }

  }

}
